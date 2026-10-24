package com.java2nb.novel.service;

import com.github.pagehelper.PageHelper;
import com.java2nb.novel.core.cache.CacheKey;
import com.java2nb.novel.core.cache.CacheService;
import com.java2nb.novel.entity.Book;
import com.java2nb.novel.entity.BookCategory;
import com.java2nb.novel.entity.BookComment;
import com.java2nb.novel.entity.BookContent;
import com.java2nb.novel.entity.BookIndex;
import com.java2nb.novel.entity.BookSetting;
import com.java2nb.novel.mapper.BookCategoryDynamicSqlSupport;
import com.java2nb.novel.mapper.BookCategoryMapper;
import com.java2nb.novel.mapper.BookCommentDynamicSqlSupport;
import com.java2nb.novel.mapper.BookCommentMapper;
import com.java2nb.novel.mapper.BookContentDynamicSqlSupport;
import com.java2nb.novel.mapper.BookContentMapper;
import com.java2nb.novel.mapper.BookDynamicSqlSupport;
import com.java2nb.novel.mapper.BookIndexDynamicSqlSupport;
import com.java2nb.novel.mapper.BookIndexMapper;
import com.java2nb.novel.mapper.BookMapper;
import com.java2nb.novel.mapper.BookSettingDynamicSqlSupport;
import com.java2nb.novel.mapper.BookSettingMapper;
import io.github.xxyopen.model.page.PageBean;
import io.github.xxyopen.model.page.builder.pagehelper.PageBuilder;
import io.github.xxyopen.util.IdWorker;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static com.java2nb.novel.core.utils.AdminUtil.biz;
import static com.java2nb.novel.core.utils.AdminUtil.like;
import static com.java2nb.novel.core.utils.AdminUtil.parseIds;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;
import static org.mybatis.dynamic.sql.SqlBuilder.isIn;
import static org.mybatis.dynamic.sql.SqlBuilder.isLike;
import static org.mybatis.dynamic.sql.SqlBuilder.isLikeWhenPresent;
import static org.mybatis.dynamic.sql.SqlBuilder.isNotEqualTo;
import static org.mybatis.dynamic.sql.SqlBuilder.isNotNull;
import static org.mybatis.dynamic.sql.SqlBuilder.isNull;
import static org.mybatis.dynamic.sql.SqlBuilder.or;

/**
 * 小说管理Service：小说列表/章节管理/内容管理
 *
 * @author Administrator
 */
@Service
@RequiredArgsConstructor
public class BookService {

    private final BookMapper bookMapper;

    private final BookIndexMapper bookIndexMapper;

    private final BookContentMapper bookContentMapper;

    private final BookCommentMapper bookCommentMapper;

    private final BookSettingMapper bookSettingMapper;

    private final BookCategoryMapper bookCategoryMapper;

    private final CacheService cacheService;

    private final IdWorker idWorker = IdWorker.INSTANCE;

    /**
     * 内置小说分类ID上限：id <= 该值的分类为系统预置（玄幻奇幻~女生频道），只允许修改不允许删除
     */
    private static final int BUILTIN_CATEGORY_MAX_ID = 7;

    public PageBean<Book> bookList(String keyword, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        // 默认按创建时间倒序（新书在前）；爬虫批量入库的书 create_time 常相同，
        // 故加 update_time 做次级排序，保证分页稳定不重不漏
        List<Book> list = bookMapper.select(c -> {
            if (StringUtils.isNotBlank(keyword)) {
                // 书名或作者名任一匹配
                String likeVal = like(keyword);
                return c.where(BookDynamicSqlSupport.bookName, isLike(likeVal))
                        .or(BookDynamicSqlSupport.authorName, isLike(likeVal))
                        .orderBy(BookDynamicSqlSupport.createTime.descending(), BookDynamicSqlSupport.updateTime.descending());
            }
            return c.orderBy(BookDynamicSqlSupport.createTime.descending(), BookDynamicSqlSupport.updateTime.descending());
        });
        return PageBuilder.build(list);
    }

    public Book bookGet(Long id) {
        return bookMapper.selectByPrimaryKey(id).orElseThrow(() -> biz("小说不存在"));
    }

    public void bookSave(Book book) {
        throw biz("小说列表不支持新增");
    }

    public void bookUpdate(Book book) {
        throw biz("小说列表不支持修改");
    }

    /**
     * 小说上架/下架（status：0 下架/未上架，1 上架）。
     * 下架后前台所有小说查询接口均不再返回该小说，故需失效前台首页榜单/推荐缓存。
     *
     * @param id     小说ID
     * @param status 目标状态
     */
    public void bookStatus(Long id, Byte status) {
        if (status == null || (status != 0 && status != 1)) {
            throw biz("状态值不合法");
        }
        Book exist = bookMapper.selectByPrimaryKey(id).orElseThrow(() -> biz("小说不存在"));
        Date now = new Date();
        // 爬虫采集的书重新上架后重置最后章节更新时间，使其立即回到近30天的自动更新队列；equalToWhenPresent 保证下架/非爬虫书不修改该字段
        Date lastIndexUpdateTime = (status == 1 && exist.getCrawlSourceId() != null) ? now : null;
        bookMapper.update(u -> u.set(BookDynamicSqlSupport.status).equalTo(status)
                .set(BookDynamicSqlSupport.updateTime).equalTo(now)
                .set(BookDynamicSqlSupport.lastIndexUpdateTime).equalToWhenPresent(lastIndexUpdateTime)
                .where(BookDynamicSqlSupport.id, isEqualTo(id)));
        // 上架状态变更后前台榜单与首页推荐缓存失效，下次访问按 status=1 重新加载
        cacheService.del(CacheKey.INDEX_CLICK_BANK_BOOK_KEY);
        cacheService.del(CacheKey.INDEX_NEW_BOOK_KEY);
        cacheService.del(CacheKey.INDEX_UPDATE_BOOK_KEY);
        cacheService.del(CacheKey.INDEX_BOOK_SETTINGS_KEY);
    }

    /**
     * 一键上架所有小说（仅处理当前非上架的书），上架后失效前台榜单/推荐缓存。
     * 爬虫采集的书同步重置最后章节更新时间，与单本上架逻辑保持一致，确保能回到近30天自动更新队列。
     *
     * @return 本次上架的小说数量
     */
    @Transactional(rollbackFor = Exception.class)
    public int bookOnlineAll() {
        Date now = new Date();
        // 爬虫书：上架 + 重置章节更新时间
        int count = bookMapper.update(u -> u.set(BookDynamicSqlSupport.status).equalTo((byte) 1)
                .set(BookDynamicSqlSupport.updateTime).equalTo(now)
                .set(BookDynamicSqlSupport.lastIndexUpdateTime).equalTo(now)
                .where(BookDynamicSqlSupport.crawlSourceId, isNotNull())
                .and(BookDynamicSqlSupport.status, isNotEqualTo((byte) 1), or(BookDynamicSqlSupport.status, isNull())));
        // 非爬虫书：仅上架，不碰章节更新时间
        count += bookMapper.update(u -> u.set(BookDynamicSqlSupport.status).equalTo((byte) 1)
                .set(BookDynamicSqlSupport.updateTime).equalTo(now)
                .where(BookDynamicSqlSupport.crawlSourceId, isNull())
                .and(BookDynamicSqlSupport.status, isNotEqualTo((byte) 1), or(BookDynamicSqlSupport.status, isNull())));
        if (count > 0) {
            cacheService.del(CacheKey.INDEX_CLICK_BANK_BOOK_KEY);
            cacheService.del(CacheKey.INDEX_NEW_BOOK_KEY);
            cacheService.del(CacheKey.INDEX_UPDATE_BOOK_KEY);
            cacheService.del(CacheKey.INDEX_BOOK_SETTINGS_KEY);
        }
        return count;
    }

    @Transactional(rollbackFor = Exception.class)
    public void bookRemove(Long id) {
        // 级联删除章节内容与小说本体
        List<BookIndex> indexList = bookIndexMapper.select(c -> c.where(BookIndexDynamicSqlSupport.bookId, isEqualTo(id)));
        removeContentByIndex(indexList.stream().map(BookIndex::getId).toList());
        bookIndexMapper.delete(c -> c.where(BookIndexDynamicSqlSupport.bookId, isEqualTo(id)));
        bookCommentMapper.delete(c -> c.where(BookCommentDynamicSqlSupport.bookId, isEqualTo(id)));
        bookSettingMapper.delete(c -> c.where(BookSettingDynamicSqlSupport.bookId, isEqualTo(id)));
        bookMapper.deleteByPrimaryKey(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void bookBatchRemove(String ids) {
        for (Long id : parseIds(ids)) {
            bookRemove(id);
        }
    }

    /**
     * 下载小说全部正文（TXT文本）
     *
     * @param bookId 小说ID
     * @return 小说正文
     */
    public String bookDownload(Long bookId) {
        Book book = bookGet(bookId);
        List<BookIndex> indexList = bookIndexMapper.select(c -> c
                .where(BookIndexDynamicSqlSupport.bookId, isEqualTo(bookId))
                .orderBy(BookIndexDynamicSqlSupport.indexNum));
        StringBuilder sb = new StringBuilder();
        sb.append("《").append(book.getBookName()).append("》\n");
        for (BookIndex index : indexList) {
            sb.append("\n").append(index.getIndexName()).append("\n");
            bookContentMapper.select(c -> c.where(BookContentDynamicSqlSupport.indexId, isEqualTo(index.getId())))
                    .stream().findFirst().ifPresent(content -> sb.append(htmlToText(content.getContent())).append("\n"));
        }
        // 章节衔接处可能叠加出多个空行，全文统一压缩
        return sb.toString().replaceAll("\n{3,}", "\n\n");
    }

    /**
     * 将库内 HTML 内容转为纯文本：换行类标签转换行符，&nbsp; 转空格，剔除其余标签并解码常见实体
     *
     * @param html 原始内容
     * @return 纯文本
     */
    private String htmlToText(String html) {
        if (html == null || html.isEmpty()) {
            return "";
        }
        String text = html
                // 统一换行符：CRLF 或单独的 CR 都归一为 LF，避免后续空行压缩被 CR 隔断失效
                .replaceAll("\r\n?", "\n")
                // 块尾补换行，避免 </p></div> 后两段粘连
                .replaceAll("(?i)</(p|div|h[1-6]|li|blockquote)>", "\n")
                // 换行标签统一转换行符
                .replaceAll("(?i)<br\\s*/?>", "\n")
                // 段落/制表符转空格（正文缩进常用）
                .replace("&nbsp;", " ")
                .replace("&ensp;", " ")
                .replace("&emsp;", "  ")
                .replaceAll("&#\\d+;", "")
                // 剔除其余所有标签
                .replaceAll("(?i)<[^>]+>", "")
                // 解码常见实体（顺序不可调，&amp; 必须最后）
                .replace("&quot;", "\"")
                .replace("&#39;", "'")
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&amp;", "&");
        // 清理多余空白：3 个以上连续换行压缩为一个段落分隔，行尾空白去掉
        return text.replaceAll("[ \\t]+\n", "\n").replaceAll("\n{3,}", "\n\n").trim() + "\n";
    }

    private void fillCatName(Book book) {
        if (book.getCatId() != null) {
            bookCategoryMapper.selectByPrimaryKey(book.getCatId())
                    .ifPresent(category -> book.setCatName(category.getName()));
        }
    }

    public PageBean<BookIndex> indexList(Long bookId, String keyword, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        List<BookIndex> list = bookIndexMapper.select(c -> c
                .where(BookIndexDynamicSqlSupport.bookId, isEqualTo(bookId))
                .and(BookIndexDynamicSqlSupport.indexName, isLikeWhenPresent(like(keyword)))
                .orderBy(BookIndexDynamicSqlSupport.indexNum));
        return PageBuilder.build(list);
    }

    public BookIndex indexGet(Long id) {
        return bookIndexMapper.selectByPrimaryKey(id).orElseThrow(() -> biz("章节不存在"));
    }

    public void indexSave(BookIndex bookIndex) {
        throw biz("章节列表不支持新增");
    }

    public void indexUpdate(BookIndex bookIndex) {
        throw biz("章节列表不支持修改");
    }

    @Transactional(rollbackFor = Exception.class)
    public void indexRemove(Long id) {
        removeContentByIndex(List.of(id));
        bookIndexMapper.deleteByPrimaryKey(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void indexBatchRemove(String ids) {
        List<Long> idList = parseIds(ids);
        if (idList.isEmpty()) {
            return;
        }
        removeContentByIndex(idList);
        bookIndexMapper.delete(c -> c.where(BookIndexDynamicSqlSupport.id, isIn(idList)));
    }

    private void removeContentByIndex(List<Long> indexIds) {
        if (!indexIds.isEmpty()) {
            bookContentMapper.delete(c -> c.where(BookContentDynamicSqlSupport.indexId, isIn(indexIds)));
        }
    }

    public BookContent contentGet(Long indexId) {
        return bookContentMapper.select(c -> c.where(BookContentDynamicSqlSupport.indexId, isEqualTo(indexId)))
                .stream().findFirst().orElse(null);
    }

    /**
     * 章节内容仅供查看，不支持编辑
     *
     * @param indexId 章节ID
     * @param content 章节内容
     */
    public void contentSave(Long indexId, String content) {
        throw biz("章节内容仅供查看，不支持修改");
    }

}

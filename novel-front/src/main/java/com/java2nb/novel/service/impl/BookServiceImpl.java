package com.java2nb.novel.service.impl;

import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import com.github.pagehelper.PageHelper;
import com.java2nb.novel.core.cache.CacheKey;
import com.java2nb.novel.core.cache.CacheService;
import com.java2nb.novel.core.config.BookPriceProperties;
import com.java2nb.novel.core.enums.ResponseStatus;
import com.java2nb.novel.core.utils.Constants;
import com.java2nb.novel.core.utils.FileUtil;
import com.java2nb.novel.core.utils.IndexStatusUtil;
import com.java2nb.novel.core.utils.StringUtil;
import com.java2nb.novel.entity.Book;
import com.java2nb.novel.entity.*;
import com.java2nb.novel.mapper.*;
import com.java2nb.novel.service.AuthorService;
import com.java2nb.novel.service.BookService;
import com.java2nb.novel.service.FileService;
import com.java2nb.novel.service.LikeService;
import com.java2nb.novel.service.UserMessageService;
import com.java2nb.novel.vo.*;
import io.github.xxyopen.model.page.PageBean;
import io.github.xxyopen.model.page.builder.pagehelper.PageBuilder;
import io.github.xxyopen.util.IdWorker;
import io.github.xxyopen.web.exception.BusinessException;
import io.github.xxyopen.web.util.BeanUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.DateUtils;
import org.mybatis.dynamic.sql.SortSpecification;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.mybatis.dynamic.sql.select.QueryExpressionDSL;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.springframework.ai.image.Image;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

import static com.java2nb.novel.mapper.BookCategoryDynamicSqlSupport.bookCategory;
import static com.java2nb.novel.mapper.BookCommentDynamicSqlSupport.bookComment;
import static com.java2nb.novel.mapper.BookContentDynamicSqlSupport.bookContent;
import static com.java2nb.novel.mapper.BookContentDynamicSqlSupport.content;
import static com.java2nb.novel.mapper.BookDynamicSqlSupport.*;
import static com.java2nb.novel.mapper.BookDynamicSqlSupport.book;
import static com.java2nb.novel.mapper.BookIndexDynamicSqlSupport.bookIndex;
import static com.java2nb.novel.mapper.BookSettingDynamicSqlSupport.bookSetting;
import static org.mybatis.dynamic.sql.SqlBuilder.*;
import static org.mybatis.dynamic.sql.select.SelectDSL.select;

/**
 * @author 11797
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

    /**
     * 本地图片保存路径
     */
    @Value("${pic.save.path}")
    private String picSavePath;

    private final FrontBookSettingMapper bookSettingMapper;

    private final FrontBookMapper bookMapper;

    private final BookCategoryMapper bookCategoryMapper;

    private final BookIndexMapper bookIndexMapper;

    private final BookContentMapper bookContentMapper;

    private final FrontBookCommentMapper bookCommentMapper;

    private final FrontBookCommentReplyMapper bookCommentReplyMapper;

    private final BookAuthorMapper bookAuthorMapper;

    private final CacheService cacheService;

    private final AuthorService authorService;

    private final FileService fileService;

    private final LikeService likeService;

    private final UserMessageService userMessageService;

    private final BookPriceProperties bookPriceConfig;

    private final OpenAiImageModel openAiImageModel;

    private final ThreadPoolExecutor threadPoolExecutor;

    private final IdWorker idWorker = IdWorker.INSTANCE;

    /**
     * 小说上架状态：仅 status=1 的小说对读者可见（0 为入库/已下架）
     */
    private static final Byte BOOK_STATUS_ONLINE = 1;

    /**
     * 读者可见章节条件：已发布，或定时发布且已到时（兜底防定时任务延迟）
     */
    private static org.mybatis.dynamic.sql.AndOrCriteriaGroup readerVisibleCriterion() {
        return or(BookIndexDynamicSqlSupport.status, isEqualTo(IndexStatusUtil.STATUS_SCHEDULED),
            and(BookIndexDynamicSqlSupport.publishTime, isLessThanOrEqualTo(new Date())));
    }


    @SneakyThrows
    @Override
    public Map<String, List<BookSettingVO>> listBookSettingVO() {
        List<BookSettingVO> list = cacheService.getList(CacheKey.INDEX_BOOK_SETTINGS_KEY, BookSettingVO.class);
        if (list == null || list.isEmpty()) {
            list = bookSettingMapper.listVO();
            if (list.isEmpty()) {
                //如果首页小说没有被设置，则初始化首页小说设置
                list = initIndexBookSetting();
            }
            cacheService.setObject(CacheKey.INDEX_BOOK_SETTINGS_KEY, list, 3600 * 24);
        }
        return list.stream().collect(
            Collectors.groupingBy(book -> book.getType().toString())
        );
    }


    /**
     * 初始化首页小说设置
     */
    private List<BookSettingVO> initIndexBookSetting() {
        Date currentDate = new Date();
        List<Book> books = bookMapper.selectIdsByScoreAndRandom(Constants.INDEX_BOOK_SETTING_NUM);
        if (books.size() == Constants.INDEX_BOOK_SETTING_NUM) {
            List<BookSetting> bookSettingList = new ArrayList<>(Constants.INDEX_BOOK_SETTING_NUM);
            List<BookSettingVO> bookSettingVOList = new ArrayList<>(Constants.INDEX_BOOK_SETTING_NUM);
            for (int i = 0; i < books.size(); i++) {
                Book book = books.get(i);
                byte type;
                if (i < 4) {
                    type = 0;
                } else if (i < 14) {
                    type = 1;
                } else if (i < 19) {
                    type = 2;
                } else if (i < 25) {
                    type = 3;
                } else {
                    type = 4;
                }
                BookSettingVO bookSettingVO = new BookSettingVO();
                BookSetting bookSetting = new BookSetting();
                bookSetting.setType(type);
                bookSetting.setSort((byte) i);
                bookSetting.setBookId(book.getId());
                bookSetting.setCreateTime(currentDate);
                bookSetting.setUpdateTime(currentDate);
                bookSettingList.add(bookSetting);

                BeanUtils.copyProperties(book, bookSettingVO);
                BeanUtils.copyProperties(bookSetting, bookSettingVO);
                bookSettingVOList.add(bookSettingVO);
            }

            bookSettingMapper.delete(deleteFrom(bookSetting).build()
                .render(RenderingStrategies.MYBATIS3));
            bookSettingMapper.insertMultiple(bookSettingList);

            return bookSettingVOList;
        }
        return new ArrayList<>(0);
    }

    @Override
    public List<Book> listClickRank() {
        List<Book> result = cacheService.getList(CacheKey.INDEX_CLICK_BANK_BOOK_KEY, Book.class);
        if (result == null || result.isEmpty()) {
            result = listRank((byte) 0, 10);
            cacheService.setObject(CacheKey.INDEX_CLICK_BANK_BOOK_KEY, result, 5000);
        }
        return result;
    }

    @Override
    public List<Book> listNewRank() {
        List<Book> result = cacheService.getList(CacheKey.INDEX_NEW_BOOK_KEY, Book.class);
        if (result == null || result.isEmpty()) {
            result = listRank((byte) 1, 10);
            cacheService.setObject(CacheKey.INDEX_NEW_BOOK_KEY, result, 3600);
        }
        return result;
    }

    @Override
    public List<BookVO> listUpdateRank() {
        List<BookVO> result = cacheService.getList(CacheKey.INDEX_UPDATE_BOOK_KEY, BookVO.class);
        if (result == null || result.isEmpty()) {
            List<Book> bookPOList = listRank((byte) 2, 23);
            result = BeanUtil.copyList(bookPOList, BookVO.class);
            cacheService.setObject(CacheKey.INDEX_UPDATE_BOOK_KEY, result, 60 * 10);
        }
        return result;
    }

    @Override
    public PageBean<?> searchByPage(BookSpVO params, int page, int pageSize) {

        if (params.getUpdatePeriod() != null) {
            long cur = System.currentTimeMillis();
            long period = params.getUpdatePeriod() * 24 * 3600 * 1000;
            long time = cur - period;
            params.setUpdateTimeMin(new Date(time));
        }

        PageHelper.startPage(page, pageSize);

        return PageBuilder.build(bookMapper.searchByPage(params));

    }

    @Override
    public List<BookCategory> listBookCategory() {
        SelectStatementProvider selectStatementProvider = select(BookCategoryDynamicSqlSupport.id,
            BookCategoryDynamicSqlSupport.name, BookCategoryDynamicSqlSupport.workDirection)
            .from(bookCategory)
            .orderBy(BookCategoryDynamicSqlSupport.sort)
            .build()
            .render(RenderingStrategies.MYBATIS3);
        return bookCategoryMapper.selectMany(selectStatementProvider);
    }

    @Override
    public Book queryBookDetail(Long bookId) {
        SelectStatementProvider selectStatement = select(book.allColumns())
            .from(book)
            .where(id, isEqualTo(bookId))
            .build()
            .render(RenderingStrategies.MYBATIS3);
        return bookMapper.selectMany(selectStatement).get(0);
    }

    @Override
    public Book queryOnlineBookDetail(Long bookId) {
        // 读者端详情：仅允许查询已上架小说，下架或不存在均视为不可见
        SelectStatementProvider selectStatement = select(book.allColumns())
            .from(book)
            .where(id, isEqualTo(bookId))
            .and(status, isEqualTo(BOOK_STATUS_ONLINE))
            .build()
            .render(RenderingStrategies.MYBATIS3);
        return bookMapper.selectMany(selectStatement).stream().findFirst()
            .orElseThrow(() -> new BusinessException(ResponseStatus.BOOK_NOT_EXISTS));
    }

    @Override
    public List<BookIndex> queryIndexList(Long bookId, String orderBy, Integer page, Integer pageSize) {
        if (page != null && pageSize != null) {
            PageHelper.startPage(page, pageSize);
        }
        QueryExpressionDSL<org.mybatis.dynamic.sql.select.SelectModel>.QueryExpressionWhereBuilder where = select(
            BookIndexDynamicSqlSupport.id,
            BookIndexDynamicSqlSupport.bookId, BookIndexDynamicSqlSupport.indexNum,
            BookIndexDynamicSqlSupport.indexName, BookIndexDynamicSqlSupport.updateTime,
            BookIndexDynamicSqlSupport.isVip)
            .from(bookIndex)
            .where(BookIndexDynamicSqlSupport.bookId, isEqualTo(bookId))
            // 读者端目录只展示已发布（或已到时的定时）章节，草稿不可见
            .and(BookIndexDynamicSqlSupport.status, isEqualTo(IndexStatusUtil.STATUS_PUBLISHED), readerVisibleCriterion());
        if ("index_num desc".equals(orderBy)) {
            where.orderBy(BookIndexDynamicSqlSupport.indexNum.descending());
        }
        return bookIndexMapper.selectMany(where
            .build()
            .render(RenderingStrategies.MYBATIS3));
    }


    @Override
    public BookIndex queryBookIndex(Long bookIndexId) {
        SelectStatementProvider selectStatement = select(BookIndexDynamicSqlSupport.id,
            BookIndexDynamicSqlSupport.bookId, BookIndexDynamicSqlSupport.indexNum,
            BookIndexDynamicSqlSupport.indexName, BookIndexDynamicSqlSupport.wordCount,
            BookIndexDynamicSqlSupport.bookPrice, BookIndexDynamicSqlSupport.updateTime,
            BookIndexDynamicSqlSupport.isVip, BookIndexDynamicSqlSupport.storageType,
            BookIndexDynamicSqlSupport.status, BookIndexDynamicSqlSupport.publishTime)
            .from(bookIndex)
            .where(BookIndexDynamicSqlSupport.id, isEqualTo(bookIndexId))
            .build()
            .render(RenderingStrategies.MYBATIS3);
        return bookIndexMapper.selectMany(selectStatement).get(0);
    }

    @Override
    public Long queryPreBookIndexId(Long bookId, Integer indexNum) {
        SelectStatementProvider selectStatement = select(BookIndexDynamicSqlSupport.id)
            .from(bookIndex)
            .where(BookIndexDynamicSqlSupport.bookId, isEqualTo(bookId))
            .and(BookIndexDynamicSqlSupport.indexNum, isLessThan(indexNum))
            .and(BookIndexDynamicSqlSupport.status, isEqualTo(IndexStatusUtil.STATUS_PUBLISHED), readerVisibleCriterion())
            .orderBy(BookIndexDynamicSqlSupport.indexNum.descending())
            .limit(1)
            .build()
            .render(RenderingStrategies.MYBATIS3);
        List<BookIndex> list = bookIndexMapper.selectMany(selectStatement);
        if (list.size() == 0) {
            return 0L;
        } else {
            return list.get(0).getId();
        }
    }

    @Override
    public Long queryNextBookIndexId(Long bookId, Integer indexNum) {
        SelectStatementProvider selectStatement = select(BookIndexDynamicSqlSupport.id)
            .from(bookIndex)
            .where(BookIndexDynamicSqlSupport.bookId, isEqualTo(bookId))
            .and(BookIndexDynamicSqlSupport.indexNum, isGreaterThan(indexNum))
            .and(BookIndexDynamicSqlSupport.status, isEqualTo(IndexStatusUtil.STATUS_PUBLISHED), readerVisibleCriterion())
            .orderBy(BookIndexDynamicSqlSupport.indexNum)
            .limit(1)
            .build()
            .render(RenderingStrategies.MYBATIS3);
        List<BookIndex> list = bookIndexMapper.selectMany(selectStatement);
        if (list.size() == 0) {
            return 0L;
        } else {
            return list.get(0).getId();
        }
    }

    @Override
    public BookContent queryBookContent(Long bookIndexId) {
        SelectStatementProvider selectStatement = select(BookContentDynamicSqlSupport.id,
            BookContentDynamicSqlSupport.content)
            .from(bookContent)
            .where(BookContentDynamicSqlSupport.indexId, isEqualTo(bookIndexId))
            .limit(1)
            .build()
            .render(RenderingStrategies.MYBATIS3);
        return bookContentMapper.selectMany(selectStatement).get(0);
    }

    @Override
    public List<Book> listRank(Byte type, Integer limit) {
        SortSpecification sortSpecification = visitCount.descending();
        switch (type) {
            case 1: {
                //最新入库排序
                sortSpecification = createTime.descending();
                break;
            }
            case 2: {
                //最新更新时间排序
                sortSpecification = lastIndexUpdateTime.descending();
                break;
            }
            case 3: {
                //评论数量排序
                sortSpecification = commentCount.descending();
                break;
            }
            default: {
                break;
            }
        }
        SelectStatementProvider selectStatement = select(id, catId, catName, bookName, lastIndexId, lastIndexName,
            authorId, authorName, picUrl, bookDesc, wordCount, lastIndexUpdateTime)
            .from(book)
            .where(wordCount, isGreaterThan(0))
            .and(status, isEqualTo(BOOK_STATUS_ONLINE))
            .orderBy(sortSpecification)
            .limit(limit)
            .build()
            .render(RenderingStrategies.MYBATIS3);
        return bookMapper.selectMany(selectStatement);

    }

    @Override
    public void addVisitCount(Long bookId, Integer visitCount) {
        bookMapper.addVisitCount(bookId, visitCount);
    }

    @Override
    public long queryIndexCount(Long bookId) {
        SelectStatementProvider selectStatement = select(count(BookIndexDynamicSqlSupport.id))
            .from(bookIndex)
            .where(BookIndexDynamicSqlSupport.bookId, isEqualTo(bookId))
            // 读者端章节数只统计已发布（或已到时的定时）章节
            .and(BookIndexDynamicSqlSupport.status, isEqualTo(IndexStatusUtil.STATUS_PUBLISHED), readerVisibleCriterion())
            .build()
            .render(RenderingStrategies.MYBATIS3);

        return bookIndexMapper.count(selectStatement);
    }

    @Override
    public List<Book> listRecBookByCatId(Integer catId) {
        return bookMapper.listRecBookByCatId(catId);
    }

    @Override
    public Long queryFirstBookIndexId(Long bookId) {
        SelectStatementProvider selectStatement = select(BookIndexDynamicSqlSupport.id)
            .from(bookIndex)
            .where(BookIndexDynamicSqlSupport.bookId, isEqualTo(bookId))
            .and(BookIndexDynamicSqlSupport.status, isEqualTo(IndexStatusUtil.STATUS_PUBLISHED), readerVisibleCriterion())
            .orderBy(BookIndexDynamicSqlSupport.indexNum)
            .limit(1)
            .build()
            .render(RenderingStrategies.MYBATIS3);
        return bookIndexMapper.selectMany(selectStatement).get(0).getId();
    }

    @Override
    public PageBean<BookCommentVO> listCommentByPage(Long userId, Long bookId, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        PageBean<BookCommentVO> pageBean = PageBuilder.build(bookCommentMapper.listCommentByPage(userId, bookId));
        for (BookCommentVO bookCommentVO : pageBean.getList()) {
            bookCommentVO.setLikesCount(likeService.getCommentLikesCount(bookCommentVO.getId()));
            bookCommentVO.setUnLikesCount(likeService.getCommentUnLikesCount(bookCommentVO.getId()));
        }
        return pageBean;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addBookComment(Long userId, BookComment comment) {
        //判断该用户是否已评论过该书籍
        SelectStatementProvider selectStatement = select(count(BookCommentDynamicSqlSupport.id))
            .from(bookComment)
            .where(BookCommentDynamicSqlSupport.createUserId, isEqualTo(userId))
            .and(BookCommentDynamicSqlSupport.bookId, isEqualTo(comment.getBookId()))
            .build()
            .render(RenderingStrategies.MYBATIS3);
        if (bookCommentMapper.count(selectStatement) > 0) {
            throw new BusinessException(ResponseStatus.HAS_COMMENTS);
        }
        //增加评论
        if(comment.getCommentContent() != null){
            comment.setCommentContent(SensitiveWordHelper.replace(comment.getCommentContent()));
        }
        comment.setCreateUserId(userId);
        comment.setCreateTime(new Date());
        bookCommentMapper.insertSelective(comment);
        //增加书籍评论数
        bookMapper.addCommentCount(comment.getBookId());

    }

    @Override
    public Long getOrCreateAuthorIdByName(String authorName, Byte workDirection) {
        Long authorId;
        SelectStatementProvider selectStatement = select(BookAuthorDynamicSqlSupport.id)
            .from(BookAuthorDynamicSqlSupport.bookAuthor)
            .where(BookAuthorDynamicSqlSupport.penName, isEqualTo(authorName))
            .build()
            .render(RenderingStrategies.MYBATIS3);
        List<BookAuthor> bookAuthors = bookAuthorMapper.selectMany(selectStatement);
        if (bookAuthors.size() > 0) {
            //作者存在
            authorId = bookAuthors.get(0).getId();
        } else {
            //作者不存在，先创建作者
            Date currentDate = new Date();
            authorId = idWorker.nextId();
            BookAuthor bookAuthor = new BookAuthor();
            bookAuthor.setId(authorId);
            bookAuthor.setPenName(authorName);
            bookAuthor.setWorkDirection(workDirection);
            bookAuthor.setStatus((byte) 1);
            bookAuthor.setCreateTime(currentDate);
            bookAuthor.setUpdateTime(currentDate);
            bookAuthorMapper.insertSelective(bookAuthor);


        }

        return authorId;
    }


    @Override
    public Long queryIdByNameAndAuthor(String bookName, String author) {
        //查询小说ID
        SelectStatementProvider selectStatement = select(id)
            .from(book)
            .where(BookDynamicSqlSupport.bookName, isEqualTo(bookName))
            .and(BookDynamicSqlSupport.authorName, isEqualTo(authorName))
            .build()
            .render(RenderingStrategies.MYBATIS3);
        List<Book> books = bookMapper.selectMany(selectStatement);
        if (books.size() > 0) {
            return books.get(0).getId();
        }
        return null;
    }

    @Override
    public List<Integer> queryIndexNumByBookId(Long bookId) {
        SelectStatementProvider selectStatement = select(BookIndexDynamicSqlSupport.indexNum)
            .from(BookIndexDynamicSqlSupport.bookIndex)
            .where(BookIndexDynamicSqlSupport.bookId, isEqualTo(bookId))
            .build()
            .render(RenderingStrategies.MYBATIS3);

        return bookIndexMapper.selectMany(selectStatement).stream().map(BookIndex::getIndexNum)
            .collect(Collectors.toList());
    }

    @Override
    public List<Book> queryNetworkPicBooks(String localPicPrefix, Integer limit) {
        return bookMapper.queryNetworkPicBooks(localPicPrefix, limit);
    }

    @Override
    public void updateBookPicToLocal(String picUrl, Long bookId) {

        picUrl = fileService.transFile(picUrl, picSavePath);

        bookMapper.update(update(book)
            .set(BookDynamicSqlSupport.picUrl)
            .equalTo(picUrl)
            .set(updateTime)
            .equalTo(new Date())
            .where(id, isEqualTo(bookId))
            .build()
            .render(RenderingStrategies.MYBATIS3));

    }

    @Override
    public PageBean<Book> listBookPageByUserId(Long userId, int page, int pageSize) {

        Author author = authorService.queryAuthor(userId);
        PageHelper.startPage(page, pageSize);

        SelectStatementProvider selectStatement = select(id, bookName, picUrl, catName, visitCount, yesterdayBuy,
            lastIndexUpdateTime, updateTime, wordCount, lastIndexName, bookStatus, status)
            .from(book)
            .where(authorId, isEqualTo(author.getId()))
            .orderBy(BookDynamicSqlSupport.createTime.descending())
            .build()
            .render(RenderingStrategies.MYBATIS3);
        return PageBuilder.build(bookMapper.selectMany(selectStatement));

    }

    @Override
    public void addBook(Book book, Long authorId, String penName) {
        book.setId(IdWorker.INSTANCE.nextId());
        //判断小说名是否存在
        if (queryIdByNameAndAuthor(book.getBookName(), penName) != null) {
            //该作者发布过此书名的小说
            throw new BusinessException(ResponseStatus.BOOKNAME_EXISTS);
        }
        book.setAuthorName(penName);
        book.setAuthorId(authorId);
        book.setVisitCount(0L);
        book.setWordCount(0);
        book.setScore(6.5f);
        book.setLastIndexName("");
        book.setCreateTime(new Date());
        book.setUpdateTime(book.getCreateTime());
        bookMapper.insertSelective(book);
        if (Objects.isNull(book.getPicUrl()) || !book.getPicUrl().startsWith(Constants.LOCAL_PIC_PREFIX)) {
            // 用户没有上传封面图片，AI自动生成封面图片
            threadPoolExecutor.execute(() -> {
                String prompt = String.format("生成一本小说的封面图片，图片中间显示书名《%s》，书名下方显示作者“%s 著”。",
                    book.getBookName(), book.getAuthorName());
                log.debug("prompt:{}", prompt);
                ImageResponse response = openAiImageModel.call(
                    new ImagePrompt(prompt,
                        OpenAiImageOptions.builder()
                            .quality("hd")
                            .height(800)
                            .width(600).build())
                );
                Image output = response.getResult().getOutput();
                Date currentDate = new Date();
                String picUrl = Constants.LOCAL_PIC_PREFIX +
                    "aiGen/" + DateUtils.formatDate(currentDate, "yyyy") + "/" +
                    DateUtils.formatDate(currentDate, "MM") + "/" +
                    DateUtils.formatDate(currentDate, "dd") + "/" + book.getId() + ".png";
                FileUtil.downloadFile(output.getUrl(), picSavePath + picUrl);
                bookMapper.update(update(BookDynamicSqlSupport.book)
                    .set(BookDynamicSqlSupport.picUrl)
                    .equalTo(picUrl)
                    .set(updateTime)
                    .equalTo(currentDate)
                    .where(id, isEqualTo(book.getId()))
                    .build()
                    .render(RenderingStrategies.MYBATIS3));
                cacheService.set(CacheKey.AI_GEN_PIC + book.getId(), picUrl, 60 * 60);
            });
        }
    }

    @Override
    public void updateBookStatus(Long bookId, Byte status, Long authorId) {
        bookMapper.update(update(book)
            .set(BookDynamicSqlSupport.status)
            .equalTo(status)
            .where(id, isEqualTo(bookId))
            .and(BookDynamicSqlSupport.authorId, isEqualTo(authorId))
            .build()
            .render(RenderingStrategies.MYBATIS3));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addBookContent(Long bookId, String indexName, String content, Byte isVip, Long authorId) {
        addBookContent(bookId, indexName, content, isVip, authorId, IndexStatusUtil.STATUS_PUBLISHED, null);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addBookContent(Long bookId, String indexName, String content, Byte isVip, Long authorId,
        Byte publishStatus, Date publishTime) {

        Book book = queryBookDetail(bookId);
        if (!authorId.equals(book.getAuthorId())) {
            //并不是更新自己的小说
            return;
        }
        byte status = publishStatus == null ? IndexStatusUtil.STATUS_PUBLISHED : publishStatus;
        Date currentDate = new Date();
        if (status == IndexStatusUtil.STATUS_SCHEDULED
            && (publishTime == null || !publishTime.after(currentDate))) {
            throw new BusinessException(ResponseStatus.INDEX_PUBLISH_TIME_INVALID);
        }
        Long newIndexId = idWorker.nextId();
        int wordCount = StringUtil.getStrValidWordCount(content);

        //仅立即发布的章节才计入总字数与最新章节；草稿/定时章节发布时再补计
        if (status == IndexStatusUtil.STATUS_PUBLISHED) {
            //更新小说主表信息
            bookMapper.update(update(BookDynamicSqlSupport.book)
                .set(BookDynamicSqlSupport.lastIndexId)
                .equalTo(newIndexId)
                .set(BookDynamicSqlSupport.lastIndexName)
                .equalTo(indexName)
                .set(BookDynamicSqlSupport.lastIndexUpdateTime)
                .equalTo(currentDate)
                .set(BookDynamicSqlSupport.wordCount)
                .equalTo(book.getWordCount() + wordCount)
                .where(id, isEqualTo(bookId))
                .and(BookDynamicSqlSupport.authorId, isEqualTo(authorId))
                .build()
                .render(RenderingStrategies.MYBATIS3));
        }

        //计算价格
        int bookPrice = new BigDecimal(wordCount).multiply(bookPriceConfig.getValue())
            .divide(bookPriceConfig.getWordCount(), 0, RoundingMode.DOWN).intValue();

        //目录号在全书（含草稿）范围内递增，避免重复
        int indexNum = queryMaxIndexNum(bookId) + 1;
        BookIndex newBookIndex = new BookIndex();
        newBookIndex.setId(newIndexId);
        newBookIndex.setWordCount(wordCount);
        newBookIndex.setIndexName(indexName);
        newBookIndex.setIndexNum(indexNum);
        newBookIndex.setBookId(bookId);
        newBookIndex.setIsVip(isVip);
        newBookIndex.setBookPrice(bookPrice);
        newBookIndex.setStatus(status);
        if (status == IndexStatusUtil.STATUS_SCHEDULED) {
            newBookIndex.setPublishTime(publishTime);
        } else {
            newBookIndex.setPublishTime(currentDate);
        }
        //草稿的 create_time 为存稿时间，发布时会刷新为发布时间
        newBookIndex.setCreateTime(currentDate);
        newBookIndex.setUpdateTime(currentDate);
        bookIndexMapper.insertSelective(newBookIndex);

        //更新小说内容表
        BookContent bookContent = new BookContent();
        bookContent.setIndexId(newIndexId);
        bookContent.setContent(content);
        bookContentMapper.insertSelective(bookContent);


    }

    /**
     * 查询全书最大目录号（含草稿/定时章节，保证目录号不冲突）
     */
    private int queryMaxIndexNum(Long bookId) {
        List<BookIndex> list = bookIndexMapper.selectMany(select(BookIndexDynamicSqlSupport.indexNum)
            .from(bookIndex)
            .where(BookIndexDynamicSqlSupport.bookId, isEqualTo(bookId))
            .orderBy(BookIndexDynamicSqlSupport.indexNum.descending())
            .limit(1)
            .build()
            .render(RenderingStrategies.MYBATIS3));
        return list.isEmpty() ? 0 : list.get(0).getIndexNum();
    }

    @Override
    public PageBean<BookIndex> queryAuthorIndexPage(Long bookId, Long authorId, int page, int pageSize) {
        Book book = queryBookDetail(bookId);
        if (!authorId.equals(book.getAuthorId())) {
            throw new BusinessException(ResponseStatus.BOOK_NOT_EXISTS);
        }
        PageHelper.startPage(page, pageSize);
        SelectStatementProvider selectStatement = select(BookIndexDynamicSqlSupport.id,
            BookIndexDynamicSqlSupport.bookId, BookIndexDynamicSqlSupport.indexNum,
            BookIndexDynamicSqlSupport.indexName, BookIndexDynamicSqlSupport.wordCount,
            BookIndexDynamicSqlSupport.isVip, BookIndexDynamicSqlSupport.status,
            BookIndexDynamicSqlSupport.publishTime, BookIndexDynamicSqlSupport.createTime,
            BookIndexDynamicSqlSupport.updateTime)
            .from(bookIndex)
            .where(BookIndexDynamicSqlSupport.bookId, isEqualTo(bookId))
            .orderBy(BookIndexDynamicSqlSupport.indexNum.descending())
            .build()
            .render(RenderingStrategies.MYBATIS3);
        return PageBuilder.build(bookIndexMapper.selectMany(selectStatement));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void publishIndex(Long indexId, Long authorId) {
        BookIndex chapter = null;
        List<BookIndex> chapters = bookIndexMapper.selectMany(
            select(BookIndexDynamicSqlSupport.id, BookIndexDynamicSqlSupport.bookId,
                BookIndexDynamicSqlSupport.indexNum, BookIndexDynamicSqlSupport.indexName,
                BookIndexDynamicSqlSupport.wordCount, BookIndexDynamicSqlSupport.status)
                .from(bookIndex)
                .where(BookIndexDynamicSqlSupport.id, isEqualTo(indexId))
                .build()
                .render(RenderingStrategies.MYBATIS3));
        if (chapters.size() > 0) {
            chapter = chapters.get(0);
        }
        if (chapter == null) {
            throw new BusinessException(ResponseStatus.INDEX_NOT_EXISTS);
        }
        byte currentStatus = chapter.getStatus() == null ? IndexStatusUtil.STATUS_PUBLISHED : chapter.getStatus();
        if (currentStatus == IndexStatusUtil.STATUS_PUBLISHED) {
            //已发布章节无需重复发布（幂等，定时任务扫描到刚手动发布的章节直接跳过）
            return;
        }
        Book book = queryBookDetail(chapter.getBookId());
        if (authorId != null && !authorId.equals(book.getAuthorId())) {
            throw new BusinessException(ResponseStatus.BOOK_NOT_EXISTS);
        }
        Date currentDate = new Date();
        int wordCount = chapter.getWordCount() == null ? 0 : chapter.getWordCount();

        //翻状态：发布时刻即为准，刷新 create_time/update_time，使更新趋势按发布日统计
        bookIndexMapper.update(update(bookIndex)
            .set(BookIndexDynamicSqlSupport.status)
            .equalTo(IndexStatusUtil.STATUS_PUBLISHED)
            .set(BookIndexDynamicSqlSupport.publishTime)
            .equalTo(currentDate)
            .set(BookIndexDynamicSqlSupport.createTime)
            .equalTo(currentDate)
            .set(BookIndexDynamicSqlSupport.updateTime)
            .equalTo(currentDate)
            .where(BookIndexDynamicSqlSupport.id, isEqualTo(indexId))
            .build()
            .render(RenderingStrategies.MYBATIS3));

        //补计总字数
        bookMapper.update(update(BookDynamicSqlSupport.book)
            .set(BookDynamicSqlSupport.wordCount)
            .equalTo(book.getWordCount() + wordCount)
            .where(id, isEqualTo(book.getId()))
            .build()
            .render(RenderingStrategies.MYBATIS3));

        //若发布后本书已发布章节中该章目录号最大，刷新最新章节信息
        List<BookIndex> lastChapters = bookIndexMapper.selectMany(
            select(BookIndexDynamicSqlSupport.id, BookIndexDynamicSqlSupport.indexName,
                BookIndexDynamicSqlSupport.createTime)
                .from(bookIndex)
                .where(BookIndexDynamicSqlSupport.bookId, isEqualTo(book.getId()))
                .and(BookIndexDynamicSqlSupport.status, isEqualTo(IndexStatusUtil.STATUS_PUBLISHED))
                .orderBy(BookIndexDynamicSqlSupport.indexNum.descending())
                .limit(1)
                .build()
                .render(RenderingStrategies.MYBATIS3));
        if (lastChapters.size() > 0 && lastChapters.get(0).getId().equals(indexId)) {
            BookIndex lastChapter = lastChapters.get(0);
            bookMapper.update(update(BookDynamicSqlSupport.book)
                .set(BookDynamicSqlSupport.lastIndexId)
                .equalTo(indexId)
                .set(BookDynamicSqlSupport.lastIndexName)
                .equalTo(lastChapter.getIndexName())
                .set(BookDynamicSqlSupport.lastIndexUpdateTime)
                .equalTo(currentDate)
                .where(id, isEqualTo(book.getId()))
                .build()
                .render(RenderingStrategies.MYBATIS3));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void scheduleIndex(Long indexId, Long authorId, Date publishTime) {
        if (publishTime == null || !publishTime.after(new Date())) {
            throw new BusinessException(ResponseStatus.INDEX_PUBLISH_TIME_INVALID);
        }
        List<BookIndex> chapters = bookIndexMapper.selectMany(
            select(BookIndexDynamicSqlSupport.id, BookIndexDynamicSqlSupport.bookId,
                BookIndexDynamicSqlSupport.status)
                .from(bookIndex)
                .where(BookIndexDynamicSqlSupport.id, isEqualTo(indexId))
                .build()
                .render(RenderingStrategies.MYBATIS3));
        if (chapters.size() == 0) {
            throw new BusinessException(ResponseStatus.INDEX_NOT_EXISTS);
        }
        BookIndex chapter = chapters.get(0);
        Book book = queryBookDetail(chapter.getBookId());
        if (!authorId.equals(book.getAuthorId())) {
            throw new BusinessException(ResponseStatus.BOOK_NOT_EXISTS);
        }
        byte currentStatus = chapter.getStatus() == null ? IndexStatusUtil.STATUS_PUBLISHED : chapter.getStatus();
        if (currentStatus == IndexStatusUtil.STATUS_PUBLISHED) {
            //已发布章节不支持撤回为定时发布
            throw new BusinessException(ResponseStatus.INDEX_ALREADY_PUBLISHED);
        }
        bookIndexMapper.update(update(bookIndex)
            .set(BookIndexDynamicSqlSupport.status)
            .equalTo(IndexStatusUtil.STATUS_SCHEDULED)
            .set(BookIndexDynamicSqlSupport.publishTime)
            .equalTo(publishTime)
            .set(BookIndexDynamicSqlSupport.updateTime)
            .equalTo(new Date())
            .where(BookIndexDynamicSqlSupport.id, isEqualTo(indexId))
            .build()
            .render(RenderingStrategies.MYBATIS3));
    }

    @Override
    public List<Long> queryDueScheduledIndexIds(Date now, int limit) {
        SelectStatementProvider selectStatement = select(BookIndexDynamicSqlSupport.id)
            .from(bookIndex)
            .where(BookIndexDynamicSqlSupport.status, isEqualTo(IndexStatusUtil.STATUS_SCHEDULED))
            .and(BookIndexDynamicSqlSupport.publishTime, isLessThanOrEqualTo(now))
            .orderBy(BookIndexDynamicSqlSupport.publishTime)
            .limit(limit)
            .build()
            .render(RenderingStrategies.MYBATIS3);
        return bookIndexMapper.selectMany(selectStatement).stream()
            .map(BookIndex::getId)
            .collect(Collectors.toList());
    }


    @Override
    public List<Book> queryBookByUpdateTimeByPage(Date startDate, int limit) {

        return bookMapper.selectMany(select(book.allColumns())
            .from(book)
            .where(updateTime, isGreaterThan(startDate))
            .and(lastIndexId, isNotNull())
            .and(status, isEqualTo(BOOK_STATUS_ONLINE))
            .orderBy(updateTime)
            .limit(limit)
            .build()
            .render(RenderingStrategies.MYBATIS3));
    }

    @Override
    public List<Book> queryBookList(Long authorId) {

        return bookMapper.selectMany(select(id, bookName)
            .from(book)
            .where(BookDynamicSqlSupport.authorId, isEqualTo(authorId))
            .build()
            .render(RenderingStrategies.MYBATIS3));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteIndex(Long indexId, Long authorId) {

        //查询小说章节表信息
        List<BookIndex> bookIndices = bookIndexMapper.selectMany(
            select(BookIndexDynamicSqlSupport.bookId, BookIndexDynamicSqlSupport.wordCount,
                BookIndexDynamicSqlSupport.status)
                .from(bookIndex)
                .where(BookIndexDynamicSqlSupport.id, isEqualTo(indexId)).build().render(RenderingStrategies.MYBATIS3));
        if (bookIndices.size() > 0) {
            BookIndex bookIndex = bookIndices.get(0);
            //获取小说ID
            Long bookId = bookIndex.getBookId();
            //查询小说表信息
            List<Book> books = bookMapper.selectMany(
                select(wordCount, BookDynamicSqlSupport.authorId)
                    .from(book)
                    .where(id, isEqualTo(bookId))
                    .build()
                    .render(RenderingStrategies.MYBATIS3));
            if (books.size() > 0) {
                Book book = books.get(0);
                int wordCount = book.getWordCount();
                //作者ID相同，表明该小说是登录用户发布，可以删除
                if (book.getAuthorId().equals(authorId)) {
                    //删除目录表和内容表记录
                    bookIndexMapper.deleteByPrimaryKey(indexId);
                    bookContentMapper.delete(
                        deleteFrom(bookContent).where(BookContentDynamicSqlSupport.indexId, isEqualTo(indexId)).build()
                            .render(RenderingStrategies.MYBATIS3));
                    //仅已发布章节计入过总字数，草稿/定时章节删除时不扣减
                    byte chapterStatus = bookIndex.getStatus() == null
                        ? IndexStatusUtil.STATUS_PUBLISHED : bookIndex.getStatus();
                    if (chapterStatus == IndexStatusUtil.STATUS_PUBLISHED) {
                        wordCount = wordCount - bookIndex.getWordCount();
                    }
                    //更新最新章节（只在已发布章节中取末章，草稿不参与）
                    Long lastIndexId = null;
                    String lastIndexName = null;
                    Date lastIndexUpdateTime = null;
                    List<BookIndex> lastBookIndices = bookIndexMapper.selectMany(
                        select(BookIndexDynamicSqlSupport.id, BookIndexDynamicSqlSupport.indexName,
                            BookIndexDynamicSqlSupport.createTime)
                            .from(BookIndexDynamicSqlSupport.bookIndex)
                            .where(BookIndexDynamicSqlSupport.bookId, isEqualTo(bookId))
                            .and(BookIndexDynamicSqlSupport.status, isEqualTo(IndexStatusUtil.STATUS_PUBLISHED))
                            .orderBy(BookIndexDynamicSqlSupport.indexNum.descending())
                            .limit(1)
                            .build()
                            .render(RenderingStrategies.MYBATIS3));
                    if (lastBookIndices.size() > 0) {
                        BookIndex lastBookIndex = lastBookIndices.get(0);
                        lastIndexId = lastBookIndex.getId();
                        lastIndexName = lastBookIndex.getIndexName();
                        lastIndexUpdateTime = lastBookIndex.getCreateTime();

                    }
                    //更新小说主表信息
                    bookMapper.update(update(BookDynamicSqlSupport.book)
                        .set(BookDynamicSqlSupport.wordCount)
                        .equalTo(wordCount)
                        .set(updateTime)
                        .equalTo(new Date())
                        .set(BookDynamicSqlSupport.lastIndexId)
                        .equalTo(lastIndexId)
                        .set(BookDynamicSqlSupport.lastIndexName)
                        .equalTo(lastIndexName)
                        .set(BookDynamicSqlSupport.lastIndexUpdateTime)
                        .equalTo(lastIndexUpdateTime)
                        .where(id, isEqualTo(bookId))
                        .build()
                        .render(RenderingStrategies.MYBATIS3));


                }
            }


        }


    }

    @Override
    public void updateIndexName(Long indexId, String indexName, Long authorId) {
        //查询小说章节表信息
        List<BookIndex> bookIndices = bookIndexMapper.selectMany(
            select(BookIndexDynamicSqlSupport.bookId, BookIndexDynamicSqlSupport.wordCount)
                .from(bookIndex)
                .where(BookIndexDynamicSqlSupport.id, isEqualTo(indexId)).build().render(RenderingStrategies.MYBATIS3));
        if (bookIndices.size() > 0) {
            BookIndex bookIndex = bookIndices.get(0);
            //获取小说ID
            Long bookId = bookIndex.getBookId();
            //查询小说表信息
            List<Book> books = bookMapper.selectMany(
                select(wordCount, BookDynamicSqlSupport.authorId)
                    .from(book)
                    .where(id, isEqualTo(bookId))
                    .build()
                    .render(RenderingStrategies.MYBATIS3));
            if (books.size() > 0) {
                Book book = books.get(0);
                //作者ID相同，表明该小说是登录用户发布，可以修改
                if (book.getAuthorId().equals(authorId)) {

                    bookIndexMapper.update(
                        update(BookIndexDynamicSqlSupport.bookIndex)
                            .set(BookIndexDynamicSqlSupport.indexName)
                            .equalTo(indexName)
                            .set(BookIndexDynamicSqlSupport.updateTime)
                            .equalTo(new Date())
                            .where(BookIndexDynamicSqlSupport.id, isEqualTo(indexId))
                            .build()
                            .render(RenderingStrategies.MYBATIS3));


                }
            }


        }
    }

    @Override
    public String queryIndexContent(Long indexId, Long authorId) {
        //查询小说章节表信息
        List<BookIndex> bookIndices = bookIndexMapper.selectMany(
            select(BookIndexDynamicSqlSupport.bookId, BookIndexDynamicSqlSupport.wordCount)
                .from(bookIndex)
                .where(BookIndexDynamicSqlSupport.id, isEqualTo(indexId)).build().render(RenderingStrategies.MYBATIS3));
        if (bookIndices.size() > 0) {
            BookIndex bookIndex = bookIndices.get(0);
            //获取小说ID
            Long bookId = bookIndex.getBookId();
            //查询小说表信息
            List<Book> books = bookMapper.selectMany(
                select(wordCount, BookDynamicSqlSupport.authorId)
                    .from(book)
                    .where(id, isEqualTo(bookId))
                    .build()
                    .render(RenderingStrategies.MYBATIS3));
            if (books.size() > 0) {
                Book book = books.get(0);
                //作者ID相同，表明该小说是登录用户发布
                if (book.getAuthorId().equals(authorId)) {
                    return bookContentMapper.selectMany(
                            select(content)
                                .from(bookContent)
                                .where(BookContentDynamicSqlSupport.indexId, isEqualTo(indexId))
                                .limit(1)
                                .build().render(RenderingStrategies.MYBATIS3))
                        .get(0).getContent();
                }

            }
        }
        return "";
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateBookContent(Long indexId, String indexName, String content, Long authorId) {

        //查询小说章节表信息（status 用于判断是否需重算小说总字数）
        List<BookIndex> bookIndices = bookIndexMapper.selectMany(
            select(BookIndexDynamicSqlSupport.bookId, BookIndexDynamicSqlSupport.wordCount,
                BookIndexDynamicSqlSupport.status)
                .from(bookIndex)
                .where(BookIndexDynamicSqlSupport.id, isEqualTo(indexId)).build().render(RenderingStrategies.MYBATIS3));
        if (bookIndices.size() > 0) {
            BookIndex bookIndex = bookIndices.get(0);
            //获取小说ID
            Long bookId = bookIndex.getBookId();
            //查询小说表信息（lastIndexId 用于判断修改的是否为最新章节）
            List<Book> books = bookMapper.selectMany(
                select(wordCount, BookDynamicSqlSupport.authorId, BookDynamicSqlSupport.lastIndexId)
                    .from(book)
                    .where(id, isEqualTo(bookId))
                    .build()
                    .render(RenderingStrategies.MYBATIS3));
            if (books.size() > 0) {
                Book book = books.get(0);
                //作者ID相同，表明该小说是登录用户发布，可以修改
                if (book.getAuthorId().equals(authorId)) {
                    Date currentDate = new Date();
                    int wordCount = StringUtil.getStrValidWordCount(content);

                    //计算价格
                    int bookPrice = new BigDecimal(wordCount).multiply(bookPriceConfig.getValue())
                        .divide(bookPriceConfig.getWordCount(), 0, RoundingMode.DOWN).intValue();

                    //更新小说目录表
                    bookIndexMapper.update(
                        update(BookIndexDynamicSqlSupport.bookIndex)
                            .set(BookIndexDynamicSqlSupport.indexName)
                            .equalTo(indexName)
                            .set(BookIndexDynamicSqlSupport.wordCount)
                            .equalTo(wordCount)
                            .set(BookIndexDynamicSqlSupport.bookPrice)
                            .equalTo(bookPrice)
                            .set(BookIndexDynamicSqlSupport.updateTime)
                            .equalTo(currentDate)
                            .where(BookIndexDynamicSqlSupport.id, isEqualTo(indexId))
                            .build().render(RenderingStrategies.MYBATIS3));

                    //更新小说内容表
                    bookContentMapper.update(
                        update(BookContentDynamicSqlSupport.bookContent)
                            .set(BookContentDynamicSqlSupport.content)
                            .equalTo(content)
                            .where(BookContentDynamicSqlSupport.indexId, isEqualTo(indexId))
                            .build().render(RenderingStrategies.MYBATIS3));

                    //已发布章节改正文后字数/价格会变，需按全书已发布章节重算小说总字数（草稿/定时不计入总数，全量重算可顺带修复历史偏差）。
                    byte indexStatus = bookIndex.getStatus() == null
                        ? IndexStatusUtil.STATUS_PUBLISHED : bookIndex.getStatus();
                    if (indexStatus == IndexStatusUtil.STATUS_PUBLISHED) {
                        Long publishedTotal = bookIndexMapper.selectStatistic(
                            select(sum(BookIndexDynamicSqlSupport.wordCount))
                                .from(BookIndexDynamicSqlSupport.bookIndex)
                                .where(BookIndexDynamicSqlSupport.bookId, isEqualTo(bookId))
                                .and(BookIndexDynamicSqlSupport.status, isEqualTo(IndexStatusUtil.STATUS_PUBLISHED))
                                .build().render(RenderingStrategies.MYBATIS3));
                        int totalWordCount = publishedTotal == null ? 0 : publishedTotal.intValue();

                        if (indexId.equals(book.getLastIndexId())) {
                            //改的正是最新章节，同步书名与最新更新时刻（局部变量 book 遮蔽静态导入的表，需显式限定）
                            bookMapper.update(update(BookDynamicSqlSupport.book)
                                .set(BookDynamicSqlSupport.wordCount)
                                .equalTo(totalWordCount)
                                .set(BookDynamicSqlSupport.lastIndexName)
                                .equalTo(indexName)
                                .set(BookDynamicSqlSupport.lastIndexUpdateTime)
                                .equalTo(currentDate)
                                .where(id, isEqualTo(bookId))
                                .build().render(RenderingStrategies.MYBATIS3));
                        } else {
                            bookMapper.update(update(BookDynamicSqlSupport.book)
                                .set(BookDynamicSqlSupport.wordCount)
                                .equalTo(totalWordCount)
                                .where(id, isEqualTo(bookId))
                                .build().render(RenderingStrategies.MYBATIS3));
                        }
                    }

                }
            }

        }
    }

    @Override
    public void updateBookPic(Long bookId, String bookPic, Long authorId) {
        bookMapper.update(update(book)
            .set(picUrl)
            .equalTo(bookPic)
            .set(updateTime)
            .equalTo(new Date())
            .where(id, isEqualTo(bookId))
            .and(BookDynamicSqlSupport.authorId, isEqualTo(authorId))
            .build()
            .render(RenderingStrategies.MYBATIS3));
    }

    @Override
    public String queryAiGenPic(Long bookId) {
        return cacheService.get(CacheKey.AI_GEN_PIC + bookId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addBookCommentReply(Long userId, BookCommentReply commentReply) {
        //增加回复
        if(commentReply.getReplyContent() != null){
            commentReply.setReplyContent(SensitiveWordHelper.replace(commentReply.getReplyContent()));
        }
        commentReply.setCreateUserId(userId);
        commentReply.setCreateTime(new Date());
        bookCommentReplyMapper.insertSelective(commentReply);
        //增加评论回复数
        bookCommentMapper.addReplyCount(commentReply.getCommentId());
        //书评被回复消息通知（回复者自己除外）
        BookComment bookComment = getBookComment(commentReply.getCommentId());
        if (bookComment != null) {
            userMessageService.sendReplyMessage(userId, bookComment.getCreateUserId(),
                UserMessage.TYPE_BOOK_COMMENT_REPLY, bookComment.getBookId(), null,
                bookComment.getId(), commentReply.getReplyContent());
        }
    }

    @Override
    public PageBean<BookCommentReplyVO> listCommentReplyByPage(Long userId, Long commentId, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        PageBean<BookCommentReplyVO> pageBean = PageBuilder.build(
            bookCommentReplyMapper.listCommentReplyByPage(userId, commentId));
        pageBean.getList().forEach(commentReply -> {
            commentReply.setLikesCount(likeService.getReplyLikesCount(commentReply.getId()));
            commentReply.setUnLikesCount(likeService.getReplyUnLikesCount(commentReply.getId()));
        });
        return pageBean;
    }

    @Override
    public BookComment getBookComment(Long commentId) {
        return bookCommentMapper.selectByPrimaryKey(commentId).orElse(null);
    }

    @Override
    public void finishBook(Long bookId, Long authorId) {
        bookMapper.update(update(book)
            .set(bookStatus)
            .equalTo((byte) 1)
            .set(updateTime)
            .equalTo(new Date())
            .where(id, isEqualTo(bookId))
            .and(BookDynamicSqlSupport.authorId, isEqualTo(authorId))
            .build()
            .render(RenderingStrategies.MYBATIS3));
    }


}
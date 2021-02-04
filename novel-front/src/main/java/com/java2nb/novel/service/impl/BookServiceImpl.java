package com.java2nb.novel.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.java2nb.novel.core.bean.PageBean;
import com.java2nb.novel.core.cache.CacheKey;
import com.java2nb.novel.core.cache.CacheService;
import com.java2nb.novel.core.config.BookPriceProperties;
import com.java2nb.novel.core.enums.ResponseStatus;
import com.java2nb.novel.core.exception.BusinessException;
import com.java2nb.novel.core.utils.*;
import com.java2nb.novel.entity.*;
import com.java2nb.novel.entity.Book;
import com.java2nb.novel.mapper.*;
import com.java2nb.novel.vo.BookSpVO;
import com.java2nb.novel.service.AuthorService;
import com.java2nb.novel.service.BookService;
import com.java2nb.novel.service.FileService;
import com.java2nb.novel.service.SearchService;
import com.java2nb.novel.vo.BookCommentVO;
import com.java2nb.novel.vo.BookSettingVO;
import com.java2nb.novel.vo.BookVO;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.dynamic.sql.SortSpecification;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.mybatis.dynamic.sql.render.RenderingStrategy;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.orderbyhelper.OrderByHelper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.java2nb.novel.mapper.BookCategoryDynamicSqlSupport.bookCategory;
import static com.java2nb.novel.mapper.BookCommentDynamicSqlSupport.bookComment;
import static com.java2nb.novel.mapper.BookContentDynamicSqlSupport.bookContent;
import static com.java2nb.novel.mapper.BookContentDynamicSqlSupport.content;
import static com.java2nb.novel.mapper.BookDynamicSqlSupport.*;
import static com.java2nb.novel.mapper.BookIndexDynamicSqlSupport.bookIndex;
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

    @Value("${spring.elasticsearch.enable}")
    private Integer esEnable;

    private final FrontBookSettingMapper bookSettingMapper;

    private final FrontBookMapper bookMapper;

    private final BookCategoryMapper bookCategoryMapper;

    private final BookIndexMapper bookIndexMapper;

    private final BookContentMapper bookContentMapper;

    private final FrontBookCommentMapper bookCommentMapper;

    private final BookAuthorMapper bookAuthorMapper;

    private final CacheService cacheService;

    private final AuthorService authorService;

    private final SearchService searchService;

    private final FileService fileService;

    private final BookPriceProperties bookPriceConfig;


    @SneakyThrows
    @Override
    public Map<Byte, List<BookSettingVO>> listBookSettingVO() {
        String result = cacheService.get(CacheKey.INDEX_BOOK_SETTINGS_KEY);
        if (result == null || result.length() < Constants.OBJECT_JSON_CACHE_EXIST_LENGTH) {
            List<BookSettingVO> list = bookSettingMapper.listVO();
            if (list.size() == 0) {
                //如果首页小说没有被设置，则初始化首页小说设置
                list = initIndexBookSetting();
            }
            result = new ObjectMapper().writeValueAsString(list.stream().collect(Collectors.groupingBy(BookSettingVO::getType)));
            cacheService.set(CacheKey.INDEX_BOOK_SETTINGS_KEY, result);
        }
        return new ObjectMapper().readValue(result, Map.class);
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
                } else if (i < 20) {
                    type = 2;
                } else if (i < 26) {
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

            bookSettingMapper.insertMultiple(bookSettingList);

            return bookSettingVOList;
        }
        return new ArrayList<>(0);
    }


    @Override
    public List<Book> listClickRank() {
        List<Book> result = (List<Book>) cacheService.getObject(CacheKey.INDEX_CLICK_BANK_BOOK_KEY);
        if (result == null || result.size() == 0) {
            result = listRank((byte) 0, 10);
            cacheService.setObject(CacheKey.INDEX_CLICK_BANK_BOOK_KEY, result, 5000);
        }
        return result;
    }

    @Override
    public List<Book> listNewRank() {
        List<Book> result = (List<Book>) cacheService.getObject(CacheKey.INDEX_NEW_BOOK_KEY);
        if (result == null || result.size() == 0) {
            result = listRank((byte) 1, 10);
            cacheService.setObject(CacheKey.INDEX_NEW_BOOK_KEY, result, 3600);
        }
        return result;
    }

    @Override
    public List<BookVO> listUpdateRank() {
        List<BookVO> result = (List<BookVO>) cacheService.getObject(CacheKey.INDEX_UPDATE_BOOK_KEY);
        if (result == null || result.size() == 0) {
            List<Book> bookPOList = listRank((byte) 2, 23);
            result = BeanUtil.copyList(bookPOList, BookVO.class);
            cacheService.setObject(CacheKey.INDEX_UPDATE_BOOK_KEY, result, 60 * 10);
        }
        return result;
    }

    @Override
    public PageBean searchByPage(BookSpVO params, int page, int pageSize) {


        if (params.getUpdatePeriod() != null) {
            long cur = System.currentTimeMillis();
            long period = params.getUpdatePeriod() * 24 * 3600 * 1000;
            long time = cur - period;
            params.setUpdateTimeMin(new Date(time));
        }

        if (esEnable == 1) {


            try {
                return searchService.searchBook(params,page,pageSize);

            }catch (Exception e){
                log.error(e.getMessage(),e);
            }


        }
        PageHelper.startPage(page, pageSize);

        if (StringUtils.isNotBlank(params.getSort())) {
            OrderByHelper.orderBy(params.getSort() + " desc");
        }
        return new PageBean(bookMapper.searchByPage(params));


    }

    @Override
    public List<BookCategory> listBookCategory() {
        SelectStatementProvider selectStatementProvider = select(BookCategoryDynamicSqlSupport.id, BookCategoryDynamicSqlSupport.name, BookCategoryDynamicSqlSupport.workDirection)
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
    public List<BookIndex> queryIndexList(Long bookId, String orderBy, Integer page, Integer pageSize) {
        if (StringUtils.isNotBlank(orderBy)) {
            OrderByHelper.orderBy(orderBy);
        }
        if (page != null && pageSize != null) {
            PageHelper.startPage(page, pageSize);
        }

        SelectStatementProvider selectStatement = select(BookIndexDynamicSqlSupport.id, BookIndexDynamicSqlSupport.bookId, BookIndexDynamicSqlSupport.indexNum, BookIndexDynamicSqlSupport.indexName, BookIndexDynamicSqlSupport.updateTime, BookIndexDynamicSqlSupport.isVip)
                .from(bookIndex)
                .where(BookIndexDynamicSqlSupport.bookId, isEqualTo(bookId))
                .build()
                .render(RenderingStrategies.MYBATIS3);

        return bookIndexMapper.selectMany(selectStatement);
    }


    @Override
    public BookIndex queryBookIndex(Long bookIndexId) {
        SelectStatementProvider selectStatement = select(BookIndexDynamicSqlSupport.id, BookIndexDynamicSqlSupport.bookId, BookIndexDynamicSqlSupport.indexNum, BookIndexDynamicSqlSupport.indexName, BookIndexDynamicSqlSupport.wordCount,BookIndexDynamicSqlSupport.bookPrice, BookIndexDynamicSqlSupport.updateTime, BookIndexDynamicSqlSupport.isVip)
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
        SelectStatementProvider selectStatement = select(BookContentDynamicSqlSupport.id, BookContentDynamicSqlSupport.content)
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
        SelectStatementProvider selectStatement = select(id, catId, catName, bookName, lastIndexId, lastIndexName, authorId, authorName, picUrl, bookDesc, wordCount, lastIndexUpdateTime)
                .from(book)
                .where(wordCount, isGreaterThan(0))
                .orderBy(sortSpecification)
                .limit(limit)
                .build()
                .render(RenderingStrategies.MYBATIS3);
        return bookMapper.selectMany(selectStatement);

    }

    @Override
    public void addVisitCount(Long bookId, Integer visitCount) {
        bookMapper.addVisitCount(bookId,visitCount);
    }

    @Override
    public long queryIndexCount(Long bookId) {
        SelectStatementProvider selectStatement = select(count(BookIndexDynamicSqlSupport.id))
                .from(bookIndex)
                .where(BookIndexDynamicSqlSupport.bookId, isEqualTo(bookId))
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
                .orderBy(BookIndexDynamicSqlSupport.indexNum)
                .limit(1)
                .build()
                .render(RenderingStrategies.MYBATIS3);
        return bookIndexMapper.selectMany(selectStatement).get(0).getId();
    }

    @Override
    public PageBean<BookCommentVO> listCommentByPage(Long userId, Long bookId, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        OrderByHelper.orderBy("t1.create_time desc");
        return new PageBean<>(bookCommentMapper.listCommentByPage(userId, bookId));
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
            authorId = new IdWorker().nextId();
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

        return bookIndexMapper.selectMany(selectStatement).stream().map(BookIndex::getIndexNum).collect(Collectors.toList());
    }

    @Override
    public List<Book> queryNetworkPicBooks(String localPicPrefix, Integer limit) {
        return bookMapper.queryNetworkPicBooks(localPicPrefix,limit);
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

        PageHelper.startPage(page, pageSize);

        SelectStatementProvider selectStatement = select(id, bookName, picUrl, catName, visitCount, yesterdayBuy, lastIndexUpdateTime, updateTime, wordCount, lastIndexName, status)
                .from(book)
                .where(authorId, isEqualTo(authorService.queryAuthor(userId).getId()))
                .orderBy(BookDynamicSqlSupport.createTime.descending())
                .build()
                .render(RenderingStrategies.MYBATIS3);
        return new PageBean<>(bookMapper.selectMany(selectStatement));

    }

    @Override
    public void addBook(Book book, Long authorId, String penName) {
        //判断小说名是否存在
        if (queryIdByNameAndAuthor(book.getBookName(), penName) != null) {
            //该作者发布过此书名的小说
            throw new BusinessException(ResponseStatus.BOOKNAME_EXISTS);
        }
        ;
        book.setAuthorName(penName);
        book.setAuthorId(authorId);
        book.setVisitCount(0L);
        book.setWordCount(0);
        book.setScore(6.5f);
        book.setLastIndexName("");
        book.setCreateTime(new Date());
        book.setUpdateTime(book.getCreateTime());
        bookMapper.insertSelective(book);

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

        Book book = queryBookDetail(bookId);
        if (!authorId.equals(book.getAuthorId())) {
            //并不是更新自己的小说
            return;
        }
        Long lastIndexId = new IdWorker().nextId();
        Date currentDate = new Date();
        int wordCount = StringUtil.getStrValidWordCount(content);

        //更新小说主表信息
        bookMapper.update(update(BookDynamicSqlSupport.book)
                .set(BookDynamicSqlSupport.lastIndexId)
                .equalTo(lastIndexId)
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

        //计算价格
        int bookPrice = new BigDecimal(wordCount).divide(bookPriceConfig.getWordCount()).multiply(bookPriceConfig.getValue()).intValue();

        //更新小说目录表
        int indexNum = 0;
        if (book.getLastIndexId() != null) {
            indexNum = queryBookIndex(book.getLastIndexId()).getIndexNum() + 1;
        }
        BookIndex lastBookIndex = new BookIndex();
        lastBookIndex.setId(lastIndexId);
        lastBookIndex.setWordCount(wordCount);
        lastBookIndex.setIndexName(indexName);
        lastBookIndex.setIndexNum(indexNum);
        lastBookIndex.setBookId(bookId);
        lastBookIndex.setIsVip(isVip);
        lastBookIndex.setBookPrice(bookPrice);
        lastBookIndex.setCreateTime(currentDate);
        lastBookIndex.setUpdateTime(currentDate);
        bookIndexMapper.insertSelective(lastBookIndex);

        //更新小说内容表
        BookContent bookContent = new BookContent();
        bookContent.setIndexId(lastIndexId);
        bookContent.setContent(content);
        bookContentMapper.insertSelective(bookContent);


    }

    @Override
    public List<Book> queryBookByUpdateTimeByPage(Date startDate,  int limit) {


        return bookMapper.selectMany(select(book.allColumns())
                .from(book)
                .where(updateTime, isGreaterThan(startDate))
                .and(lastIndexId,isNotNull())
                .orderBy(updateTime)
                .limit(limit)
                .build()
                .render(RenderingStrategies.MYBATIS3));
    }

    @Override
    public List<Book> queryBookList(Long authorId) {

        return bookMapper.selectMany(select(id, bookName)
                .from(book)
                .where(BookDynamicSqlSupport.authorId,isEqualTo(authorId))
                .build()
                .render(RenderingStrategies.MYBATIS3));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteIndex(Long indexId, Long authorId) {

        //查询小说章节表信息
        List<BookIndex> bookIndices = bookIndexMapper.selectMany(
                select(BookIndexDynamicSqlSupport.bookId, BookIndexDynamicSqlSupport.wordCount)
                        .from(bookIndex)
                        .where(BookIndexDynamicSqlSupport.id, isEqualTo(indexId)).build().render(RenderingStrategy.MYBATIS3));
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
                            .render(RenderingStrategy.MYBATIS3));
            if (books.size() > 0) {
                Book book = books.get(0);
                int wordCount = book.getWordCount();
                //作者ID相同，表明该小说是登录用户发布，可以删除
                if (book.getAuthorId().equals(authorId)) {
                    //删除目录表和内容表记录
                    bookIndexMapper.deleteByPrimaryKey(indexId);
                    bookContentMapper.delete(deleteFrom(bookContent).where(BookContentDynamicSqlSupport.indexId, isEqualTo(indexId)).build()
                            .render(RenderingStrategies.MYBATIS3));
                    //更新总字数
                    wordCount = wordCount - bookIndex.getWordCount();
                    //更新最新章节
                    Long lastIndexId = null;
                    String lastIndexName = null;
                    Date lastIndexUpdateTime = null;
                    List<BookIndex> lastBookIndices = bookIndexMapper.selectMany(
                            select(BookIndexDynamicSqlSupport.id, BookIndexDynamicSqlSupport.indexName, BookIndexDynamicSqlSupport.createTime)
                                    .from(BookIndexDynamicSqlSupport.bookIndex)
                                    .where(BookIndexDynamicSqlSupport.bookId, isEqualTo(bookId))
                                    .orderBy(BookIndexDynamicSqlSupport.indexNum.descending())
                                    .limit(1)
                                    .build()
                                    .render(RenderingStrategy.MYBATIS3));
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
                        .where(BookIndexDynamicSqlSupport.id, isEqualTo(indexId)).build().render(RenderingStrategy.MYBATIS3));
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
                            .render(RenderingStrategy.MYBATIS3));
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
                                    .render(RenderingStrategy.MYBATIS3));


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
                        .where(BookIndexDynamicSqlSupport.id, isEqualTo(indexId)).build().render(RenderingStrategy.MYBATIS3));
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
                            .render(RenderingStrategy.MYBATIS3));
            if (books.size() > 0) {
                Book book = books.get(0);
                //作者ID相同，表明该小说是登录用户发布
                if (book.getAuthorId().equals(authorId)) {
                    return bookContentMapper.selectMany(
                            select(content)
                                    .from(bookContent)
                                    .where(BookContentDynamicSqlSupport.indexId, isEqualTo(indexId))
                                    .limit(1)
                                    .build().render(RenderingStrategy.MYBATIS3))
                            .get(0).getContent();
                }

            }
        }
        return "";
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateBookContent(Long indexId, String indexName, String content, Long authorId) {

        //查询小说章节表信息
        List<BookIndex> bookIndices = bookIndexMapper.selectMany(
                select(BookIndexDynamicSqlSupport.bookId, BookIndexDynamicSqlSupport.wordCount)
                        .from(bookIndex)
                        .where(BookIndexDynamicSqlSupport.id, isEqualTo(indexId)).build().render(RenderingStrategy.MYBATIS3));
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
                            .render(RenderingStrategy.MYBATIS3));
            if (books.size() > 0) {
                Book book = books.get(0);
                //作者ID相同，表明该小说是登录用户发布，可以修改
                if (book.getAuthorId().equals(authorId)) {
                    Date currentDate = new Date();
                    int wordCount = StringUtil.getStrValidWordCount(content);

                    //计算价格
                    int bookPrice = new BigDecimal(wordCount).divide(bookPriceConfig.getWordCount()).multiply(bookPriceConfig.getValue()).intValue();


                    //更新小说目录表
                    int update = bookIndexMapper.update(
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
                                    .build().render(RenderingStrategy.MYBATIS3));

                    //更新小说内容表
                    bookContentMapper.update(
                            update(BookContentDynamicSqlSupport.bookContent)
                                    .set(BookContentDynamicSqlSupport.content)
                                    .equalTo(content)
                                    .where(BookContentDynamicSqlSupport.indexId, isEqualTo(indexId))
                                    .build().render(RenderingStrategy.MYBATIS3));

                }
            }

        }
    }


}

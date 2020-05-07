package com.java2nb.novel.service.impl;

import com.java2nb.novel.core.utils.IdWorker;
import com.java2nb.novel.entity.Book;
import com.java2nb.novel.entity.BookContent;
import com.java2nb.novel.entity.BookIndex;
import com.java2nb.novel.mapper.*;
import com.java2nb.novel.service.BookService;
import com.java2nb.novel.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.java2nb.novel.mapper.BookDynamicSqlSupport.crawlBookId;
import static com.java2nb.novel.mapper.BookDynamicSqlSupport.crawlSourceId;
import static com.java2nb.novel.mapper.CrawlSourceDynamicSqlSupport.id;
import static org.mybatis.dynamic.sql.SqlBuilder.*;
import static org.mybatis.dynamic.sql.select.SelectDSL.select;

/**
 * @author Administrator
 */
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final CrawlBookMapper bookMapper;

    private final BookCategoryMapper bookCategoryMapper;

    private final CrawlBookIndexMapper bookIndexMapper;

    private final BookContentMapper bookContentMapper;


    @Override
    public boolean queryIsExistByBookNameAndAuthorName(String bookName, String authorName) {

        return bookMapper.count(countFrom(BookDynamicSqlSupport.book).where(BookDynamicSqlSupport.bookName, isEqualTo(bookName))
                .and(BookDynamicSqlSupport.authorName, isEqualTo(authorName))
                .build()
                .render(RenderingStrategies.MYBATIS3))>0;

    }

    @Override
    public void updateCrawlProperties(Long id, Integer sourceId, String bookId) {
        bookMapper.update(update(BookDynamicSqlSupport.book)
                .set(crawlSourceId)
                .equalTo(sourceId)
                .set(crawlBookId)
                .equalTo(bookId)
                .where(BookDynamicSqlSupport.id,isEqualTo(id))
                .build()
                .render(RenderingStrategies.MYBATIS3));
    }

    @Override
    public String queryCatNameByCatId(int catId) {
        return bookCategoryMapper.selectMany(select(BookCategoryDynamicSqlSupport.name)
                .from(BookCategoryDynamicSqlSupport.bookCategory)
                .where(id, isEqualTo(catId))
                .build()
                .render(RenderingStrategies.MYBATIS3)).get(0).getName();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveBookAndIndexAndContent(Book book, List<BookIndex> bookIndexList, List<BookContent> bookContentList) {
        if(!queryIsExistByBookNameAndAuthorName(book.getBookName(),book.getAuthorName())) {

            if(bookIndexList.size()>0) {

                if (book.getId() == null) {
                    book.setId(new IdWorker().nextId());
                }

                //保存小说主表

                bookMapper.insertSelective(book);

                //批量保存目录和内容
                bookIndexMapper.insertMultiple(bookIndexList);
                bookContentMapper.insertMultiple(bookContentList);

            }
        }


    }

    @Override
    public List<Book> queryNeedUpdateBook(Date startDate, int limit) {
        List<Book> books = bookMapper.queryNeedUpdateBook(startDate, limit);
        if(books.size()>0) {
            //更新最后抓取时间为当前时间
            bookMapper.updateCrawlLastTime(books, new Date());
        }
        return books;
    }

    @Override
    public Map<Integer, BookIndex> queryExistBookIndexMap(Long bookId) {
        List<BookIndex> bookIndexs = bookIndexMapper.selectMany(select(BookIndexDynamicSqlSupport.id,BookIndexDynamicSqlSupport.indexNum,BookIndexDynamicSqlSupport.indexName)
                .from(BookIndexDynamicSqlSupport.bookIndex)
                .where(BookIndexDynamicSqlSupport.bookId,isEqualTo(bookId))
                .build()
                .render(RenderingStrategies.MYBATIS3));
        if (bookIndexs.size() > 0) {
            return  bookIndexs.stream().collect(Collectors.toMap(BookIndex::getIndexNum, Function.identity()));
        }
        return new HashMap<>(0);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateBookAndIndexAndContent(Book book, List<BookIndex> bookIndexList, List<BookContent> bookContentList, Map<Integer, BookIndex> existBookIndexMap) {
        Date currentDate = new Date();
        for (int i = 0; i < bookIndexList.size(); i++) {
            BookIndex bookIndex = bookIndexList.get(i);
            BookContent bookContent = bookContentList.get(i);

            //插入或更新目录
            Integer wordCount = bookContent.getContent().length();
            bookIndex.setWordCount(wordCount);
            bookIndex.setUpdateTime(currentDate);

            if(bookIndex.getId() == null) {
                //插入
                bookIndex.setBookId(book.getId());
                Long indexId = new IdWorker().nextId();
                bookIndex.setId(indexId);
                bookIndex.setCreateTime(currentDate);
                bookIndexMapper.insertSelective(bookIndex);
            }else{
                //更新
                bookIndexMapper.updateByPrimaryKeySelective(bookIndex);
            }

            if(bookContent.getIndexId() == null) {
                //插入
                bookContent.setIndexId(bookIndex.getId());
                bookContentMapper.insertSelective(bookContent);
            }else{
                //更新

                bookContentMapper.update(update(BookContentDynamicSqlSupport.bookContent)
                        .set(BookContentDynamicSqlSupport.content)
                        .equalTo(bookContent.getContent())
                        .where(BookContentDynamicSqlSupport.indexId,isEqualTo(bookContent.getIndexId()))
                        .build()
                        .render(RenderingStrategies.MYBATIS3));
            }

        }

        //更新小说主表
        if(bookIndexList.size()>0) {
            //有更新章节，才需要更新以下字段
            book.setWordCount(queryTotalWordCount(book.getId()));
            BookIndex lastIndex = bookIndexList.get(bookIndexList.size()-1);
            if(!existBookIndexMap.containsKey(lastIndex.getIndexNum())) {
                //如果最新章节不在已存在章节中，那么更新小说表最新章节信息
                book.setLastIndexId(lastIndex.getId());
                book.setLastIndexName(lastIndex.getIndexName());
                book.setLastIndexUpdateTime(currentDate);
            }
        }
        book.setUpdateTime(currentDate);
        book.setBookName(null);
        book.setAuthorName(null);
        if(Constants.VISIT_COUNT_DEFAULT.equals(book.getVisitCount())) {
            book.setVisitCount(null);
        }
        bookMapper.updateByPrimaryKeySelective(book);

    }

    @Override
    public void updateCrawlLastTime(Long bookId) {
        Book book = new Book();
        book.setId(bookId);
        book.setCrawlLastTime(new Date());
        bookMapper.updateByPrimaryKeySelective(book);
    }

    @Override
    public Book queryBookByBookNameAndAuthorName(String bookName, String authorName) {
        List<Book> books = bookMapper.selectMany(select(BookDynamicSqlSupport.id).from(BookDynamicSqlSupport.book)
                .where(BookDynamicSqlSupport.bookName, isEqualTo(bookName))
                .and(BookDynamicSqlSupport.authorName, isEqualTo(authorName))
                .build()
                .render(RenderingStrategies.MYBATIS3));

        if(books.size()>0){
            return books.get(0);
        }

        return null;

    }

    /**
     * 查询最后的章节
     * */
    private BookIndex queryLastIndex(Long bookId) {
        return bookIndexMapper.queryLastIndex(bookId);
    }

    /**
     * 查询小说总字数
     * */
    private Integer queryTotalWordCount(Long bookId) {

        return bookMapper.queryTotalWordCount(bookId);

    }
}

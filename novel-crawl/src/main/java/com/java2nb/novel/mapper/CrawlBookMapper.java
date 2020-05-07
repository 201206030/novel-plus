package com.java2nb.novel.mapper;

import com.java2nb.novel.entity.Book;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author Administrator
 */
public interface CrawlBookMapper extends BookMapper {

    /**
     * 查询需要更新的小说数据
     * @param startDate 最新更新时间的起始时间
     * @param limit 查询条数
     * @return 小说集合
     * */
    List<Book> queryNeedUpdateBook(@Param("startDate") Date startDate, @Param("limit") int limit);

    /**
     * 查询小说总字数
     * @param bookId 小说ID
     * @return 小说总字数
     * */
    Integer queryTotalWordCount(@Param("bookId") Long bookId);

    /**
     * 批量更新小说最后抓取时间
     * @param books 需要更新的小说集合
     * @param currentDate 当前时间
     * */
    void updateCrawlLastTime(@Param("books") List<Book> books,@Param("currentDate") Date currentDate);
}

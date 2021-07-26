package com.java2nb.novel.mapper;

import com.java2nb.novel.entity.Book;
import com.java2nb.novel.vo.BookSpVO;
import com.java2nb.novel.vo.BookVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Administrator
 */
public interface FrontNewsMapper extends NewsMapper {

    /**
     * 增加新闻阅读量
     * @param newsId 新闻ID
     * */
    void addReadCount(@Param("newsId") Integer newsId);

}

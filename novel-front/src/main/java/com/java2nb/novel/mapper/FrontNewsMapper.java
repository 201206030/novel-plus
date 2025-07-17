package com.java2nb.novel.mapper;

import org.apache.ibatis.annotations.Param;

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

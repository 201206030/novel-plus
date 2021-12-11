package com.java2nb.novel.service;


import io.github.xxyopen.model.page.PageBean;
import com.java2nb.novel.entity.News;
import com.java2nb.novel.vo.NewsVO;

import java.util.List;

/**
 * @author 11797
 */
public interface NewsService {

    /**
     * 查询首页新闻
     * @return
     * */
    List<News> listIndexNews();

    /**
     * 查询新闻
     * @param newsId 新闻id
     * @return 新闻
     * */
    News queryNewsInfo(Long newsId);

    /**
     * 分页查询新闻列表
     * @param page 页码
     * @param pageSize 分页大小
     * @return 新闻分页数据
     * */
    PageBean<News> listByPage(int page, int pageSize);

    /**
     * 增加新闻阅读量
     * @param newsId 新闻ID
     * */
    void addReadCount(Integer newsId);
}

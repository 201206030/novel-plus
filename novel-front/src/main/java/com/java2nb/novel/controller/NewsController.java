package com.java2nb.novel.controller;

import com.github.pagehelper.PageInfo;
import io.github.xxyopen.model.page.PageBean;
import com.java2nb.novel.core.enums.ResponseStatus;
import com.java2nb.novel.entity.News;
import com.java2nb.novel.service.NewsService;
import io.github.xxyopen.model.resp.RestResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 11797
 */
@RequestMapping("news")
@RestController
@Slf4j
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    /**
     * 查询首页新闻
     * */
    @GetMapping("listIndexNews")
    public RestResult<List<News>> listIndexNews(){
        return RestResult.ok(newsService.listIndexNews());
    }

    /**
     * 分页查询新闻列表
     * */
    @GetMapping("listByPage")
    public RestResult<PageBean<News>> listByPage(@RequestParam(value = "curr", defaultValue = "1") int page, @RequestParam(value = "limit", defaultValue = "5") int pageSize){
        return RestResult.ok(newsService.listByPage(page,pageSize));
    }

    /**
     * 查询新闻详情（含正文，供跨端客户端使用）
     * */
    @GetMapping("detail/{newsId}")
    public RestResult<?> detail(@PathVariable("newsId") Long newsId){
        News news;
        try {
            // queryNewsInfo 对不存在的新闻会抛异常，统一转为业务错误码
            news = newsService.queryNewsInfo(newsId);
        } catch (RuntimeException e) {
            news = null;
        }
        if (news == null) {
            return RestResult.fail(ResponseStatus.NEWS_NOT_EXISTS);
        }
        return RestResult.ok(news);
    }

    /**
     * 增加新闻阅读量
     * */
    @PostMapping("addReadCount")
    public RestResult<Void> addReadCount(@RequestParam(value = "newsId") Integer newsId){
        newsService.addReadCount(newsId);
        return RestResult.ok();
    }



}

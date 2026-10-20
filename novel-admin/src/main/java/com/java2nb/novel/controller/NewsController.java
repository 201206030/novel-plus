package com.java2nb.novel.controller;

import com.java2nb.novel.entity.NewsCategory;
import com.java2nb.novel.entity.News;
import com.java2nb.novel.service.NewsService;
import io.github.xxyopen.model.page.PageBean;
import io.github.xxyopen.model.resp.RestResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 新闻管理Controller：新闻列表/类别管理
 *
 * @author Administrator
 */
@RestController
@RequestMapping("news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @GetMapping("news/list")
    public RestResult<PageBean<News>> newsList(@RequestParam(required = false) String keyword,
        @RequestParam(value = "curr", defaultValue = "1") int page,
        @RequestParam(value = "limit", defaultValue = "10") int pageSize) {
        return RestResult.ok(newsService.newsList(keyword, page, pageSize));
    }

    @GetMapping("news/get")
    public RestResult<News> newsGet(@RequestParam Long id) {
        return RestResult.ok(newsService.newsGet(id));
    }

    @PostMapping("news/save")
    public RestResult<Void> newsSave(News news) {
        newsService.newsSave(news);
        return RestResult.ok();
    }

    @PostMapping("news/update")
    public RestResult<Void> newsUpdate(News news) {
        newsService.newsUpdate(news);
        return RestResult.ok();
    }

    @PostMapping("news/remove")
    public RestResult<Void> newsRemove(@RequestParam Long id) {
        newsService.newsRemove(id);
        return RestResult.ok();
    }

    @PostMapping("news/batchRemove")
    public RestResult<Void> newsBatchRemove(@RequestParam String ids) {
        newsService.newsBatchRemove(ids);
        return RestResult.ok();
    }

    @GetMapping("category/list")
    public RestResult<PageBean<NewsCategory>> categoryList(@RequestParam(required = false) String keyword,
        @RequestParam(value = "curr", defaultValue = "1") int page,
        @RequestParam(value = "limit", defaultValue = "10") int pageSize) {
        return RestResult.ok(newsService.categoryList(keyword, page, pageSize));
    }

    @GetMapping("category/get")
    public RestResult<NewsCategory> categoryGet(@RequestParam Integer id) {
        return RestResult.ok(newsService.categoryGet(id));
    }

    @PostMapping("category/save")
    public RestResult<Void> categorySave(NewsCategory category) {
        newsService.categorySave(category);
        return RestResult.ok();
    }

    @PostMapping("category/update")
    public RestResult<Void> categoryUpdate(NewsCategory category) {
        newsService.categoryUpdate(category);
        return RestResult.ok();
    }

    @PostMapping("category/remove")
    public RestResult<Void> categoryRemove(@RequestParam Integer id) {
        newsService.categoryRemove(id);
        return RestResult.ok();
    }

    @PostMapping("category/batchRemove")
    public RestResult<Void> categoryBatchRemove(@RequestParam String ids) {
        newsService.categoryBatchRemove(ids);
        return RestResult.ok();
    }

    /**
     * 类别简化列表（新闻/小说选择分类用）
     */
    @GetMapping("category/all")
    public RestResult<PageBean<NewsCategory>> categoryAll() {
        return RestResult.ok(newsService.categoryList(null, 1, 100));
    }

}

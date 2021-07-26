package com.java2nb.novel.controller;

import com.github.pagehelper.PageInfo;
import com.java2nb.novel.core.bean.PageBean;
import com.java2nb.novel.core.bean.ResultBean;
import com.java2nb.novel.entity.News;
import com.java2nb.novel.service.NewsService;
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
    public ResultBean<List<News>> listIndexNews(){
        return ResultBean.ok(newsService.listIndexNews());
    }

    /**
     * 分页查询新闻列表
     * */
    @GetMapping("listByPage")
    public ResultBean<PageBean<News>> listByPage(@RequestParam(value = "curr", defaultValue = "1") int page, @RequestParam(value = "limit", defaultValue = "5") int pageSize){
        return ResultBean.ok(newsService.listByPage(page,pageSize));
    }

    /**
     * 增加新闻阅读量
     * */
    @PostMapping("addReadCount")
    public ResultBean<Void> addReadCount(@RequestParam(value = "newsId") Integer newsId){
        newsService.addReadCount(newsId);
        return ResultBean.ok();
    }



}

package com.java2nb.novel.controller;

import io.github.xxyopen.model.page.PageBean;

import com.java2nb.novel.entity.CrawlSingleTask;
import com.java2nb.novel.entity.CrawlSource;
import com.java2nb.novel.service.CrawlService;
import io.github.xxyopen.model.resp.RestResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("crawl")
@RequiredArgsConstructor
public class CrawlController {

    private final CrawlService crawlService;


    /**
     * 新增爬虫源
     * */
    @PostMapping("addCrawlSource")
    public RestResult<Void> addCrawlSource(CrawlSource source){
        crawlService.addCrawlSource(source);

        return RestResult.ok();

    }

    /**
     * 爬虫源分页列表查询
     * */
    @GetMapping("listCrawlByPage")
    public RestResult<PageBean<CrawlSource>> listCrawlByPage(@RequestParam(value = "curr", defaultValue = "1") int page, @RequestParam(value = "limit", defaultValue = "10") int pageSize){

        return RestResult.ok(crawlService.listCrawlByPage(page,pageSize));
    }

    /**
     * 开启或停止爬虫
     * */
    @PostMapping("openOrCloseCrawl")
    public RestResult<Void> openOrCloseCrawl(Integer sourceId,Byte sourceStatus){

        crawlService.openOrCloseCrawl(sourceId,sourceStatus);

        return RestResult.ok();
    }

    /**
     * 新增单本采集任务
     * */
    @PostMapping("addCrawlSingleTask")
    public RestResult<Void> addCrawlSingleTask(CrawlSingleTask singleTask){
        crawlService.addCrawlSingleTask(singleTask);

        return RestResult.ok();

    }

    /**
     * 单本采集任务分页列表查询
     * */
    @GetMapping("listCrawlSingleTaskByPage")
    public RestResult<PageBean<CrawlSingleTask>> listCrawlSingleTaskByPage(@RequestParam(value = "curr", defaultValue = "1") int page, @RequestParam(value = "limit", defaultValue = "10") int pageSize){

        return RestResult.ok(crawlService.listCrawlSingleTaskByPage(page,pageSize));
    }

    /**
     * 删除采集任务
     * */
    @DeleteMapping("delCrawlSingleTask/{id}")
    public RestResult<Void> delCrawlSingleTask(@PathVariable("id") Long id){

        crawlService.delCrawlSingleTask(id);

        return RestResult.ok();
    }




}

package com.java2nb.novel.controller;

import com.java2nb.novel.core.bean.PageBean;
import com.java2nb.novel.core.bean.ResultBean;
import com.java2nb.novel.entity.CrawlSingleTask;
import com.java2nb.novel.entity.CrawlSource;
import com.java2nb.novel.service.CrawlService;
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
    public ResultBean<Void> addCrawlSource(CrawlSource source){
        crawlService.addCrawlSource(source);

        return ResultBean.ok();

    }

    /**
     * 爬虫源分页列表查询
     * */
    @GetMapping("listCrawlByPage")
    public ResultBean<PageBean<CrawlSource>> listCrawlByPage(@RequestParam(value = "curr", defaultValue = "1") int page, @RequestParam(value = "limit", defaultValue = "10") int pageSize){

        return ResultBean.ok(crawlService.listCrawlByPage(page,pageSize));
    }

    /**
     * 开启或停止爬虫
     * */
    @PostMapping("openOrCloseCrawl")
    public ResultBean<Void> openOrCloseCrawl(Integer sourceId,Byte sourceStatus){

        crawlService.openOrCloseCrawl(sourceId,sourceStatus);

        return ResultBean.ok();
    }

    /**
     * 新增单本采集任务
     * */
    @PostMapping("addCrawlSingleTask")
    public ResultBean<Void> addCrawlSingleTask(CrawlSingleTask singleTask){
        crawlService.addCrawlSingleTask(singleTask);

        return ResultBean.ok();

    }

    /**
     * 单本采集任务分页列表查询
     * */
    @GetMapping("listCrawlSingleTaskByPage")
    public ResultBean<PageBean<CrawlSingleTask>> listCrawlSingleTaskByPage(@RequestParam(value = "curr", defaultValue = "1") int page, @RequestParam(value = "limit", defaultValue = "10") int pageSize){

        return ResultBean.ok(crawlService.listCrawlSingleTaskByPage(page,pageSize));
    }

    /**
     * 删除采集任务
     * */
    @DeleteMapping("delCrawlSingleTask/{id}")
    public ResultBean<Void> delCrawlSingleTask(@PathVariable("id") Long id){

        crawlService.delCrawlSingleTask(id);

        return ResultBean.ok();
    }




}

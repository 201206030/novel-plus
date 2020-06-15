package com.java2nb.novel.controller;

import com.github.pagehelper.PageInfo;
import com.java2nb.novel.core.bean.ResultBean;
import com.java2nb.novel.core.utils.BeanUtil;
import com.java2nb.novel.entity.CrawlSingleTask;
import com.java2nb.novel.entity.CrawlSource;
import com.java2nb.novel.service.CrawlService;
import com.java2nb.novel.vo.CrawlSingleTaskVO;
import com.java2nb.novel.vo.CrawlSourceVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ResultBean addCrawlSource(CrawlSource source){
        crawlService.addCrawlSource(source);

        return ResultBean.ok();

    }

    /**
     * 爬虫源分页列表查询
     * */
    @PostMapping("listCrawlByPage")
    public ResultBean listCrawlByPage(@RequestParam(value = "curr", defaultValue = "1") int page, @RequestParam(value = "limit", defaultValue = "10") int pageSize){

        return ResultBean.ok(new PageInfo<>(BeanUtil.copyList(crawlService.listCrawlByPage(page,pageSize), CrawlSourceVO.class)
                 ));
    }

    /**
     * 开启或停止爬虫
     * */
    @PostMapping("openOrCloseCrawl")
    public ResultBean openOrCloseCrawl(Integer sourceId,Byte sourceStatus){

        crawlService.openOrCloseCrawl(sourceId,sourceStatus);

        return ResultBean.ok();
    }

    /**
     * 新增单本采集任务
     * */
    @PostMapping("addCrawlSingleTask")
    public ResultBean addCrawlSingleTask(CrawlSingleTask singleTask){
        crawlService.addCrawlSingleTask(singleTask);

        return ResultBean.ok();

    }

    /**
     * 单本采集任务分页列表查询
     * */
    @PostMapping("listCrawlSingleTaskByPage")
    public ResultBean listCrawlSingleTaskByPage(@RequestParam(value = "curr", defaultValue = "1") int page, @RequestParam(value = "limit", defaultValue = "10") int pageSize){

        return ResultBean.ok(new PageInfo<>(BeanUtil.copyList(crawlService.listCrawlSingleTaskByPage(page,pageSize), CrawlSingleTaskVO.class)
        ));
    }

    /**
     * 删除采集任务
     * */
    @PostMapping("delCrawlSingleTask")
    public ResultBean delCrawlSingleTask(Long id){

        crawlService.delCrawlSingleTask(id);

        return ResultBean.ok();
    }




}

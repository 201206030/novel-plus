package com.java2nb.novel.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java2nb.novel.core.bean.PageBean;
import com.java2nb.novel.core.bean.ResultBean;
import com.java2nb.novel.core.cache.CacheKey;
import com.java2nb.novel.core.cache.CacheService;
import com.java2nb.novel.core.crawl.CrawlParser;
import com.java2nb.novel.core.crawl.RuleBean;
import com.java2nb.novel.core.utils.HttpUtil;
import com.java2nb.novel.entity.BookIndex;
import com.java2nb.novel.entity.CrawlSingleTask;
import com.java2nb.novel.entity.CrawlSource;
import com.java2nb.novel.service.CrawlService;
import com.java2nb.novel.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("crawl")
@RequiredArgsConstructor
public class CrawlController {

    private final CrawlService crawlService;

    private final CacheService cacheService;
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
     * 获取爬虫源
     * */
    @GetMapping("getCrawlSource/{id}")
    public ResultBean<CrawlSource> getCrawlSource(@PathVariable("id") Integer id){
        CrawlSource crawlSource=  crawlService.getCrawlSource(id);
        return ResultBean.ok(crawlSource);

    }

    /**
     * 测试规则
     * @param rule
     * @param url
     * @param isRefresh
     * @return
     */
    @PostMapping("testParse")
    public ResultBean<Object> testParse(String rule,String url,String isRefresh){

        Map<String,Object> resultMap=new HashMap<>();
        String html =null;
        if(url.startsWith("https://")||url.startsWith("http://")){
            String refreshCache="1";
            if(!refreshCache.equals(isRefresh)) {
                Object cache = cacheService.getObject(CacheKey.BOOK_TEST_PARSE + url);
                if (cache == null) {
                    isRefresh="1";
                }else {
                    html = (String) cache;
                }
            }
            if(refreshCache.equals(isRefresh)){
                html = HttpUtil.getByHttpClientWithChrome(url);
                if (html != null) {
                    cacheService.setObject(CacheKey.BOOK_TEST_PARSE + url, html, 60 * 10);
                }else{
                    resultMap.put("msg","html is null");
                    return ResultBean.ok(resultMap);
                }
            }
        }else{
            resultMap.put("html","url is null");
            return ResultBean.ok(resultMap);
        }
        Pattern pattern = Pattern.compile(rule);
        Matcher matcher = pattern.matcher(html);
        boolean isFind = matcher.find();
        resultMap.put("是否匹配",isFind);
        if(isFind){
            resultMap.put("匹配结果",matcher.group(1));
        }
       // resultMap.put("url",url);
        return ResultBean.ok(resultMap);
    }
    /**
     * 修改爬虫源
     * */
    @PostMapping("updateCrawlSource")
    public ResultBean<Void> updateCrawlSource(CrawlSource source){
        crawlService.updateCrawlSource(source);
        return ResultBean.ok();

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

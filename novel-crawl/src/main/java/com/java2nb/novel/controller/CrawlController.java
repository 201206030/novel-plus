package com.java2nb.novel.controller;

import com.java2nb.novel.core.cache.CacheKey;
import com.java2nb.novel.core.cache.CacheService;
import com.java2nb.novel.core.utils.HttpUtil;
import io.github.xxyopen.model.page.PageBean;

import com.java2nb.novel.entity.CrawlSingleTask;
import com.java2nb.novel.entity.CrawlSource;
import com.java2nb.novel.service.CrawlService;
import io.github.xxyopen.model.resp.RestResult;
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
     * 获取爬虫源
     * */
    @GetMapping("getCrawlSource/{id}")
    public RestResult<CrawlSource> getCrawlSource(@PathVariable("id") Integer id){
        CrawlSource crawlSource=  crawlService.getCrawlSource(id);
        return RestResult.ok(crawlSource);

    }

    /**
     * 测试规则
     * @param rule
     * @param url
     * @param isRefresh
     * @return
     */
    @PostMapping("testParse")
    public RestResult<Object> testParse(String rule,String url,String isRefresh){

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
                    return RestResult.ok(resultMap);
                }
            }
        }else{
            resultMap.put("html","url is null");
            return RestResult.ok(resultMap);
        }
        Pattern pattern = Pattern.compile(rule);
        Matcher matcher = pattern.matcher(html);
        boolean isFind = matcher.find();
        resultMap.put("是否匹配",isFind);
        if(isFind){
            resultMap.put("匹配结果",matcher.group(1));
        }
       // resultMap.put("url",url);
        return RestResult.ok(resultMap);
    }
    /**
     * 修改爬虫源
     * */
    @PostMapping("updateCrawlSource")
    public RestResult<Void> updateCrawlSource(CrawlSource source) {
        crawlService.updateCrawlSource(source);
        return RestResult.ok();

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

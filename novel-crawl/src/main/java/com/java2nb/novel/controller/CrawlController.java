package com.java2nb.novel.controller;

import com.java2nb.novel.core.cache.CacheKey;
import com.java2nb.novel.core.cache.CacheService;
import com.java2nb.novel.core.crawl.RuleExtractor;
import com.java2nb.novel.core.utils.HttpUtil;
import com.java2nb.novel.entity.BookCategory;
import io.github.xxyopen.model.page.PageBean;

import com.java2nb.novel.entity.CrawlSingleTask;
import com.java2nb.novel.entity.CrawlSource;
import com.java2nb.novel.service.BookService;
import com.java2nb.novel.service.CrawlService;
import com.java2nb.novel.utils.Constants;
import io.github.xxyopen.model.resp.RestResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("crawl")
@RequiredArgsConstructor
public class CrawlController {

    private final CrawlService crawlService;

    private final BookService bookService;

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
     * 查询全部小说分类（book_category动态加载，供爬虫源编辑页/导入映射/单本采集分类下拉渲染）
     * */
    @GetMapping("listBookCategory")
    public RestResult<List<BookCategory>> listBookCategory(){
        return RestResult.ok(bookService.queryAllCategory());
    }

    /**
     * 测试规则
     * @param rule 提取规则
     * @param url 页面地址
     * @param isRefresh 是否强制刷新
     * @param parseType 解析方式：REGEX(默认)/CSS/XPATH/JSONPATH
     * @param charset 网站编码，留空按爬虫默认编码 UTF-8，需与爬虫源配置保持一致
     * @return
     */
    @PostMapping("testParse")
    public RestResult<Object> testParse(String rule,String url,String isRefresh,String parseType,String charset){

        Map<String,Object> resultMap=new HashMap<>();
        //规范化编码名称（utf-8 -> UTF-8），同时校验合法性，避免非法编码名导致抓页时抛异常
        String pageCharset = charset == null || charset.trim().isEmpty()
                ? Constants.CRAWL_DEFAULT_CHARSET : charset.trim();
        try {
            pageCharset = Charset.forName(pageCharset).name();
        } catch (IllegalArgumentException e) {
            resultMap.put("msg", "网站编码无效：" + pageCharset);
            return RestResult.ok(resultMap);
        }

        String html =null;
        //同一 URL 用不同编码解码得到的正文不同，缓存必须按编码区分，否则切换编码仍会命中旧的乱码内容
        String cacheKey = CacheKey.BOOK_TEST_PARSE + pageCharset + ":" + url;
        if(url.startsWith("https://")||url.startsWith("http://")){
            String refreshCache="1";
            if(!refreshCache.equals(isRefresh)) {
                html = cacheService.get(cacheKey);
                if (html == null) {
                    isRefresh="1";
                }
            }
            if(refreshCache.equals(isRefresh)){
                html = HttpUtil.getByHttpClientWithChrome(url, pageCharset);
                if (html != null) {
                    cacheService.set(cacheKey, html, 60 * 10);
                }else{
                    resultMap.put("msg","html is null");
                    return RestResult.ok(resultMap);
                }
            }
        }else{
            resultMap.put("html","url is null");
            return RestResult.ok(resultMap);
        }
        RuleExtractor extractor = RuleExtractor.create(parseType, html);
        List<String> results;
        try {
            results = extractor.extractAll(rule);
        } catch (IllegalArgumentException e) {
            resultMap.put("msg", e.getMessage());
            return RestResult.ok(resultMap);
        }
        boolean isFind = !results.isEmpty();
        resultMap.put("网站编码", pageCharset);
        resultMap.put("解析方式", parseType == null || parseType.isEmpty() ? "REGEX" : parseType.toUpperCase());
        resultMap.put("是否匹配",isFind);
        if(isFind){
            resultMap.put("匹配结果",results.get(0));
            resultMap.put("全部结果",results);
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
     * 删除爬虫源（仅限已停止的源，不删除已采集小说）
     * */
    @DeleteMapping("delCrawlSource/{id}")
    public RestResult<Void> delCrawlSource(@PathVariable("id") Integer id){

        crawlService.delCrawlSource(id);

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

    /**
     * 采集任务进度查询
     * */
    @GetMapping("getTaskProgress/{id}")
    public RestResult<Integer> getTaskProgress(@PathVariable("id") Long id){
        return RestResult.ok(crawlService.getTaskProgress(id));
    }




}

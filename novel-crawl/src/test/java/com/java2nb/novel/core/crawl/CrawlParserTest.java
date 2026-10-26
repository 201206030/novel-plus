package com.java2nb.novel.core.crawl;

import com.java2nb.novel.entity.Book;
import com.java2nb.novel.utils.CrawlHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * 目录分页与正文分页采集测试（mock HTTP抓取）
 */
class CrawlParserTest {

    private static final String BASE = "http://test.com";

    private CrawlHttpClient crawlHttpClient;

    private CrawlParser crawlParser;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        crawlHttpClient = mock(CrawlHttpClient.class);
        StringRedisTemplate redisTemplate = mock(StringRedisTemplate.class);
        ValueOperations<String, String> valueOperations = mock(ValueOperations.class);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        crawlParser = new CrawlParser(crawlHttpClient, redisTemplate);
    }

    private RuleBean cssRule(String nextIndex, String nextContent) {
        RuleBean ruleBean = new RuleBean();
        ruleBean.setParseType("CSS");
        ruleBean.setUrlPrefix(BASE);
        ruleBean.setBookIndexUrl("{bookId}");
        ruleBean.setIndexNamePatten(".list a::text");
        ruleBean.setIndexIdPatten(".list a::attr(href)");
        ruleBean.setNextIndexUrlPatten(nextIndex);
        ruleBean.setBookContentUrl("{indexId}");
        ruleBean.setContentPatten("#con::text");
        ruleBean.setNextContentUrlPatten(nextContent);
        return ruleBean;
    }

    @Test
    void shouldFollowIndexAndContentPagination() throws Exception {
        //目录第1页：两章 + 下页链接（相对当前页）
        when(crawlHttpClient.get(eq(BASE + "/index/1.html"), anyString())).thenReturn(
            "<div class=\"list\"><a href=\"/c/1.html\">第一章</a><a href=\"/c/2.html\">第二章</a></div>"
                + "<a href=\"2.html\">下页</a>");
        //目录第2页：一章，无下页（同目录相对地址 + 末尾页）
        when(crawlHttpClient.get(eq(BASE + "/index/2.html"), anyString())).thenReturn(
            "<div class=\"list\"><a href=\"/c/3.html\">第三章</a></div>");
        //第1章正文分两页
        when(crawlHttpClient.get(eq(BASE + "/c/1.html"), anyString())).thenReturn(
            "<div id=\"con\">AA</div><a class=\"next\" href=\"1_2.html\">下一页</a>");
        when(crawlHttpClient.get(eq(BASE + "/c/1_2.html"), anyString())).thenReturn(
            "<div id=\"con\">BB</div>");
        when(crawlHttpClient.get(eq(BASE + "/c/2.html"), anyString())).thenReturn(
            "<div id=\"con\">CC</div>");
        when(crawlHttpClient.get(eq(BASE + "/c/3.html"), anyString())).thenReturn(
            "<div id=\"con\">DD</div>");

        Book book = new Book();
        book.setId(1L);
        AtomicReference<ChapterBean> captured = new AtomicReference<>();
        boolean result = crawlParser.parseBookIndexAndContent("index/1.html", book,
            cssRule("a:containsOwn(下页)::attr(href)", ".next::attr(href)"),
            1, new HashMap<>(), captured::set, null);

        assertTrue(result);
        ChapterBean chapter = captured.get();
        //跨页汇总3章且按页序编号
        assertEquals(3, chapter.getBookIndexList().size());
        assertEquals("第一章", chapter.getBookIndexList().get(0).getIndexName());
        assertEquals(0, chapter.getBookIndexList().get(0).getIndexNum());
        assertEquals("第三章", chapter.getBookIndexList().get(2).getIndexName());
        assertEquals(2, chapter.getBookIndexList().get(2).getIndexNum());
        //正文分页按页序拼接
        assertEquals("AA\nBB", chapter.getBookContentList().get(0).getContent());
        assertEquals("CC", chapter.getBookContentList().get(1).getContent());
        assertEquals("DD", chapter.getBookContentList().get(2).getContent());
    }

    @Test
    void shouldStopWhenNextPageLinksToItself() throws Exception {
        //目录下一页指向当前页，visited去重后只采一页
        when(crawlHttpClient.get(eq(BASE + "/index/1.html"), anyString())).thenReturn(
            "<div class=\"list\"><a href=\"/c/1.html\">第一章</a></div>"
                + "<a href=\"" + BASE + "/index/1.html\">下页</a>");
        when(crawlHttpClient.get(eq(BASE + "/c/1.html"), anyString())).thenReturn("<div id=\"con\">AA</div>");

        Book book = new Book();
        book.setId(2L);
        AtomicReference<ChapterBean> captured = new AtomicReference<>();
        boolean result = crawlParser.parseBookIndexAndContent("index/1.html", book,
            cssRule("a:containsOwn(下页)::attr(href)", null),
            1, new HashMap<>(), captured::set, null);

        assertTrue(result);
        assertEquals(1, captured.get().getBookIndexList().size());
    }

    @Test
    void shouldWorkWithoutPagingRules() throws Exception {
        //无分页规则时保持单次抓取行为
        when(crawlHttpClient.get(eq(BASE + "/index/1.html"), anyString())).thenReturn(
            "<div class=\"list\"><a href=\"/c/1.html\">第一章</a></div>");
        when(crawlHttpClient.get(eq(BASE + "/c/1.html"), anyString())).thenReturn("<div id=\"con\">AA</div>");

        Book book = new Book();
        book.setId(3L);
        AtomicReference<ChapterBean> captured = new AtomicReference<>();
        boolean result = crawlParser.parseBookIndexAndContent("index/1.html", book,
            cssRule(null, null), 1, new HashMap<>(), captured::set, null);

        assertTrue(result);
        assertEquals(1, captured.get().getBookIndexList().size());
        assertEquals("AA", captured.get().getBookContentList().get(0).getContent());
    }

}

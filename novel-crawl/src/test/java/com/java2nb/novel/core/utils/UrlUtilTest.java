package com.java2nb.novel.core.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 相对URL补全测试
 */
class UrlUtilTest {

    @Test
    void absolutizeShouldResolveAgainstOriginBase() {
        //baseUrl只有origin时，普通相对地址按站点根拼接而不是覆盖主机名
        assertEquals("http://test.com/index/1.html", UrlUtil.absolutize("http://test.com", "index/1.html"));
        assertEquals("https://www.sudugu.org/book/1.html", UrlUtil.absolutize("https://www.sudugu.org", "/book/1.html"));
    }

    @Test
    void absolutizeShouldResolveAgainstDirectoryBase() {
        assertEquals("http://test.com/index/2.html", UrlUtil.absolutize("http://test.com/index/1.html", "2.html"));
        assertEquals("http://test.com/a/b.html", UrlUtil.absolutize("http://test.com/a/", "b.html"));
        assertEquals("http://test.com/root.html", UrlUtil.absolutize("http://test.com/a/1.html", "/root.html"));
    }

    @Test
    void absolutizeShouldKeepAbsoluteAndSchemeRelative() {
        assertEquals("http://other.com/x", UrlUtil.absolutize("http://test.com", "http://other.com/x"));
        assertEquals("https://cdn.test.com/x.png", UrlUtil.absolutize("https://www.test.com/a/", "//cdn.test.com/x.png"));
    }

    @Test
    void originShouldExtractSchemeAndHost() {
        assertEquals("https://www.sudugu.co", UrlUtil.origin("https://www.sudugu.co/sudugu/123/"));
        assertEquals("https://www.sudugu.co", UrlUtil.origin("https://www.sudugu.co"));
        assertEquals(null, UrlUtil.origin("www.sudugu.co"));
    }

}

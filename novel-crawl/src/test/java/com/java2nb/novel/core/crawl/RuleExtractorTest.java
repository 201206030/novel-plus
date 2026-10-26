package com.java2nb.novel.core.crawl;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 提取器统一规则语法测试（重点覆盖##替换后处理与规则级语法前缀）
 */
class RuleExtractorTest {

    private static final String HTML = "<div class=\"content\">"
        + "<p>第一章 初入江湖（本章完）</p>"
        + "<p>请收藏本站：example.com</p>"
        + "<p>（本章完）</p>"
        + "</div>"
        + "<div class=\"book\"><a href=\"/book/123.html\">书名</a></div>";

    private static final String JSON = "{\"name\":\"张三\",\"chapters\":[{\"url\":\"/c/1.html\"},{\"url\":\"/c/2.html\"}]}";

    @Test
    void hashReplaceWithoutReplacementShouldDelete() {
        RuleExtractor extractor = RuleExtractor.create("CSS", HTML);
        List<String> values = extractor.extractAll(".content p::text##（本章完）");
        assertEquals(2, values.size());
        assertEquals("第一章 初入江湖", values.get(0));
        assertEquals("请收藏本站：example.com", values.get(1));
    }

    @Test
    void hashReplaceWithReplacement() {
        RuleExtractor extractor = RuleExtractor.create("CSS", HTML);
        List<String> values = extractor.extractAll(".content p::text##请收藏本站：.*##继续阅读");
        assertEquals(3, values.size());
        assertEquals("第一章 初入江湖（本章完）", values.get(0));
        assertEquals("继续阅读", values.get(1));
        //不匹配替换正则的值原样保留
        assertEquals("（本章完）", values.get(2));
    }

    @Test
    void hashReplaceAppliesToHtmlHint() {
        RuleExtractor extractor = RuleExtractor.create("CSS", HTML);
        String value = extractor.extractFirst(".content::html##（本章完）");
        //html取值中的匹配文本被删除，标签结构保留
        assertEquals("<p>第一章 初入江湖</p><p>请收藏本站：example.com</p><p></p>", value);
    }

    @Test
    void hashReplaceBeforePostRegex() {
        RuleExtractor extractor = RuleExtractor.create("CSS", HTML);
        //##替换先于|二次正则：先把链接中的123替换成456，再截取数字
        String value = extractor.extractFirst("a::attr(href)|(\\d+)##123##456");
        assertEquals("456", value);
    }

    @Test
    void xpathPrefixShouldOverrideCssExtractor() {
        RuleExtractor extractor = RuleExtractor.create("CSS", HTML);
        List<String> values = extractor.extractAll("xpath://div[@class='content']//p::text##（本章完）");
        assertEquals(2, values.size());
        assertEquals("第一章 初入江湖", values.get(0));
    }

    @Test
    void jsonPrefixShouldReadJsonContent() {
        RuleExtractor extractor = RuleExtractor.create("CSS", JSON);
        List<String> values = extractor.extractAll("json:$.chapters[*].url");
        assertEquals(2, values.size());
        assertEquals("/c/1.html", values.get(0));
    }

    @Test
    void cssPrefixShouldWorkOnJsonExtractor() {
        RuleExtractor extractor = RuleExtractor.create("JSONPATH", HTML);
        String value = extractor.extractFirst("css:.book a::attr(href)");
        assertEquals("/book/123.html", value);
    }

    @Test
    void plainRuleWithoutPrefixKeepsOwnExtractor() {
        RuleExtractor extractor = RuleExtractor.create("CSS", HTML);
        String value = extractor.extractFirst("a::attr(href)|(\\d+)");
        assertEquals("123", value);
    }

    /**
     * :eq(n)应按"第n个匹配元素"理解（速读谷导入规则回归：jsoup原生的"父节点第n个子节点"语义会导致提取为0）
     */
    @Test
    void cssEqShouldMeanNthMatchNotNthChild() {
        //速读谷目录页结构：#list前面有兄弟节点，不是父节点的第一个子元素
        String sdHtml = "<div class=\"wrap\"><div class=\"intro\">简介</div>"
            + "<h2 id=\"dir\"><span>更新时间</span></h2>"
            + "<div id=\"list\"><ul>"
            + "<li><a href=\"/1378/1.html\">第一章</a></li><li><a href=\"/1378/2.html\">第二章</a></li>"
            + "</ul></div></div>"
            + "<div class=\"head\"><span class=\"logo\">站标</span><img alt=\"书名\" src=\"/cover.jpg\"></div>";
        RuleExtractor extractor = RuleExtractor.create("CSS", sdHtml);
        List<String> hrefs = extractor.extractAll("#list:eq(0) a::attr(href)");
        assertEquals(2, hrefs.size());
        assertEquals("/1378/1.html", hrefs.get(0));
        //img不是父节点第一个子节点，但应是第0个匹配到的img
        assertEquals("书名", extractor.extractFirst("img:eq(0)::attr(alt)"));
    }

    /**
     * 逐段嵌套的:eq索引：第一段第0个容器内，第二段第1个子匹配，再取其中第0个链接
     */
    @Test
    void cssNestedEqShouldIndexWithinEachSegment() {
        String html = "<div class=\"box\"><i>干扰</i>"
            + "<div class=\"itemtxt\"><p>第一行</p><p><a href=\"/a\">作者A</a></p></div>"
            + "<div class=\"itemtxt\"><p>别的</p><p><a href=\"/b\">作者B</a></p></div></div>"
            + "<div class=\"box\"><div class=\"itemtxt\"><p>x</p><p><a href=\"/c\">作者C</a></p></div></div>";
        RuleExtractor extractor = RuleExtractor.create("CSS", html);
        String value = extractor.extractFirst(".box:eq(1) .itemtxt:eq(0) p:eq(1) a:eq(0)::attr(href)");
        assertEquals("/c", value);
        //索引越界时返回空而不是报错
        assertEquals(null, extractor.extractFirst(".itemtxt:eq(9) p:eq(0)::text"));
    }

}

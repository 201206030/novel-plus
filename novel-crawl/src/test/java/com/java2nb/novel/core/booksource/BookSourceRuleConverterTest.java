package com.java2nb.novel.core.booksource;

import com.java2nb.novel.core.crawl.RuleBean;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 阅读3.0书源转换器测试，覆盖DSL转CSS、XPath直通、混合语法前缀、@js不可转换等典型书源形态
 */
class BookSourceRuleConverterTest {

    private final BookSourceRuleConverter converter = new BookSourceRuleConverter();

    /**
     * 速读谷风格：阅读DSL全量转CSS，##过滤透传，exploreUrl暴露分类条目供手动映射
     */
    @Test
    void convertDslSourceShouldRenderCssRules() {
        BookSourceBean source = new BookSourceBean();
        source.setBookSourceName("速读谷");
        source.setBookSourceUrl("https://www.sudugu.org");
        source.setEnabled(true);
        source.setRuleBookInfo(map(
            "name", "class.itemtxt.0@tag.h1@text",
            "author", "class.itemtxt.0@tag.a@text##作者：",
            "coverUrl", "class.itemimg.0@tag.img@src",
            "intro", "class.intro.0@text",
            "kind", "class.point.0@text"));
        source.setRuleToc(map(
            "chapterList", "class.listbody.0@tag.a",
            "chapterName", "text",
            "chapterUrl", "href"));
        source.setRuleContent(map(
            "content", "id.content.0@html",
            "replaceRegex", "##请收藏.*"));
        source.setRuleSearch(map(
            "bookList", "class.itembox",
            "bookUrl", "class.cover.0@tag.a@href"));
        source.setExploreUrl("玄幻小说::/xuanhuan/page.html\n都市小说::/dushi/page.html");

        BookSourceConvertResult result = converter.convert(source);

        assertEquals(BookSourceConvertResult.Status.PARTIAL, result.getStatus());
        assertTrue(result.isImportable());
        RuleBean bean = result.getRuleBean();
        assertNotNull(bean);
        assertEquals("CSS", bean.getParseType());
        assertEquals("https://www.sudugu.org", bean.getUrlPrefix());
        assertEquals("{bookId}", bean.getBookDetailUrl());
        assertEquals("{bookId}", bean.getBookIndexUrl());
        assertEquals("{indexId}", bean.getBookContentUrl());
        assertEquals(".itemtxt:eq(0) h1::text", bean.getBookNamePatten());
        assertEquals(".itemtxt:eq(0) a::text##作者：", bean.getAuthorNamePatten());
        assertEquals(".itemimg:eq(0) img::attr(src)", bean.getPicUrlPatten());
        assertEquals(".intro:eq(0)::text", bean.getDescPatten());
        assertEquals(".listbody:eq(0) a::text", bean.getIndexNamePatten());
        assertEquals(".listbody:eq(0) a::attr(href)", bean.getIndexIdPatten());
        assertEquals("#content:eq(0)::html##请收藏.*", bean.getContentPatten());
        assertEquals(".itembox .cover:eq(0) a::attr(href)", bean.getBookIdPatten());
        //发现分类不再默认顺序映射，catIdRule留空由导入用户在预览中手动映射
        Map<String, String> catIdRule = bean.getCatIdRule();
        assertNotNull(catIdRule);
        assertTrue(catIdRule.isEmpty());
        //发现分类条目暴露给前端供手动映射
        assertEquals(2, result.getExploreOptions().size());
        assertEquals("玄幻小说", result.getExploreOptions().get(0).getTitle());
        assertEquals("https://www.sudugu.org/xuanhuan/{page}.html", result.getExploreOptions().get(0).getUrl());
        assertEquals("https://www.sudugu.org/dushi/{page}.html", result.getExploreOptions().get(1).getUrl());
        //kind属于novel-plus不承载的常规元信息，已静默不再产生“未导入”警告；搜索页兜底列表规则仍应警告
        assertFalse(result.getWarnings().stream().anyMatch(w -> w.contains("kind")));
        assertTrue(result.getWarnings().stream().anyMatch(w -> w.contains("搜索页规则")));
    }

    @Test
    void convertPagePlaceholderVariantsShouldNormalize() {
        //书源原始URL已带{{page}}/{page}/${page}占位时，不得再次套壳成{{{page}}}
        BookSourceBean source = new BookSourceBean();
        source.setBookSourceName("快书5");
        source.setBookSourceUrl("https://www.kuaishu5.com");
        source.setRuleBookInfo(map(
            "name", "tag.h1@text",
            "author", "class.author@text"));
        source.setRuleToc(map(
            "chapterList", "div.list@tag.a",
            "chapterName", "text",
            "chapterUrl", "href"));
        source.setRuleContent(map("content", "div.con@html"));
        source.setRuleSearch(map(
            "bookList", "div.item",
            "bookUrl", "tag.a@href"));
        source.setExploreUrl("网文::/wangyou/p{{page}}.html\n女频::/nvpin/p{page}.html\n完结::/wanjie/p${page}.html");

        BookSourceConvertResult result = converter.convert(source);

        assertTrue(result.isImportable());
        //不再默认映射，归一结果通过exploreOptions暴露给用户选择
        assertTrue(result.getRuleBean().getCatIdRule().isEmpty());
        assertEquals("https://www.kuaishu5.com/wangyou/p{page}.html", result.getExploreOptions().get(0).getUrl());
        assertEquals("https://www.kuaishu5.com/nvpin/p{page}.html", result.getExploreOptions().get(1).getUrl());
        assertEquals("https://www.kuaishu5.com/wanjie/p{page}.html", result.getExploreOptions().get(2).getUrl());
    }

    /**
     * 快书网风格：meta信息XPath直通，正文CSS规则加css:前缀
     */
    @Test
    void convertMetaXpathSourceShouldPassThrough() {
        BookSourceBean source = new BookSourceBean();
        source.setBookSourceName("快书网");
        source.setBookSourceUrl("https://www.kuaishu18.com");
        source.setRuleBookInfo(map(
            "name", "//meta[@property=\"og:novel:book_name\"]/@content",
            "author", "//meta[@property=\"og:novel:author\"]/@content",
            "coverUrl", "//meta[@property=\"og:image\"]/@content",
            "intro", "//meta[@property=\"og:description\"]/@content"));
        source.setRuleToc(map(
            "chapterList", "//div[@id=\"list\"]//a",
            "chapterName", "text",
            "chapterUrl", "href"));
        source.setRuleContent(map("content", "class.con@html"));
        source.setRuleSearch(map(
            "bookList", "//div[@class=\"item\"]",
            "bookUrl", "href"));

        BookSourceConvertResult result = converter.convert(source);

        assertTrue(result.isImportable());
        RuleBean bean = result.getRuleBean();
        assertNotNull(bean);
        assertEquals("XPATH", bean.getParseType());
        assertEquals("//meta[@property=\"og:novel:book_name\"]::attr(content)", bean.getBookNamePatten());
        assertEquals("//div[@id=\"list\"]//a::text", bean.getIndexNamePatten());
        assertEquals("//div[@id=\"list\"]//a::attr(href)", bean.getIndexIdPatten());
        //少数派CSS语法加规则级前缀
        assertEquals("css:.con::html", bean.getContentPatten());
    }

    /**
     * 大文学风格：JSONPath与XPath混合，JSONPath规则渲染为json:前缀
     */
    @Test
    void convertMixedJsonpathSourceShouldRenderPrefix() {
        BookSourceBean source = new BookSourceBean();
        source.setBookSourceName("大文学");
        source.setBookSourceUrl("http://www.dwxsw.net");
        source.setRuleBookInfo(map(
            "name", "$.bookName",
            "author", "//meta[@name=\"author\"]/@content"));
        source.setRuleToc(map(
            "chapterList", "//dl[@class=\"list\"]//a",
            "chapterName", "text",
            "chapterUrl", "href"));
        source.setRuleContent(map("content", "//div[@id=\"content\"]//text()"));
        source.setRuleSearch(map(
            "bookList", "$.data",
            "bookUrl", "$.url"));

        BookSourceConvertResult result = converter.convert(source);

        assertTrue(result.isImportable());
        RuleBean bean = result.getRuleBean();
        assertNotNull(bean);
        //多数规则为XPath语法族
        assertEquals("XPATH", bean.getParseType());
        assertEquals("json:$.bookName", bean.getBookNamePatten());
        assertEquals("json:$.url", bean.getBookIdPatten());
        assertEquals("//dl[@class=\"list\"]//a::attr(href)", bean.getIndexIdPatten());
        //无发现页配置时分类采集不可用但可单本采集
        assertTrue(bean.getCatIdRule().isEmpty());
        assertTrue(result.getWarnings().stream().anyMatch(w -> w.contains("发现")));
    }

    /**
     * 来看文学风格：核心规则@js依赖，标记不可导入并给出原因
     */
    @Test
    void convertJsHeavySourceShouldFail() {
        BookSourceBean source = new BookSourceBean();
        source.setBookSourceName("来看文学");
        source.setBookSourceUrl("https://wwwlakian1com.web.cdn-akb01.com");
        source.setRuleBookInfo(map(
            "name", "@js:result.put('name', 1)",
            "author", "@js:result.put('author', 1)"));
        source.setRuleToc(map(
            "chapterList", "//dd/a",
            "chapterName", "text",
            "chapterUrl", "href"));
        source.setRuleContent(map("content", "id.content@html"));

        BookSourceConvertResult result = converter.convert(source);

        assertEquals(BookSourceConvertResult.Status.FAILED, result.getStatus());
        assertFalse(result.isImportable());
        assertNull(result.getRuleBean());
        assertTrue(result.getErrors().stream().anyMatch(e -> e.contains("详情页书名")));
        assertTrue(result.getErrors().stream().anyMatch(e -> e.contains("JS")));
    }

    /**
     * 必填规则缺失（无正文/目录规则）同样不可导入
     */
    @Test
    void convertMissingRequiredRuleShouldFail() {
        BookSourceBean source = new BookSourceBean();
        source.setBookSourceName("残缺书源");
        source.setBookSourceUrl("https://www.example.com");
        source.setRuleBookInfo(map("name", "tag.h1@text"));

        BookSourceConvertResult result = converter.convert(source);

        assertEquals(BookSourceConvertResult.Status.FAILED, result.getStatus());
        assertTrue(result.getErrors().stream().anyMatch(e -> e.contains("正文内容")));
        assertTrue(result.getErrors().stream().anyMatch(e -> e.contains("目录")));
    }

    /**
     * 书源地址非法直接失败
     */
    @Test
    void convertInvalidSourceUrlShouldFail() {
        BookSourceBean source = new BookSourceBean();
        source.setBookSourceName("无地址书源");
        source.setBookSourceUrl("www.example.com");

        BookSourceConvertResult result = converter.convert(source);

        assertEquals(BookSourceConvertResult.Status.FAILED, result.getStatus());
        assertTrue(result.getErrors().stream().anyMatch(e -> e.contains("bookSourceUrl")));
    }

    /**
     * GBK站点从searchUrl探测编码
     */
    @Test
    void convertGbkSourceShouldSetCharset() {
        BookSourceBean source = new BookSourceBean();
        source.setBookSourceName("八一中文");
        source.setBookSourceUrl("https://www.81zww.com");
        source.setSearchUrl("/s.php?keyword={{key}}&charset=gbk");
        source.setRuleBookInfo(map(
            "name", "//meta[@property=\"og:novel:book_name\"]/@content",
            "author", "//meta[@property=\"og:novel:author\"]/@content"));
        source.setRuleToc(map(
            "chapterList", "//dd/a",
            "chapterName", "text",
            "chapterUrl", "href"));
        source.setRuleContent(map("content", "//div[@id=\"content\"]"));

        BookSourceConvertResult result = converter.convert(source);

        assertTrue(result.isImportable());
        assertEquals("gbk", result.getRuleBean().getCharset());
    }

    /**
     * “下一页”规则转换：text定位近似、@css:、XPath直通；无法转换时降级警告
     */
    @Test
    void convertNextPageRules() {
        BookSourceBean source = new BookSourceBean();
        source.setBookSourceName("分页书源");
        source.setBookSourceUrl("https://www.example.com");
        source.setRuleBookInfo(map(
            "name", "tag.h1@text",
            "author", "tag.a.0@text"));
        source.setRuleToc(map(
            "chapterList", "class.list.0@tag.a",
            "chapterName", "text",
            "chapterUrl", "href",
            "nextTocUrl", "text.下页@href"));
        source.setRuleContent(map(
            "content", "id.con@html",
            "nextContentUrl", "//a[text()='下一页']/@href"));

        BookSourceConvertResult result = converter.convert(source);

        assertTrue(result.isImportable());
        RuleBean bean = result.getRuleBean();
        assertNotNull(bean);
        assertEquals("CSS", bean.getParseType());
        //text定位近似为包含该文本的链接
        assertEquals("a:containsOwn(下页)::attr(href)", bean.getNextIndexUrlPatten());
        //少数派XPath语法加规则级前缀
        assertEquals("xpath://a[text()='下一页']::attr(href)", bean.getNextContentUrlPatten());
        assertTrue(result.getWarnings().stream().anyMatch(w -> w.contains("文本定位")));
    }

    /**
     * @css:与普通DSL形式的下一页规则可直接转换；含链中text定位片段的降级为警告
     */
    @Test
    void convertNextPageRuleFallback() {
        BookSourceBean source = new BookSourceBean();
        source.setBookSourceName("降级书源");
        source.setBookSourceUrl("https://www.example.com");
        source.setRuleBookInfo(map(
            "name", "tag.h1@text",
            "author", "tag.a.0@text"));
        source.setRuleToc(map(
            "chapterList", "class.list.0@tag.a",
            "chapterName", "text",
            "chapterUrl", "href",
            "nextTocUrl", "class.prenext.0@tag.span.-1@text.下一页.0@href"));
        source.setRuleContent(map(
            "content", "id.con@html",
            "nextContentUrl", "@css:#pages .gr:not([href='#'])@href"));

        BookSourceConvertResult result = converter.convert(source);

        assertTrue(result.isImportable());
        RuleBean bean = result.getRuleBean();
        assertNotNull(bean);
        //链中text定位片段无法映射，目录分页降级
        assertNull(bean.getNextIndexUrlPatten());
        assertTrue(result.getWarnings().stream().anyMatch(w -> w.contains("目录下一页") && w.contains("无法静态转换")));
        //@css:显式规则正常转换
        assertEquals("#pages .gr:not([href='#'])::attr(href)", bean.getNextContentUrlPatten());
    }

    private static Map<String, String> map(String... kv) {
        Map<String, String> m = new LinkedHashMap<>();
        for (int i = 0; i + 1 < kv.length; i += 2) {
            m.put(kv[i], kv[i + 1]);
        }
        return m;
    }

}

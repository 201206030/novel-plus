package com.java2nb.novel.core.crawl;

import com.java2nb.novel.utils.Constants;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 爬虫解析规则bean
 *
 * @author Administrator
 */
@Data
public class RuleBean {

    /**
     * 解析方式：REGEX(默认)/CSS/XPATH/JSONPATH，空值按REGEX处理兼容老规则
     */
    private String parseType;

    /**
     * 网页字符编码
     */
    private String charset = Constants.CRAWL_DEFAULT_CHARSET;


    /**
     * 小说更新列表url
     */
    private String updateBookListUrl;

    /**
     * 分类列表页URL规则
     */
    private String bookListUrl;

    /**
     * 分类URL规则，key为catId+book_category分类ID（动态分类，如catId1/catId8），value为分类列表页URL（支持{page}占位）；
     * 默认空Map避免导入书源未配置分类时采集线程空指针
     */
    private Map<String, String> catIdRule = new HashMap<>();

    private Map<String, Byte> bookStatusRule;

    private String bookIdPatten;
    private String pagePatten;
    private String totalPagePatten;
    private String bookDetailUrl;
    private String bookNamePatten;
    private String authorNamePatten;
    private String picUrlPatten;
    private String statusPatten;
    private String scorePatten;
    private String visitCountPatten;
    private String descStart;
    private String descEnd;
    /**
     * 简介提取规则（优先于descStart/descEnd，非REGEX模式使用），
     * 如 CSS：#intro::text、XPath：//div[@id='intro']::text、JSONPath：$.data.desc
     */
    private String descPatten;
    private String filterDesc;
    private String upadateTimePatten;
    private String upadateTimeFormatPatten;
    private String bookIndexUrl;
    private String indexIdPatten;
    private String indexNamePatten;
    /**
     * 目录页“下一页”链接提取规则（如 a:containsOwn(下页)::attr(href)），
     * 留空表示目录不分页；相对地址按当前目录页URL补全
     */
    private String nextIndexUrlPatten;
    private String bookContentUrl;
    private String contentStart;
    private String contentEnd;
    /**
     * 正文提取规则（优先于contentStart/contentEnd，非REGEX模式使用），
     * 如 CSS：#content::html、XPath：//div[@id='content']::html、JSONPath：$.data.content
     */
    private String contentPatten;

    /**
     * 正文页“下一页”链接提取规则（如 .next::attr(href)），
     * 留空表示正文不分页；分页内容按页序拼接，相对地址按当前页URL补全
     */
    private String nextContentUrlPatten;


    private String picUrlPrefix;

    /**
     * 相对URL补全前缀（一般为源站origin，如 https://www.sudugu.co），
     * 详情页/目录页/内容页URL拼接后不是绝对地址时用它补全，
     * 导入的阅读3.0书源会设置该字段，老规则留空保持原有行为
     */
    private String urlPrefix;

    private String bookIndexStart;

    private String filterContent;


}

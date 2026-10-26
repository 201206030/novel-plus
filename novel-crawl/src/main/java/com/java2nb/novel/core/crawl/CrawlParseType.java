package com.java2nb.novel.core.crawl;

/**
 * 爬虫规则解析方式
 * <p>
 * 每种解析方式对应一个 {@link RuleExtractor} 实现，规则JSON中的 parseType 字段决定使用哪种方式。
 * 历史爬虫规则没有该字段，默认按 REGEX 处理，保证向下兼容。
 *
 * @author xiongxiaoyang
 */
public enum CrawlParseType {

    /**
     * 正则表达式（默认），提取第1个捕获组
     */
    REGEX,

    /**
     * CSS选择器（基于jsoup）
     */
    CSS,

    /**
     * XPath表达式（基于jsoup原生selectXpath）
     */
    XPATH,

    /**
     * JSONPath表达式（基于jayway JsonPath），适用于接口返回JSON的站点
     */
    JSONPATH;

    /**
     * 解析方式字符串转枚举，空值或非法值一律回退为REGEX
     */
    public static CrawlParseType of(String parseType) {
        if (parseType == null || parseType.trim().isEmpty()) {
            return REGEX;
        }
        try {
            return valueOf(parseType.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return REGEX;
        }
    }
}

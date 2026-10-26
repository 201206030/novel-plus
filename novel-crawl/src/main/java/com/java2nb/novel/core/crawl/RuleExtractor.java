package com.java2nb.novel.core.crawl;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 爬虫内容提取器抽象基类
 * <p>
 * 针对一段抓取到的页面内容（HTML或JSON文本），按具体解析方式（正则/CSS/XPath/JSONPath）提取字段值。
 * <p>
 * 统一规则语法（对CSS/XPath/JSONPath生效）：
 * <ul>
 * <li>{@code 表达式}：默认提取元素文本</li>
 * <li>{@code 表达式::attr(名称)}：提取元素属性值，如 {@code #author::attr(href)}</li>
 * <li>{@code 表达式::html}：提取元素内部HTML（保留标签，适合正文/简介）</li>
 * <li>{@code 表达式::ownText}：只提取元素自身的直接文本（不含子孙文本）</li>
 * <li>{@code 表达式|后端正则}：先按表达式提取，再用正则的1号捕获组做二次加工，
 * 如 {@code dl dd a::attr(href)|(\d+)\.html} 从章节链接中截取章节ID</li>
 * <li>{@code 表达式##正则##替换}：先按表达式提取，再对每个值执行正则替换（替换段可省略，缺省为删除），
 * 兼容阅读3.0书源的{@code ##}过滤语法，如 {@code .content::html##[\(（]本章完[\)）]}</li>
 * <li>{@code xpath://a@href}、{@code css:.a::text}、{@code json:$.name}、{@code regex:...}：
 * 规则级解析方式前缀，单条规则显式指定解析方式，覆盖整源的parseType（导入混合语法书源时使用）</li>
 * </ul>
 *
 * @author xiongxiaoyang
 */
public abstract class RuleExtractor {

    /**
     * 待提取的页面内容
     */
    protected final String content;

    protected RuleExtractor(String content) {
        this.content = content;
    }

    /**
     * 按解析方式创建提取器
     */
    public static RuleExtractor create(String parseType, String content) {
        switch (CrawlParseType.of(parseType)) {
            case CSS:
                return new CssExtractor(content);
            case XPATH:
                return new XpathExtractor(content);
            case JSONPATH:
                return new JsonPathExtractor(content);
            default:
                return new RegexExtractor(content);
        }
    }

    /**
     * 提取所有匹配值（已按规则做完##替换与后端二次正则加工）
     */
    public List<String> extractAll(String rule) {
        if (StringUtils.isBlank(rule)) {
            return Collections.emptyList();
        }
        String remaining = rule.trim();
        //规则级解析方式前缀，如 xpath://a::attr(href)，命中时改用对应提取器解析本条规则
        RuleExtractor extractor = this;
        CrawlParseType prefixType = matchSyntaxPrefix(remaining);
        if (prefixType != null) {
            remaining = remaining.substring(remaining.indexOf(':') + 1);
            extractor = create(prefixType.name(), content);
        }
        //切分阅读风格的"##正则##替换"后处理（替换段可省略，缺省为空串即删除）
        String mainRule = remaining;
        String replaceRegex = null;
        String replacement = "";
        int hashIndex = remaining.indexOf("##");
        if (hashIndex >= 0) {
            mainRule = remaining.substring(0, hashIndex);
            String replacePart = remaining.substring(hashIndex + 2);
            int secondHashIndex = replacePart.indexOf("##");
            if (secondHashIndex >= 0) {
                replaceRegex = replacePart.substring(0, secondHashIndex);
                replacement = replacePart.substring(secondHashIndex + 2);
            } else {
                replaceRegex = replacePart;
            }
        }
        //拆分后端二次正则（'|'后部分），注意CSS属性选择器中的"|="不能误切
        String expression = mainRule;
        String postRegex = null;
        int pipeIndex = indexOfPostRegexSeparator(mainRule);
        if (pipeIndex >= 0) {
            expression = mainRule.substring(0, pipeIndex);
            postRegex = mainRule.substring(pipeIndex + 1);
        }
        List<String> values;
        try {
            values = extractor.doExtractAll(expression.trim());
        } catch (Exception e) {
            throw new IllegalArgumentException("规则解析失败[" + expression + "]：" + e.getMessage(), e);
        }
        if (StringUtils.isNotBlank(replaceRegex)) {
            values = applyReplace(values, replaceRegex, replacement);
        }
        if (StringUtils.isNotBlank(postRegex)) {
            values = applyPostRegex(values, postRegex.trim());
        }
        return values;
    }

    /**
     * 识别规则级解析方式前缀（xpath:/css:/json:/regex:），非法或无前缀返回null
     */
    private static CrawlParseType matchSyntaxPrefix(String rule) {
        int colonIndex = rule.indexOf(':');
        if (colonIndex <= 0) {
            return null;
        }
        String prefix = rule.substring(0, colonIndex).toLowerCase();
        switch (prefix) {
            case "xpath":
                return CrawlParseType.XPATH;
            case "css":
                return CrawlParseType.CSS;
            case "json":
                return CrawlParseType.JSONPATH;
            case "regex":
                return CrawlParseType.REGEX;
            default:
                return null;
        }
    }

    /**
     * 提取第一个匹配值，没有匹配时返回null
     */
    public String extractFirst(String rule) {
        List<String> values = extractAll(rule);
        return values.isEmpty() ? null : values.get(0);
    }

    /**
     * 具体解析方式的提取实现，返回原始匹配值
     */
    protected abstract List<String> doExtractAll(String expression);

    /**
     * 定位后端二次正则的分隔符'|'，CSS属性选择器中的"|="不是分隔符
     */
    private static int indexOfPostRegexSeparator(String rule) {
        for (int i = 0; i < rule.length(); i++) {
            if (rule.charAt(i) == '|' && (i + 1 >= rule.length() || rule.charAt(i + 1) != '=')) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 对每个提取值执行正则替换（阅读##语义），替换后为空白的值被丢弃
     */
    private static List<String> applyReplace(List<String> values, String regex, String replacement) {
        Pattern pattern = PatternFactory.getPattern(regex);
        List<String> result = new ArrayList<>();
        for (String value : values) {
            String replaced = pattern.matcher(value).replaceAll(replacement);
            if (StringUtils.isNotBlank(replaced)) {
                result.add(replaced);
            }
        }
        return result;
    }

    /**
     * 对每个提取值应用二次正则，取1号捕获组（无捕获组则取整个匹配），不匹配的值被丢弃
     */
    private static List<String> applyPostRegex(List<String> values, String regex) {
        Pattern pattern = PatternFactory.getPattern(regex);
        List<String> result = new ArrayList<>();
        for (String value : values) {
            Matcher matcher = pattern.matcher(value);
            if (matcher.find()) {
                String extracted = matcher.groupCount() >= 1 ? matcher.group(1) : matcher.group();
                if (StringUtils.isNotBlank(extracted)) {
                    result.add(extracted);
                }
            }
        }
        return result;
    }

    /**
     * 拆分"表达式::取值方式"，返回[表达式, 取值方式]，取值方式缺省为text
     */
    protected static String[] splitValueHint(String expression) {
        int index = expression.indexOf("::");
        if (index < 0) {
            return new String[]{expression, "text"};
        }
        return new String[]{expression.substring(0, index), expression.substring(index + 2).trim()};
    }
}

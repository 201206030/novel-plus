package com.java2nb.novel.core.crawl;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * HTML类提取器基类（CSS选择器 / XPath共用），负责HTML的懒解析与元素取值
 *
 * @author xiongxiaoyang
 */
public abstract class HtmlExtractor extends RuleExtractor {

    /**
     * 页面内容对应的DOM树，同一页面多次提取时复用，懒加载
     */
    private Document document;

    protected HtmlExtractor(String content) {
        super(content);
    }

    protected Document document() {
        if (document == null) {
            document = Jsoup.parse(content);
            //关闭美化输出，避免::html取值时被注入多余换行
            document.outputSettings().prettyPrint(false);
        }
        return document;
    }

    /**
     * 按取值方式从元素中提取值：text(默认) / ownText / html / attr(名称)
     */
    protected String valueOf(Element element, String valueHint) {
        String value;
        if (valueHint.startsWith("attr(") && valueHint.endsWith(")")) {
            value = element.attr(valueHint.substring(5, valueHint.length() - 1).trim());
        } else {
            switch (valueHint) {
                case "html":
                    value = element.html();
                    break;
                case "ownText":
                    value = element.ownText();
                    break;
                default:
                    value = element.text();
            }
        }
        return StringUtils.isBlank(value) ? null : value.trim();
    }

    /**
     * 收集非空值
     */
    protected static void addIfNotBlank(List<String> values, String value) {
        if (StringUtils.isNotBlank(value)) {
            values.add(value);
        }
    }

    protected static List<String> newArrayList() {
        return new ArrayList<>();
    }
}

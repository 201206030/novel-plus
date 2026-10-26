package com.java2nb.novel.core.crawl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式提取器（原有解析方式），提取每个匹配的1号捕获组（无捕获组时取整个匹配）
 *
 * @author xiongxiaoyang
 */
public class RegexExtractor extends RuleExtractor {

    public RegexExtractor(String content) {
        super(content);
    }

    @Override
    protected List<String> doExtractAll(String expression) {
        List<String> values = new ArrayList<>();
        Pattern pattern = PatternFactory.getPattern(expression);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            String value = matcher.groupCount() >= 1 ? matcher.group(1) : matcher.group();
            if (value != null) {
                values.add(value);
            }
        }
        return values;
    }
}

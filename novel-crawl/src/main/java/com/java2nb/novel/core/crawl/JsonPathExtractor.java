package com.java2nb.novel.core.crawl;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.PathNotFoundException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * JSONPath提取器（jayway JsonPath），适用于站点接口返回JSON的场景，规则示例：
 * <ul>
 * <li>{@code $.data.bookName}：取单个字段</li>
 * <li>{@code $.data.chapters[*].name}：通配符取全部章节名</li>
 * <li>{@code $.data.chapters[*].url|/(\d+)\.html}：取出后二次正则截取章节ID</li>
 * <li>{@code $.data.bookDesc}：对象/数组结果会被序列化为JSON字符串返回</li>
 * </ul>
 *
 * @author xiongxiaoyang
 */
public class JsonPathExtractor extends RuleExtractor {

    private static final Configuration CONF = Configuration.builder()
        .options(Option.SUPPRESS_EXCEPTIONS)
        .build();

    public JsonPathExtractor(String content) {
        super(content);
    }

    @Override
    protected List<String> doExtractAll(String expression) {
        Object result;
        try {
            result = JsonPath.using(CONF).parse(content).read(expression);
        } catch (PathNotFoundException e) {
            return Collections.emptyList();
        }
        if (result == null) {
            return Collections.emptyList();
        }
        List<String> values = new ArrayList<>();
        if (result instanceof List) {
            for (Object item : (List<?>) result) {
                addValue(values, item);
            }
        } else {
            addValue(values, result);
        }
        return values;
    }

    private static void addValue(List<String> values, Object item) {
        if (item == null) {
            return;
        }
        String value;
        if (item instanceof CharSequence || item instanceof Number || item instanceof Boolean) {
            value = String.valueOf(item);
        } else {
            //对象或数组结果（json-smart的JSONObject/JSONArray）toString即为JSON文本
            value = item.toString();
        }
        value = value.trim();
        if (!value.isEmpty()) {
            values.add(value);
        }
    }
}

package com.java2nb.novel.core.crawl;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * CSS选择器提取器，规则示例：
 * <ul>
 * <li>{@code #bookName}：取文本</li>
 * <li>{@code a.author::attr(href)}：取作者链接</li>
 * <li>{@code dl dd a::attr(href)|/(\d+)\.html}：取链接后二次正则截取章节ID</li>
 * <li>{@code #content::html}：取正文内部HTML</li>
 * <li>{@code #list:eq(0) a}：{@code :eq(n)}按"第n个匹配元素"理解（jsoup原生语义是"父节点的第n个子节点"，
 * 与阅读DSL的索引含义不一致，这里已重定义），不带:eq的选择器仍走jsoup原生解析</li>
 * </ul>
 *
 * @author xiongxiaoyang
 */
public class CssExtractor extends HtmlExtractor {

    /**
     * 选择器片段末尾的:eq(n)
     */
    private static final Pattern EQ_TAIL = Pattern.compile(":eq\\((-?\\d+)\\)$");

    public CssExtractor(String content) {
        super(content);
    }

    @Override
    protected List<String> doExtractAll(String expression) {
        String[] split = splitValueHint(expression);
        Elements elements = selectWithEq(document(), split[0]);
        List<String> values = newArrayList();
        for (Element element : elements) {
            addIfNotBlank(values, valueOf(element, split[1]));
        }
        return values;
    }

    /**
     * 按"第n个匹配"语义执行带:eq(n)的CSS选择器；选择器不含:eq(时直接交给jsoup。
     * 仅支持空格分隔的后代组合器（属性选择器内部空格已做保护），混用>、+等组合器时:eq不生效
     */
    private Elements selectWithEq(Element root, String selector) {
        if (!selector.contains(":eq(")) {
            return root.select(selector);
        }
        Elements context = null;
        for (String part : splitComponents(selector)) {
            Matcher m = EQ_TAIL.matcher(part);
            int index = -1;
            String base = part;
            if (m.find()) {
                index = Integer.parseInt(m.group(1));
                base = part.substring(0, m.start());
            }
            if (base.isEmpty()) {
                return new Elements();
            }
            Elements matched = context == null ? root.select(base) : context.select(base);
            if (index >= 0) {
                if (index >= matched.size()) {
                    return new Elements();
                }
                Elements single = new Elements();
                single.add(matched.get(index));
                matched = single;
            }
            context = matched;
        }
        return context == null ? new Elements() : context;
    }

    /**
     * 按空格把选择器拆成后代组合的片段，[]与引号内部的空格不拆分
     */
    private List<String> splitComponents(String selector) {
        List<String> parts = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        int depth = 0;
        char quote = 0;
        for (char c : selector.toCharArray()) {
            if (quote != 0) {
                sb.append(c);
                if (c == quote) {
                    quote = 0;
                }
            } else if (c == '\'' || c == '"') {
                quote = c;
                sb.append(c);
            } else if (c == '[') {
                depth++;
                sb.append(c);
            } else if (c == ']') {
                depth--;
                sb.append(c);
            } else if (Character.isWhitespace(c) && depth == 0) {
                if (sb.length() > 0) {
                    parts.add(sb.toString());
                    sb.setLength(0);
                }
            } else {
                sb.append(c);
            }
        }
        if (sb.length() > 0) {
            parts.add(sb.toString());
        }
        return parts;
    }
}

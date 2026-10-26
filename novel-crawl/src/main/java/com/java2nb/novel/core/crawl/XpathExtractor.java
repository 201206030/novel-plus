package com.java2nb.novel.core.crawl;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.util.List;

/**
 * XPath提取器（jsoup原生selectXpath），规则示例：
 * <ul>
 * <li>{@code //h1[@class='bookname']}：取文本</li>
 * <li>{@code //a[@id='author']}：取作者链接（也可写 {@code //a[@id='author']/text()}）</li>
 * <li>{@code //div[@id='list']//dd/a::attr(href)|/(\d+)\.html}：取链接后二次正则截取章节ID</li>
 * <li>{@code //div[@id='content']::html}：取正文内部HTML</li>
 * </ul>
 *
 * @author xiongxiaoyang
 */
public class XpathExtractor extends HtmlExtractor {

    public XpathExtractor(String content) {
        super(content);
    }

    @Override
    protected List<String> doExtractAll(String expression) {
        String[] split = splitValueHint(expression);
        //用Node类型接收，兼容XPath返回文本节点/属性值等非Element结果
        List<Node> nodes = document().selectXpath(split[0], Node.class);
        List<String> values = newArrayList();
        for (Node node : nodes) {
            if (node instanceof Element) {
                addIfNotBlank(values, valueOf((Element) node, split[1]));
            } else if (node instanceof TextNode) {
                //text()/@attr等表达式返回的文本节点，取值方式固定为文本
                addIfNotBlank(values, ((TextNode) node).getWholeText().trim());
            } else {
                addIfNotBlank(values, node.toString().trim());
            }
        }
        return values;
    }
}

package com.java2nb.novel.core.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.java2nb.novel.core.enums.ResponseStatus;
import io.github.xxyopen.web.exception.BusinessException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * AI 规则生成工具：模型输出的 JSON 提取/白名单过滤，以及示例页面 HTML 清洗。
 *
 * <p>模型输出不保证是合法 JSON（可能带解释文字或 markdown 围栏），统一在此防御解析；
 * 只允许白名单内的规则字段回填，避免模型臆造字段污染入库的 crawlRule。</p>
 *
 * @author xiongxiaoyang
 */
public final class AiRuleUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 允许 AI 生成并回填的规则字段（与 crawlSource_add.html 的输入框 id 一致）。
     * 分类映射 catIdRule 依赖系统动态分类，不由 AI 生成；parseType 由用户在面板选定。
     */
    public static final Set<String> ALLOWED_RULE_KEYS = Set.of(
        "bookListUrl",
        "bookIdPatten", "pagePatten", "totalPagePatten",
        "bookDetailUrl", "bookNamePatten", "authorNamePatten", "picUrlPatten", "picUrlPrefix",
        "statusPatten", "bookStatus0", "bookStatus1", "scorePatten", "visitCountPatten",
        "descStart", "descEnd", "descPatten", "filterDesc",
        "upadateTimePatten", "upadateTimeFormatPatten",
        "bookIndexUrl", "bookIndexStart", "indexIdPatten", "indexNamePatten",
        "bookContentUrl", "contentStart", "contentEnd", "contentPatten", "filterContent"
    );

    /** 单个字段值上限，防止模型输出异常长文本 */
    private static final int MAX_FIELD_CHARS = 2000;

    /**
     * URL 模板字段：这些字段里的占位符必须被规范化。
     * 系统只识别 {bookId}/{indexId}/{catId}/{page}，模型偶尔会臆造 {bookId1}/{bookId2} 之类带编号变体，
     * 入库前统一收敛为不带编号的原样占位符，避免生成无法替换的错误 URL。
     */
    private static final Set<String> URL_TEMPLATE_KEYS = Set.of(
        "bookListUrl", "bookDetailUrl", "bookIndexUrl", "bookContentUrl"
    );

    /** 匹配 {bookId1}、{indexId2} 等带数字后缀的非法占位符 */
    private static final Pattern NUMBERED_PLACEHOLDER =
        Pattern.compile("\\{(bookId|indexId|catId|page)\\d+\\}");

    private static final Pattern SCRIPT = Pattern.compile("(?is)<script\\b[^>]*>.*?</script\\s*>");
    private static final Pattern STYLE = Pattern.compile("(?is)<style\\b[^>]*>.*?</style\\s*>");
    private static final Pattern COMMENT = Pattern.compile("(?s)<!--.*?-->");

    private AiRuleUtil() {
    }

    /**
     * 从模型输出中提取规则 JSON，按白名单过滤后返回，保留模型给出的字段顺序。
     * 若没有任何合法字段，视为生成失败。
     */
    public static Map<String, String> parseRules(String raw) {
        if (raw == null || raw.isBlank()) {
            throw new BusinessException(ResponseStatus.AI_GEN_FAILED);
        }
        String cleaned = stripCodeFence(raw);
        int start = cleaned.indexOf('{');
        int end = cleaned.lastIndexOf('}');
        if (start < 0 || end <= start) {
            throw new BusinessException(ResponseStatus.AI_GEN_FAILED);
        }
        JsonNode node;
        try {
            node = MAPPER.readTree(cleaned.substring(start, end + 1));
        } catch (Exception e) {
            throw new BusinessException(ResponseStatus.AI_GEN_FAILED);
        }
        if (node == null || !node.isObject()) {
            throw new BusinessException(ResponseStatus.AI_GEN_FAILED);
        }
        Map<String, String> rules = new LinkedHashMap<>();
        node.fields().forEachRemaining(entry -> {
            String key = entry.getKey();
            JsonNode value = entry.getValue();
            if (!ALLOWED_RULE_KEYS.contains(key) || value == null || value.isNull() || value.isContainerNode()) {
                return;
            }
            String text = value.asText("").trim();
            if (!text.isEmpty()) {
                if (text.length() > MAX_FIELD_CHARS) {
                    text = text.substring(0, MAX_FIELD_CHARS);
                }
                if (URL_TEMPLATE_KEYS.contains(key)) {
                    text = canonicalizePlaceholders(text);
                }
                rules.put(key, text);
            }
        });
        if (rules.isEmpty()) {
            throw new BusinessException(ResponseStatus.AI_GEN_FAILED);
        }
        return rules;
    }

    /**
     * 把带编号的非法占位符收敛为系统识别的原样形式。
     * 如 {bookId1}/{bookId2} -> {bookId}/{bookId}（由提示词约束模型只输出单个 {bookId}，
     * 这里做兜底，避免任何 {xxx数字} 逃逸到入库规则中）。
     */
    private static String canonicalizePlaceholders(String text) {
        java.util.regex.Matcher m = NUMBERED_PLACEHOLDER.matcher(text);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, "{" + m.group(1) + "}");
        }
        m.appendTail(sb);
        return sb.toString();
    }

    /**
     * 去掉 markdown 代码围栏
     */
    private static String stripCodeFence(String text) {
        String cleaned = text.trim();
        if (cleaned.startsWith("```")) {
            int firstLineEnd = cleaned.indexOf('\n');
            if (firstLineEnd > 0) {
                cleaned = cleaned.substring(firstLineEnd + 1);
            }
            int fenceEnd = cleaned.lastIndexOf("```");
            if (fenceEnd >= 0) {
                cleaned = cleaned.substring(0, fenceEnd);
            }
        }
        return cleaned.trim();
    }

    /**
     * 清洗示例页面 HTML：剔除脚本/样式/注释噪声，再按上限截断。
     * 保留标签与属性原样（AI 需要据此写正则/CSS/XPath），不做空白折叠。
     */
    public static String cleanHtml(String html, int maxChars) {
        if (html == null || html.isEmpty()) {
            return "";
        }
        String cleaned = COMMENT.matcher(html).replaceAll(" ");
        cleaned = SCRIPT.matcher(cleaned).replaceAll(" ");
        cleaned = STYLE.matcher(cleaned).replaceAll(" ");
        if (cleaned.length() <= maxChars) {
            return cleaned;
        }
        return cleaned.substring(0, maxChars) + "\n……（HTML 已截断）";
    }
}

package com.java2nb.novel.core.ai;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * AI 提示词模板工具：从 classpath:prompts/ai-assistant/ 加载模板并按 {key} 占位符渲染
 *
 * @author 11797
 */
@Slf4j
public final class PromptTemplateUtil {

    private static final String TEMPLATE_DIR = "prompts/ai-assistant/";

    private static final Map<String, String> TEMPLATE_CACHE = new ConcurrentHashMap<>();

    /**
     * 占位符缺省值
     */
    private static final String DEFAULT_VALUE = "未提供";

    /**
     * 占位符格式：{标识符}，不会误匹配 JSON 示例中 {"key":... 形式的花括号
     */
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{([a-zA-Z][a-zA-Z0-9_]*)\\}");

    private PromptTemplateUtil() {
    }

    /**
     * 加载模板内容（带缓存）
     */
    public static String load(String key) {
        return TEMPLATE_CACHE.computeIfAbsent(key, k -> {
            try (InputStream is = new ClassPathResource(TEMPLATE_DIR + k + ".txt").getInputStream()) {
                return new String(is.readAllBytes(), StandardCharsets.UTF_8);
            } catch (Exception e) {
                log.error("加载AI提示词模板失败: {}", k, e);
                throw new IllegalStateException("提示词模板缺失: " + k);
            }
        });
    }

    /**
     * 渲染模板，将 {key} 占位符替换为变量值，缺省变量填充为“未提供”
     */
    public static String render(String key, Map<String, String> vars) {
        String template = load(key);
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(template);
        StringBuilder sb = new StringBuilder();
        // 单次扫描原模板：未提供的变量填充为缺省值，已替换内容不再参与匹配，防止变量值含占位符时死循环
        while (matcher.find()) {
            String value = vars.get(matcher.group(1));
            if (value == null || value.isBlank()) {
                value = DEFAULT_VALUE;
            }
            matcher.appendReplacement(sb, Matcher.quoteReplacement(value));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}

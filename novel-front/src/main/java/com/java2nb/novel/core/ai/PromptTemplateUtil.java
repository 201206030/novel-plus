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
        return load(TEMPLATE_DIR, key);
    }

    /**
     * 加载指定目录下的模板内容（带缓存，缓存键包含目录避免重名冲突）
     *
     * @param dir 相对 classpath 的目录，需以 / 结尾，如 {@code prompts/author-writing/}
     */
    public static String load(String dir, String key) {
        String cacheKey = dir + key;
        return TEMPLATE_CACHE.computeIfAbsent(cacheKey, k -> {
            try (InputStream is = new ClassPathResource(dir + key + ".txt").getInputStream()) {
                return new String(is.readAllBytes(), StandardCharsets.UTF_8);
            } catch (Exception e) {
                log.error("加载AI提示词模板失败: {}{}", dir, key, e);
                throw new IllegalStateException("提示词模板缺失: " + dir + key);
            }
        });
    }

    /**
     * 渲染默认目录下的模板，将 {key} 占位符替换为变量值，缺省变量填充为“未提供”
     */
    public static String render(String key, Map<String, String> vars) {
        return render(TEMPLATE_DIR, key, vars);
    }

    /**
     * 渲染指定目录下的模板。单次扫描原模板，已替换内容不再参与匹配，
     * 因此变量值里含有 {xxx} 或 $ 等字符也不会被二次解析或破坏。
     */
    public static String render(String dir, String key, Map<String, String> vars) {
        String template = load(dir, key);
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(template);
        StringBuilder sb = new StringBuilder();
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

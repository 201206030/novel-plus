package com.java2nb.novel.core.ai;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 章节发布页的 AI 阈值链路静态回归：
 * 模板里的 Thymeleaf 属性名必须与 {@code PageController.module2} 注入的一致，
 * 且前端校验必须引用注入值而不是另写一份硬编码阈值。
 *
 * <p>注：运行时 SpEL 渲染需要完整 Spring ApplicationContext，这里只做结构校验，
 * 实际数值由 PageController 注入，模板同时保留静态预览默认值。</p>
 */
class ContentAddAiLimitsTemplateTest {

    private static final Pattern INLINE_SCRIPT = Pattern.compile(
        "<script[^>]*th:inline=\"javascript\"[^>]*>(.*?)</script>", Pattern.DOTALL);

    private String template() throws Exception {
        return new ClassPathResource("templates/author/content_add.html")
            .getContentAsString(StandardCharsets.UTF_8);
    }

    @Test
    void allFourLimitsAreInjectedFromBackend() throws Exception {
        String html = template();
        Matcher matcher = INLINE_SCRIPT.matcher(html);
        assertTrue(matcher.find(), "模板里应存在 th:inline=javascript 的配置块");
        String script = matcher.group(1);

        // 属性名必须与 PageController.module2 中 model.addAttribute 的键完全一致
        assertTrue(script.contains("${aiMaxTextLength}"), script);
        assertTrue(script.contains("${aiMaxExpandRatio}"), script);
        assertTrue(script.contains("${aiMaxCondenseRatio}"), script);
        assertTrue(script.contains("${aiMaxContinueLength}"), script);
    }

    @Test
    void validationActuallyUsesInjectedLimits() throws Exception {
        String html = template();
        // 四类校验都用 AI_LIMITS，而不是另写一份硬编码数字
        assertTrue(html.contains("selectedText.length > AI_LIMITS.maxTextLength"));
        assertTrue(html.contains("ratio > AI_LIMITS.maxExpandRatio"));
        assertTrue(html.contains("ratio > AI_LIMITS.maxCondenseRatio"));
        assertTrue(html.contains("num > AI_LIMITS.maxContinueLength"));
        // 不应残留旧的裸 parse* 传参（condense 的 ratio 是 Integer，必须取整）
        assertFalse(html.contains("parseFloat(value) * 100"), "ratio 必须用 Math.round 归一为整数");
    }

    @Test
    void staticPreviewKeepsDefaultValuesAndGuardsAgainstNull() throws Exception {
        Matcher matcher = INLINE_SCRIPT.matcher(template());
        assertTrue(matcher.find());
        String script = matcher.group(1);
        // 注释右侧的默认值保证静态预览时校验仍可用
        assertTrue(script.contains("/*[[${aiMaxTextLength}]]*/ 5000"), script);
        assertTrue(script.contains("/*[[${aiMaxExpandRatio}]]*/ 400"), script);
        assertTrue(script.contains("/*[[${aiMaxCondenseRatio}]]*/ 100"), script);
        assertTrue(script.contains("/*[[${aiMaxContinueLength}]]*/ 3000"), script);
        // 属性缺失时回退，避免校验拿到 null 静默失效
        assertTrue(script.contains("Number(AI_LIMITS.maxTextLength) || 5000"), script);
    }
}

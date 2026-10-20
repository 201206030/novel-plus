package com.java2nb.novel.core.ai;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 提示词模板加载与渲染测试
 */
class PromptTemplateUtilTest {

    private static final String[] ALL_TEMPLATES = {
            "book_name", "creative_plan", "chapter_plan_batch",
            "chapter_content", "chapter_check", "chapter_revise", "chapter_summary"};

    @Test
    void load_shouldFindAllTemplatesOnClasspath() {
        for (String key : ALL_TEMPLATES) {
            String tpl = PromptTemplateUtil.load(key);
            assertNotNull(tpl);
            assertFalse(tpl.isBlank(), "模板不应为空: " + key);
        }
    }

    @Test
    void render_shouldReplacePlaceholders() {
        Map<String, String> vars = new HashMap<>();
        vars.put("genre", "玄幻");
        String rendered = PromptTemplateUtil.render("book_name", vars);
        assertTrue(rendered.contains("题材：玄幻"));
        assertFalse(rendered.contains("{genre}"));
    }

    @Test
    void render_shouldFillMissingVarWithDefault() {
        Map<String, String> vars = new HashMap<>();
        String rendered = PromptTemplateUtil.render("book_name", vars);
        assertTrue(rendered.contains("题材：未提供"));
    }

    @Test
    void render_shouldNotLoopOnSelfReferenceValue() {
        //变量值本身包含占位符时不应死循环或二次替换
        Map<String, String> vars = new HashMap<>();
        vars.put("coreIdea", "这是一个含{coreIdea}字样的创意");
        String rendered = assertTimeout(java.time.Duration.ofSeconds(5),
                () -> PromptTemplateUtil.render("book_name", vars));
        assertTrue(rendered.contains("这是一个含{coreIdea}字样的创意"));
    }

    @Test
    void render_shouldKeepUnrelatedBraces() {
        //输出格式示例中的JSON花括号不应被破坏
        Map<String, String> vars = new HashMap<>();
        vars.put("genre", "都市");
        String rendered = PromptTemplateUtil.render("book_name", vars);
        assertTrue(rendered.contains("{\"names\":"));
    }
}

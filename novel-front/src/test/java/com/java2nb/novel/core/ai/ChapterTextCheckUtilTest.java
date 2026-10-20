package com.java2nb.novel.core.ai;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 章节正文程序化校验测试
 */
class ChapterTextCheckUtilTest {

    @Test
    void validWordCount_shouldMatchAuthorRule() {
        //与作者后台 StringUtil.getStrValidWordCount 规则一致：中文按字、英文按词、数字按组
        assertEquals(5, ChapterTextCheckUtil.validWordCount("你好呀世界"));
        assertEquals(0, ChapterTextCheckUtil.validWordCount(null));
    }

    @Test
    void check_shouldFlagEmptyContent() {
        assertTrue(ChapterTextCheckUtil.check("", 3000).contains("章节正文为空"));
        assertTrue(ChapterTextCheckUtil.check(null, 3000).contains("章节正文为空"));
    }

    @Test
    void check_shouldFlagTooShort() {
        List<String> problems = ChapterTextCheckUtil.check("很短的一段", 3000);
        assertFalse(problems.isEmpty());
        assertTrue(problems.get(0).contains("明显不足"));
    }

    @Test
    void check_shouldFlagBelowMinRatio() {
        //1000字 < 3000*0.7
        String content = "字".repeat(1000);
        List<String> problems = ChapterTextCheckUtil.check(content, 3000);
        assertTrue(problems.stream().anyMatch(p -> p.contains("低于目标")));
    }

    @Test
    void check_shouldPassNormalLength() {
        String content = "字".repeat(2500);
        List<String> problems = ChapterTextCheckUtil.check(content, 3000);
        assertTrue(problems.isEmpty(), "2500字相对目标3000字应在允许范围: " + problems);
    }

    @Test
    void check_shouldFlagOverMaxRatio() {
        String content = "字".repeat(5000);
        List<String> problems = ChapterTextCheckUtil.check(content, 3000);
        assertTrue(problems.stream().anyMatch(p -> p.contains("注水嫌疑")));
    }

    @Test
    void check_shouldFlagDuplicateParagraphs() {
        String dup = "他缓缓抬起头，目光穿过人群，落在了那柄旧剑上。";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(dup).append('\n');
        }
        //补足字数避免先触发字数问题
        sb.append("续".repeat(2500));
        List<String> problems = ChapterTextCheckUtil.check(sb.toString(), 2600);
        assertTrue(problems.stream().anyMatch(p -> p.contains("重复段落")));
    }

    @Test
    void fallbackSummary_shouldStripTitleAndLimitLength() {
        String content = "第一章 初入江湖\n" + "少年负剑出门，" .repeat(50);
        String summary = ChapterTextCheckUtil.fallbackSummary(content);
        assertFalse(summary.startsWith("第一章"));
        assertTrue(summary.length() <= 123);
    }
}

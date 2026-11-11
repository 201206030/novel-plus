package com.java2nb.novel.core.ai;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * AI 写作输出清洗：说明块截断、流式增量清洗、不误伤正文。
 */
class AiTextSanitizerTest {

    private static final String NOVEL = "夜色沉了下来。\n他推开那扇旧木门，屋里还亮着一盏灯。";

    @Test
    void stripsTypicalExplanationBlock() {
        String raw = NOVEL + "\n---\n**改写说明**：\n"
            + "-**延续了原始语气，保持礼貌友好**\n"
            + "如果你有特定的场景，也可以告诉我，我可以为你进一步调整。";
        assertEquals(NOVEL, AiTextSanitizer.clean(raw));
    }

    @Test
    void stripsStandaloneHeadingAndAdvice() {
        assertEquals(NOVEL, AiTextSanitizer.clean(NOVEL + "\n改写说明：\n- 语气保持一致"));
        assertEquals(NOVEL, AiTextSanitizer.clean(NOVEL + "\n希望这些修改对你有帮助"));
        assertEquals(NOVEL, AiTextSanitizer.clean(NOVEL + "\n**创作思路**\n主要是调整了节奏"));
    }

    @Test
    void stripsLeadInAndCodeFence() {
        String raw = "```text\n以下是润色后的文本：\n" + NOVEL + "\n```";
        assertEquals(NOVEL, AiTextSanitizer.clean(raw));
    }

    @Test
    void keepsSceneSeparatorInsideBody() {
        String raw = NOVEL + "\n---\n三年后，他又回到了这里。";
        String cleaned = AiTextSanitizer.clean(raw);
        assertTrue(cleaned.contains("三年后，他又回到了这里"), "正文里的场景分隔线不应被当作说明块");
        assertFalse(cleaned.contains("改写说明"));
    }

    @Test
    void cleanBodyIsUntouched() {
        assertEquals(NOVEL, AiTextSanitizer.clean(NOVEL));
    }

    @Test
    void streamCutsExplanationEvenWhenSplitIntoSingleChars() {
        String full = NOVEL + "\n---\n**改写说明**：\n- 语气保持一致\n如果你有特定的场景，也可以告诉我。";
        List<String> chunks = new ArrayList<>();
        for (char c : full.toCharArray()) {
            chunks.add(String.valueOf(c));
        }
        List<String> out = AiTextSanitizer.cleanStream(Flux.fromIterable(chunks))
            .collectList().block();
        String joined = String.join("", out == null ? List.of() : out).stripTrailing();
        assertEquals(NOVEL, joined);
    }

    @Test
    void streamKeepsCleanBodyIntact() {
        List<String> out = AiTextSanitizer.cleanStream(
            Flux.just("夜色沉了下来。", "\n他推开那扇旧木门，", "屋里还亮着一盏灯。"))
            .collectList().block();
        assertEquals(NOVEL, String.join("", out == null ? List.of() : out));
    }

    @Test
    void eachSubscriberHasIndependentState() {
        Flux<String> shared = AiTextSanitizer.cleanStream(Flux.just(NOVEL));
        assertEquals(NOVEL, String.join("", shared.collectList().block()));
        assertEquals(NOVEL, String.join("", shared.collectList().block()));
    }
}

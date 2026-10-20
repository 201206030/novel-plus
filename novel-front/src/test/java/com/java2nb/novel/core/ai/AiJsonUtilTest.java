package com.java2nb.novel.core.ai;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.xxyopen.web.exception.BusinessException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * AI输出JSON防御性解析与结构校验测试
 */
class AiJsonUtilTest {

    @Test
    void parseJsonFromModel_shouldParsePureJson() {
        JsonNode node = AiJsonUtil.parseJsonFromModel("{\"a\":1}");
        assertEquals(1, node.path("a").asInt());
    }

    @Test
    void parseJsonFromModel_shouldStripCodeFence() {
        String raw = "```json\n{\"names\":[{\"name\":\"测试\",\"reason\":\"r\"}]}\n```";
        JsonNode node = AiJsonUtil.parseJsonFromModel(raw);
        assertEquals("测试", node.path("names").get(0).path("name").asText());
    }

    @Test
    void parseJsonFromModel_shouldExtractFromNoisyText() {
        String raw = "好的，以下是结果：\n{\"passed\":true,\"issues\":[]}\n以上。";
        JsonNode node = AiJsonUtil.parseJsonFromModel(raw);
        assertTrue(node.path("passed").asBoolean());
    }

    @Test
    void parseJsonFromModel_shouldThrowOnInvalidJson() {
        assertThrows(BusinessException.class, () -> AiJsonUtil.parseJsonFromModel("这不是JSON"));
        assertThrows(BusinessException.class, () -> AiJsonUtil.parseJsonFromModel(""));
        assertThrows(BusinessException.class, () -> AiJsonUtil.parseJsonFromModel(null));
        assertThrows(BusinessException.class, () -> AiJsonUtil.parseJsonFromModel("{\"broken\":"));
    }

    @Test
    void validatePlan_shouldAcceptCompletePlan() {
        String json = "{\"intro\":\"简介\",\"characters\":[{\"name\":\"甲\"}],"
                + "\"mainOutline\":{\"opening\":\"o\"},"
                + "\"chapters\":[{\"chapterNo\":1,\"indexName\":\"开端\"}]}";
        JsonNode node = AiJsonUtil.parseJsonFromModel(json);
        assertDoesNotThrow(() -> AiJsonUtil.validatePlan(node));
    }

    @Test
    void validatePlan_shouldRejectMissingParts() {
        // 缺章节规划
        String noChapters = "{\"intro\":\"简介\",\"characters\":[],\"mainOutline\":{},\"chapters\":[]}";
        assertThrows(BusinessException.class,
                () -> AiJsonUtil.validatePlan(AiJsonUtil.parseJsonFromModel(noChapters)));
        // 章节缺标题
        String noTitle = "{\"intro\":\"简介\",\"characters\":[{}],\"mainOutline\":{},"
                + "\"chapters\":[{\"chapterNo\":1}]}";
        assertThrows(BusinessException.class,
                () -> AiJsonUtil.validatePlan(AiJsonUtil.parseJsonFromModel(noTitle)));
        // 章节号非法
        String badNo = "{\"intro\":\"简介\",\"characters\":[{}],\"mainOutline\":{},"
                + "\"chapters\":[{\"chapterNo\":0,\"indexName\":\"x\"}]}";
        assertThrows(BusinessException.class,
                () -> AiJsonUtil.validatePlan(AiJsonUtil.parseJsonFromModel(badNo)));
    }

    @Test
    void validateNames_shouldRejectEmptyNames() {
        assertThrows(BusinessException.class,
                () -> AiJsonUtil.validateNames(AiJsonUtil.parseJsonFromModel("{\"names\":[]}")));
        assertDoesNotThrow(() -> AiJsonUtil.validateNames(
                AiJsonUtil.parseJsonFromModel("{\"names\":[{\"name\":\"书\"}]}")));
    }

    @Test
    void truncate_shouldCutLongText() {
        assertEquals("", AiJsonUtil.truncate(null, 10));
        assertEquals("abc", AiJsonUtil.truncate("abc", 10));
        String longText = "a".repeat(100);
        String cut = AiJsonUtil.truncate(longText, 10);
        assertTrue(cut.startsWith("aaaaaaaaaa"));
        assertTrue(cut.contains("已截断"));
    }
}

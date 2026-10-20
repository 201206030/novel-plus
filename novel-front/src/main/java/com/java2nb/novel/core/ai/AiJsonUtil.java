package com.java2nb.novel.core.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.java2nb.novel.core.enums.ResponseStatus;
import io.github.xxyopen.web.exception.BusinessException;

/**
 * AI 输出 JSON 提取与结构校验工具。
 * 模型输出不保证是合法 JSON（可能带解释文本或 markdown 围栏），统一在此处防御解析。
 *
 * @author 11797
 */
public final class AiJsonUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private AiJsonUtil() {
    }

    /**
     * 从模型输出中提取 JSON 对象并解析，失败抛出业务异常
     */
    public static JsonNode parseJsonFromModel(String raw) {
        if (raw == null || raw.isBlank()) {
            throw new BusinessException(ResponseStatus.AI_GEN_FAILED);
        }
        String cleaned = stripCodeFence(raw);
        int start = cleaned.indexOf('{');
        int end = cleaned.lastIndexOf('}');
        if (start < 0 || end <= start) {
            throw new BusinessException(ResponseStatus.AI_GEN_FAILED);
        }
        try {
            return MAPPER.readTree(cleaned.substring(start, end + 1));
        } catch (Exception e) {
            throw new BusinessException(ResponseStatus.AI_GEN_FAILED);
        }
    }

    /**
     * 校验书名建议结果：names 数组非空且每项含 name
     */
    public static void validateNames(JsonNode node) {
        JsonNode names = node.path("names");
        if (!names.isArray() || names.isEmpty()) {
            throw new BusinessException(ResponseStatus.AI_GEN_FAILED);
        }
        for (JsonNode item : names) {
            requireText(item, "name");
        }
    }

    /**
     * 校验创作方案结构：简介、人物档案、总纲、章节规划必须存在且合法
     */
    public static void validatePlan(JsonNode node) {
        requireText(node, "intro");
        JsonNode characters = node.path("characters");
        if (!characters.isArray() || characters.isEmpty()) {
            throw new BusinessException(ResponseStatus.AI_PLAN_INVALID);
        }
        JsonNode mainOutline = node.path("mainOutline");
        if (!mainOutline.isObject()) {
            throw new BusinessException(ResponseStatus.AI_PLAN_INVALID);
        }
        validateChapters(node);
    }

    /**
     * 校验章节规划数组：非空且每项含正整数 chapterNo 与标题
     */
    public static void validateChapters(JsonNode node) {
        JsonNode chapters = node.path("chapters");
        if (!chapters.isArray() || chapters.isEmpty()) {
            throw new BusinessException(ResponseStatus.AI_PLAN_INVALID);
        }
        for (JsonNode chapter : chapters) {
            if (chapter.path("chapterNo").asInt(0) <= 0) {
                throw new BusinessException(ResponseStatus.AI_PLAN_INVALID);
            }
            requireText(chapter, "indexName");
        }
    }

    /**
     * 校验章节检查结果结构：passed 布尔 + issues 数组
     */
    public static void validateCheck(JsonNode node) {
        if (!node.has("passed") || !node.path("issues").isArray()) {
            throw new BusinessException(ResponseStatus.AI_GEN_FAILED);
        }
    }

    /**
     * 必填文本字段校验
     */
    public static void requireText(JsonNode node, String field) {
        if (node.path(field).asText("").isBlank()) {
            throw new BusinessException(ResponseStatus.AI_PLAN_INVALID);
        }
    }

    /**
     * 去掉 markdown 代码围栏
     */
    public static String stripCodeFence(String text) {
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
     * 长文本截断（控制发送给模型的上下文规模）
     */
    public static String truncate(String text, int maxLen) {
        if (text == null || text.isBlank()) {
            return "";
        }
        if (text.length() <= maxLen) {
            return text;
        }
        return text.substring(0, maxLen) + "……（已截断）";
    }
}

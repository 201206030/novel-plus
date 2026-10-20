package com.java2nb.novel.core.ai;

import com.java2nb.novel.core.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 章节正文程序化校验：字数统计规则与作者后台发布章节保持一致（StringUtil.getStrValidWordCount），
 * 并做轻量化的重复段落/空内容检测。程序校验只兜底明显问题，质量判断交由 AI 校验。
 *
 * @author 11797
 */
public final class ChapterTextCheckUtil {

    /**
     * 字数达标下限比例（低于目标字数70%视为问题）
     */
    public static final double MIN_RATIO = 0.7;

    /**
     * 字数达标上限比例（高于目标字数150%视为注水嫌疑）
     */
    public static final double MAX_RATIO = 1.5;

    /**
     * 参与重复检测的最小段落长度
     */
    private static final int MIN_PARAGRAPH_LEN = 15;

    /**
     * 重复段落占比告警阈值
     */
    private static final double DUP_RATIO_THRESHOLD = 0.12;

    private ChapterTextCheckUtil() {
    }

    /**
     * 有效字数统计（与作者后台章节字数规则一致）
     */
    public static int validWordCount(String content) {
        return StringUtil.getStrValidWordCount(content == null ? "" : content);
    }

    /**
     * 程序化检查，返回问题列表；列表为空表示程序检查通过
     */
    public static List<String> check(String content, int targetWords) {
        List<String> problems = new ArrayList<>();
        if (content == null || content.isBlank()) {
            problems.add("章节正文为空");
            return problems;
        }
        int wordCount = validWordCount(content);
        if (wordCount < 200) {
            problems.add("正文有效字数仅" + wordCount + "字，明显不足");
        } else if (targetWords > 0) {
            if (wordCount < targetWords * MIN_RATIO) {
                problems.add("正文有效字数" + wordCount + "字，低于目标" + targetWords + "字的" + (int) (MIN_RATIO * 100) + "%");
            } else if (wordCount > targetWords * MAX_RATIO) {
                problems.add("正文有效字数" + wordCount + "字，超过目标" + targetWords + "字的" + (int) (MAX_RATIO * 100) + "%，存在注水嫌疑");
            }
        }
        checkDuplicateParagraphs(content, problems);
        return problems;
    }

    /**
     * 重复段落检测：长度达标的段落中，完全重复的段落占比超过阈值视为注水信号
     */
    private static void checkDuplicateParagraphs(String content, List<String> problems) {
        String[] lines = content.split("\\n");
        Map<String, Integer> counter = new HashMap<>();
        int total = 0;
        int duplicated = 0;
        for (String line : lines) {
            String paragraph = line.trim();
            if (paragraph.length() < MIN_PARAGRAPH_LEN) {
                continue;
            }
            total++;
            Integer cnt = counter.merge(paragraph, 1, Integer::sum);
            if (cnt == 2) {
                // 该段落重复出现一次，计入一次重复
                duplicated++;
            } else if (cnt > 2) {
                duplicated++;
            }
        }
        if (total >= 5 && (double) duplicated / total > DUP_RATIO_THRESHOLD) {
            problems.add("存在" + duplicated + "处重复段落，疑似内容复读");
        }
    }

    /**
     * AI 摘要失败时的程序化降级摘要：截取首个有效段落开头
     */
    public static String fallbackSummary(String content) {
        if (content == null || content.isBlank()) {
            return "";
        }
        String cleaned = content.replaceAll("<br>", "\n").replaceAll("&nbsp;", " ").trim();
        // 去掉首行标题
        int firstBreak = cleaned.indexOf('\n');
        if (firstBreak > 0) {
            cleaned = cleaned.substring(firstBreak + 1).trim();
        }
        int end = Math.min(cleaned.length(), 120);
        return cleaned.substring(0, end) + (cleaned.length() > end ? "……" : "");
    }
}

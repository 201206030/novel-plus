package com.java2nb.novel.service.impl;

import com.java2nb.novel.core.ai.AiRuleUtil;
import com.java2nb.novel.core.config.CrawlAiProperties;
import com.java2nb.novel.core.crawl.RuleExtractor;
import com.java2nb.novel.core.enums.ResponseStatus;
import com.java2nb.novel.core.utils.HttpUtil;
import com.java2nb.novel.service.AiRuleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.xxyopen.web.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Pattern;

/**
 * AI 辅助生成爬虫规则实现。
 * <p>
 * 按页面类型分 4 批并行调用模型：列表/详情/目录/正文各生成自己那部分字段，最后合并。
 * 每批上下文只含一个页面，字段聚焦，显著提升生成字段的覆盖率与准确率；
 * 结果经白名单过滤后返回前端回填。带 IP 频率限制与硬超时。
 *
 * @author xiongxiaoyang
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AiRuleServiceImpl implements AiRuleService {

    private static final String PROMPT_PATH = "prompts/crawl-rule/generate.txt";
    private static final String REPAIR_PROMPT_PATH = "prompts/crawl-rule/repair.txt";

    /** 每批任务的页面标签、负责字段（供提示词展示与输出约束） */
    private static final String LIST_FIELDS =
        "bookListUrl、bookIdPatten、pagePatten、totalPagePatten、bookDetailUrl、bookIndexUrl、bookContentUrl、picUrlPrefix";
    private static final String DETAIL_FIELDS =
        "bookNamePatten、authorNamePatten、picUrlPatten、statusPatten、bookStatus0、bookStatus1、scorePatten、"
            + "visitCountPatten、descPatten、descStart、descEnd、filterDesc、upadateTimePatten、upadateTimeFormatPatten";
    private static final String INDEX_FIELDS = "indexIdPatten、indexNamePatten、bookIndexStart";
    private static final String CONTENT_FIELDS = "contentPatten、contentStart、contentEnd、filterContent";

    /** 提示词模板，加载一次后缓存 */
    private static volatile String promptTemplate;

    /** 定向修复提示词模板 */
    private static volatile String repairPromptTemplate;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /** 作者名里不应出现的相邻字段关键字，用于识别「取到上一行」 */
    private static final String[] WRONG_AUTHOR_WORDS = {"作者", "分类", "类别", "状态", "更新", "字数", "推荐", "点击", "连载", "完结"};

    /** 单值字段禁用位置选择器：上方多一行分类就会整体偏移 */
    private static final Pattern POSITION_SELECTOR =
        Pattern.compile(":(eq|lt|gt|nth-child|nth-last-child|nth-of-type|first-child|last-child)\\s*\\(");

    /** 分批并行调用的小型线程池 */
    private final ExecutorService batchExecutor = Executors.newFixedThreadPool(4, r -> {
        Thread t = new Thread(r, "ai-rule-batch");
        t.setDaemon(true);
        return t;
    });

    private final ChatClient chatClient;

    private final CrawlAiProperties properties;

    /** 简易 IP 频率限制：IP -> 上次调用时间戳（毫秒） */
    private final ConcurrentHashMap<String, Long> lastCallTime = new ConcurrentHashMap<>();

    @Override
    public Map<String, String> generateRules(String listUrl, String detailUrl, String indexUrl, String contentUrl,
                                             String parseType, String charset, String clientIp) {
        if (listUrl == null || listUrl.isBlank()) {
            throw new BusinessException(ResponseStatus.AI_PARAMS_INVALID);
        }
        checkUrl(listUrl);
        checkUrl(detailUrl);
        checkUrl(indexUrl);
        checkUrl(contentUrl);

        String normalizedCharset = normalizeCharset(charset);
        checkRateLimit(clientIp);

        int maxChars = properties.getMaxHtmlChars();
        String listHtml = fetchClean(listUrl, normalizedCharset, maxChars);
        String detailHtml = fetchClean(detailUrl, normalizedCharset, maxChars);
        String indexHtml = fetchClean(indexUrl, normalizedCharset, maxChars);
        String contentHtml = fetchClean(contentUrl, normalizedCharset, maxChars);

        // 组装批次：仅有可用 HTML 的页面才生成对应字段
        List<Batch> batches = new ArrayList<>();
        batches.add(new Batch("列表页", LIST_FIELDS, listHtml));
        batches.add(new Batch("详情页", DETAIL_FIELDS, detailHtml));
        batches.add(new Batch("目录页", INDEX_FIELDS, indexHtml));
        batches.add(new Batch("正文页", CONTENT_FIELDS, contentHtml));
        batches.removeIf(b -> b.html == null);

        if (batches.isEmpty()) {
            throw new BusinessException(ResponseStatus.AI_GEN_FAILED);
        }

        // 并行提交各批次
        List<Future<Map<String, String>>> futures = new ArrayList<>();
        for (Batch batch : batches) {
            futures.add(batchExecutor.submit(() -> runBatch(batch, normalizedCharset,
                listUrl, detailUrl, indexUrl, contentUrl)));
        }

        Map<String, String> merged = new LinkedHashMap<>();
        int success = 0;
        for (Future<Map<String, String>> future : futures) {
            try {
                Map<String, String> part = future.get();
                merged.putAll(part);
                success++;
            } catch (Exception e) {
                log.warn("AI规则某批次生成失败：{}", e.getClass().getSimpleName());
            }
        }
        if (success == 0 || merged.isEmpty()) {
            throw new BusinessException(ResponseStatus.AI_GEN_FAILED);
        }
        // 用真实 HTML 回放校验详情页字段，发现「取到上一行/取不到/状态时间无法解析」时定向修复
        return verifyAndRepair(merged, detailHtml, normalizedCharset, listUrl, detailUrl, indexUrl, contentUrl);
    }

    /** 执行单个批次：组装聚焦提示词 -> 调用模型 -> 白名单过滤 */
    private Map<String, String> runBatch(Batch batch, String charset,
                                         String listUrl, String detailUrl, String indexUrl, String contentUrl) {
        String prompt = buildPrompt(charset, listUrl, detailUrl, indexUrl, contentUrl, batch);
        String raw = callAi(prompt);
        return AiRuleUtil.parseRules(raw);
    }

    /**
     * 用真实详情 HTML 回放校验规则，发现「取到上一行/取不到值/状态或时间无法解析」时，
     * 让模型针对失败字段做一次定向修复。校验和修复都不阻断主流程：修复失败或未改善时保留原结果。
     */
    private Map<String, String> verifyAndRepair(Map<String, String> rules, String detailHtml, String charset,
                                                String listUrl, String detailUrl, String indexUrl, String contentUrl) {
        if (detailHtml == null || detailHtml.isBlank()) {
            return rules;
        }
        List<String> issues = new ArrayList<>();
        RuleExtractor extractor;
        try {
            extractor = RuleExtractor.create("CSS", detailHtml);
            checkDetailRules(rules, extractor, issues);
        } catch (Exception e) {
            log.warn("AI规则回放校验跳过：{}", e.getClass().getSimpleName());
            return rules;
        }
        if (issues.isEmpty()) {
            return rules;
        }
        log.warn("AI规则回放校验发现 {} 项问题：{}", issues.size(), issues);
        try {
            String currentRules = MAPPER.writeValueAsString(rules);
            String issuesText = String.join("\n", issues);
            String prompt = buildRepairPrompt(charset, listUrl, detailUrl, indexUrl, contentUrl,
                detailHtml, currentRules, issuesText);
            Map<String, String> fixed = AiRuleUtil.parseRules(callAi(prompt));

            // 只接收失败项涉及的字段（含配套字段），其余字段即使模型输出了也丢弃，避免误改
            Map<String, String> accepted = new LinkedHashMap<>();
            for (Map.Entry<String, String> entry : fixed.entrySet()) {
                String key = entry.getKey();
                boolean companionStatus = (key.equals("bookStatus0") || key.equals("bookStatus1"))
                    && issuesText.contains("statusPatten");
                boolean companionTime = key.equals("upadateTimeFormatPatten")
                    && issuesText.contains("upadateTimePatten");
                if (issuesText.contains(key) || companionStatus || companionTime) {
                    accepted.put(key, entry.getValue());
                }
            }

            // 只接受「修复后问题确实减少」的结果，防止模型把本来正确的字段改坏
            Map<String, String> candidate = new LinkedHashMap<>(rules);
            candidate.putAll(accepted);
            List<String> after = new ArrayList<>();
            checkDetailRules(candidate, extractor, after);
            if (after.size() < issues.size()) {
                log.info("AI规则定向修复生效：问题由 {} 项降为 {} 项", issues.size(), after.size());
                return candidate;
            }
            log.warn("AI规则定向修复未改善，保留原结果");
        } catch (Exception e) {
            log.warn("AI规则定向修复失败，保留原结果：{}", e.getClass().getSimpleName());
        }
        return rules;
    }

    /**
     * 详情页字段回放校验：作者、状态、更新时间三组是最容易整体错位的一批。
     */
    private void checkDetailRules(Map<String, String> rules, RuleExtractor extractor, List<String> issues) {
        // 作者：取不到，或取到的是带前缀/相邻字段的脏值
        String authorRule = rules.get("authorNamePatten");
        if (notBlank(authorRule)) {
            if (hasPositionSelector(authorRule)) {
                issues.add("authorNamePatten 使用了 :eq(n)/:nth-child(n) 等位置选择器，上方多一行分类就会整体偏移。请改用 :containsOwn(作者) 按标签文字锚定当前字段。");
            }
            String value = safeFirst(extractor, authorRule);
            if (value == null) {
                issues.add("authorNamePatten 没有匹配到任何内容。请改用 :containsOwn(作者) 锚定作者标签所在节点，不要用 :eq(n)/:nth-child(n)，也不要选父容器。");
            } else if (looksLikeNeighborField(value)) {
                issues.add("authorNamePatten 实际取到「" + value + "」，是相邻字段或带前缀的脏值而非作者名。请锚定作者标签并用 ## 正则剥掉前缀，只留纯作者名。");
            }
        }

        // 状态：取出值必须与 bookStatus0/bookStatus1 的字面值精确一致，否则查 Map 失败
        String statusRule = rules.get("statusPatten");
        if (notBlank(statusRule)) {
            if (hasPositionSelector(statusRule)) {
                issues.add("statusPatten 使用了位置选择器，上方多一行分类就会整体偏移。请改用 :containsOwn(状态) 按标签文字锚定当前字段。");
            }
            String value = safeFirst(extractor, statusRule);
            String s0 = rules.get("bookStatus0");
            String s1 = rules.get("bookStatus1");
            if (value == null) {
                issues.add("statusPatten 没有匹配到任何内容。请按状态标签文字锚定当前行，不要用位置索引，也不要选父容器。");
            } else if (!notBlank(s0) && !notBlank(s1)) {
                issues.add("bookStatus0 / bookStatus1 均缺失，statusPatten 取到的「" + value + "」无法用于状态映射。请补上两者，并让 statusPatten 的取值精确等于其中之一。");
            } else if (!value.equals(trimToNull(s0)) && !value.equals(trimToNull(s1))) {
                issues.add("statusPatten 实际取到「" + value + "」，与 bookStatus0「" + s0 + "」和 bookStatus1「" + s1 + "」都不相等（很可能取到了上方分类行）。请锚定状态标签并剥前缀，使取值与状态字面值一字不差。");
            }
        }

        // 更新时间：取出串必须能被 SimpleDateFormat 严格解析
        String timeRule = rules.get("upadateTimePatten");
        if (notBlank(timeRule)) {
            if (hasPositionSelector(timeRule)) {
                issues.add("upadateTimePatten 使用了位置选择器，上方多一行状态就会整体偏移。请改用 :containsOwn(更新) 按标签文字锚定当前字段。");
            }
            String value = safeFirst(extractor, timeRule);
            String format = rules.get("upadateTimeFormatPatten");
            if (value == null) {
                issues.add("upadateTimePatten 没有匹配到任何内容。请按更新时间标签文字锚定当前行，不要用位置索引，也不要选父容器。");
            } else if (!notBlank(format)) {
                issues.add("upadateTimeFormatPatten 缺失，无法解析取到的「" + value + "」。请补上与真实时间串逐字对应的 SimpleDateFormat 格式。");
            } else if (!canParseDateTime(value, format)) {
                issues.add("upadateTimePatten 取到「" + value + "」无法被 upadateTimeFormatPatten「" + format + "」严格解析（很可能取到了状态那一行或带了前缀）。请锚定更新时间标签并核对日期占位符。");
            }
        }
    }

    private boolean hasPositionSelector(String rule) {
        return POSITION_SELECTOR.matcher(rule).find();
    }

    /** 作者字段取到这些关键字，说明命中了相邻行或没剥掉标签前缀 */
    private boolean looksLikeNeighborField(String value) {
        if (value.length() > 30) {
            return true;
        }
        for (String word : WRONG_AUTHOR_WORDS) {
            if (value.contains(word)) {
                return true;
            }
        }
        return false;
    }

    private String safeFirst(RuleExtractor extractor, String rule) {
        try {
            return extractor.extractFirst(rule);
        } catch (Exception e) {
            return null;
        }
    }

    /** SimpleDateFormat 严格解析：不允许 lenient 容错，且必须完整消费整个字符串 */
    private boolean canParseDateTime(String value, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            sdf.setLenient(false);
            ParsePosition position = new ParsePosition(0);
            Date date = sdf.parse(value, position);
            return date != null && position.getIndex() == value.length();
        } catch (Exception e) {
            return false;
        }
    }

    private boolean notBlank(String value) {
        return value != null && !value.isBlank();
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    /** 渲染定向修复提示词 */
    private String buildRepairPrompt(String charset, String listUrl, String detailUrl, String indexUrl, String contentUrl,
                                     String detailHtml, String currentRules, String issues) {
        return loadRepairTemplate()
            .replace("__PAGE_HTML__", detailHtml)
            .replace("__CURRENT_RULES__", currentRules)
            .replace("__ISSUES__", issues)
            .replace("__CHARSET__", charset)
            .replace("__LIST_URL__", listUrl == null ? "" : listUrl)
            .replace("__DETAIL_URL__", detailUrl == null || detailUrl.isBlank() ? "（未提供）" : detailUrl)
            .replace("__INDEX_URL__", indexUrl == null || indexUrl.isBlank() ? "（未提供）" : indexUrl)
            .replace("__CONTENT_URL__", contentUrl == null || contentUrl.isBlank() ? "（未提供）" : contentUrl);
    }

    private String loadRepairTemplate() {
        if (repairPromptTemplate == null) {
            synchronized (AiRuleServiceImpl.class) {
                if (repairPromptTemplate == null) {
                    repairPromptTemplate = loadResource(REPAIR_PROMPT_PATH);
                }
            }
        }
        return repairPromptTemplate;
    }

    /** 校验 URL 合法性，仅允许 http/https，空值跳过 */
    private void checkUrl(String url) {
        if (url == null || url.isBlank()) {
            return;
        }
        if (!url.matches("(?i)^https?://.+")) {
            throw new BusinessException(ResponseStatus.AI_PARAMS_INVALID);
        }
    }

    private String normalizeCharset(String charset) {
        String value = charset == null || charset.trim().isEmpty() ? "utf-8" : charset.trim();
        try {
            return java.nio.charset.Charset.forName(value).name();
        } catch (IllegalArgumentException e) {
            throw new BusinessException(ResponseStatus.AI_PARAMS_INVALID);
        }
    }

    /** 抓取页面并清洗；不可用返回 null，该批次跳过 */
    private String fetchClean(String url, String charset, int maxChars) {
        if (url == null || url.isBlank()) {
            return null;
        }
        String html = HttpUtil.getByHttpClientWithChrome(url, charset);
        if (html == null) {
            return null;
        }
        return AiRuleUtil.cleanHtml(html, maxChars);
    }

    /** 渲染聚焦提示词，字面替换避免规则里的 {bookId} 被误当占位符 */
    private String buildPrompt(String charset, String listUrl, String detailUrl, String indexUrl, String contentUrl,
                               Batch batch) {
        String template = loadTemplate();
        return template
            .replace("__TASK_DESC__", batch.label + "规则")
            .replace("__TASK_FIELDS__", batch.fields)
            .replace("__PAGE_LABEL__", batch.label)
            .replace("__PAGE_HTML__", batch.html)
            .replace("__CHARSET__", charset)
            .replace("__LIST_URL__", listUrl == null ? "" : listUrl)
            .replace("__DETAIL_URL__", detailUrl == null || detailUrl.isBlank() ? "（未提供）" : detailUrl)
            .replace("__INDEX_URL__", indexUrl == null || indexUrl.isBlank() ? "（未提供）" : indexUrl)
            .replace("__CONTENT_URL__", contentUrl == null || contentUrl.isBlank() ? "（未提供）" : contentUrl);
    }

    private String loadTemplate() {
        if (promptTemplate == null) {
            synchronized (AiRuleServiceImpl.class) {
                if (promptTemplate == null) {
                    promptTemplate = loadResource(PROMPT_PATH);
                }
            }
        }
        return promptTemplate;
    }

    /** 从 classpath 加载提示词模板并缓存调用方 */
    private String loadResource(String path) {
        try (InputStream is = new ClassPathResource(path).getInputStream()) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("加载AI提示词模板失败: {}", path, e);
            throw new BusinessException(ResponseStatus.AI_GEN_FAILED);
        }
    }

    /** 流式聚合调用模型，分段响应规避长上下文读超时 */
    private String callAi(String prompt) {
        try {
            return String.join("",
                chatClient.prompt().user(prompt).stream().content()
                    .collectList().block(Duration.ofSeconds(properties.getTimeoutSeconds())));
        } catch (IllegalStateException e) {
            log.warn("AI规则生成调用超时");
            throw new BusinessException(ResponseStatus.AI_GEN_TIMEOUT);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.warn("AI规则生成调用失败: {}", e.getClass().getSimpleName());
            throw new BusinessException(ResponseStatus.AI_GEN_FAILED);
        }
    }

    /** 简单 IP 限流：两次生成请求间隔不得小于配置值 */
    private void checkRateLimit(String clientIp) {
        String key = clientIp == null || clientIp.isBlank() ? "unknown" : clientIp;
        long now = System.currentTimeMillis();
        long interval = Math.max(0, properties.getMinIntervalSeconds()) * 1000L;
        lastCallTime.compute(key, (k, last) -> {
            if (last != null && now - last < interval) {
                throw new BusinessException(ResponseStatus.AI_RATE_LIMIT);
            }
            return now;
        });
    }

    /** 一个生成批次 */
    private static class Batch {
        final String label;
        final String fields;
        final String html;

        Batch(String label, String fields, String html) {
            this.label = label;
            this.fields = fields;
            this.html = html;
        }
    }
}

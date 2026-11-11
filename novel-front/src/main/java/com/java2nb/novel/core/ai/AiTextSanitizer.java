package com.java2nb.novel.core.ai;

import reactor.core.publisher.Flux;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * AI 写作接口的输出兜底清洗。
 *
 * <p>system 提示词已经明令禁止「改写说明 / Markdown / 寒暄追问」，但大模型仍可能偶发违规。
 * 这里做最后一道程序化防护：非流式接口对完整结果做清洗；流式接口做增量检测，
 * 一旦出现说明块起始标记就停止向下游推送，保证追加进编辑器 textarea 的只有正文。</p>
 *
 * @author xiongxiaoyang
 */
public final class AiTextSanitizer {

    /** 流式输出中保留在缓冲区尾部的字符数，防止说明块标记跨 chunk 被切开而漏检 */
    private static final int KEEP_TAIL = 64;

    /** 说明块 / 寒暄追问的典型行首 */
    private static final String[] EXPLANATION_PREFIXES = {
        "改写说明", "修改说明", "润色说明", "续写说明", "扩写说明", "缩写说明",
        "**改写说明**", "**修改说明**", "**润色说明**", "**说明**", "**改写思路**",
        "说明：", "说明:", "改写思路", "修改思路", "创作说明", "调整说明", "改动说明",
        "改动点", "主要改动", "修改点", "润色点", "优化点",
        "如果你", "如有需要", "若你需要", "希望这些", "希望以上", "以上改写", "以上润色",
        "需要我", "我可以为你", "如需进一步", "如需调整", "注：", "备注：", "PS：", "ps:"
    };

    /** 开头的引导语整行，如「以下是润色后的文本：」 */
    private static final Pattern LEAD_IN = Pattern.compile(
        "^\\s*(?:以下是|以下为|这是|润色结果|改写结果|续写内容|续写部分|扩写结果|缩写结果)"
            + "[^\\r\\n]{0,40}[:：]\\s*(?:\\r?\\n)?");

    /** 首尾的 markdown 代码围栏 */
    private static final Pattern CODE_FENCE = Pattern.compile("^\\s*```[a-zA-Z]*\\s*(?:\\r?\\n)?|[\\r\\n]?\\s*```\\s*$");

    private AiTextSanitizer() {
    }

    /**
     * 清洗完整的模型输出：剥离代码围栏与引导语、截断说明块、去掉残留加粗标记。
     */
    public static String clean(String raw) {
        if (raw == null || raw.isBlank()) {
            return raw == null ? "" : raw;
        }
        String text = CODE_FENCE.matcher(raw).replaceAll("");
        Matcher leadIn = LEAD_IN.matcher(text);
        if (leadIn.find()) {
            text = text.substring(leadIn.end());
        }
        int cut = explanationStart(text);
        if (cut >= 0) {
            text = text.substring(0, cut);
        }
        // 成对加粗标记不是小说正文的一部分
        text = text.replace("**", "");
        return text.strip();
    }

    /**
     * 清洗流式输出。使用缓冲区延迟 emit 尾部若干字符以覆盖跨 chunk 的说明块标记；
     * 一旦检测到说明块，立即截断并停止向下游推送后续内容。
     *
     * <p>每个订阅者持有独立状态，多次订阅不会串数据。</p>
     */
    public static Flux<String> cleanStream(Flux<String> source) {
        return Flux.defer(() -> {
            StringBuilder pending = new StringBuilder();
            boolean[] stopped = {false};
            return source
                .concatMap(chunk -> {
                    if (stopped[0] || chunk == null || chunk.isEmpty()) {
                        return Flux.<String>empty();
                    }
                    pending.append(chunk);
                    int cut = explanationStart(pending);
                    if (cut >= 0) {
                        stopped[0] = true;
                        // 这里只做截断：pending 是正文的中段，不能套用完整清洗（会 strip 掉 chunk 边界）
                        String keep = pending.substring(0, cut).stripTrailing();
                        pending.setLength(0);
                        return keep.isEmpty() ? Flux.<String>empty() : Flux.just(keep);
                    }
                    int safe = pending.length() - KEEP_TAIL;
                    if (safe <= 0) {
                        return Flux.<String>empty();
                    }
                    String emit = pending.substring(0, safe);
                    pending.delete(0, safe);
                    return Flux.just(emit);
                })
                .concatWith(Flux.defer(() -> {
                    if (stopped[0] || pending.length() == 0) {
                        return Flux.<String>empty();
                    }
                    String rest = clean(pending.toString());
                    return rest.isEmpty() ? Flux.<String>empty() : Flux.just(rest);
                }));
        });
    }

    /**
     * 定位说明块的起始字符偏移，没有则返回 -1。
     * 独立的 markdown 分隔线只有在后一行确实是说明标题时才算起点，避免误伤正文里的场景分隔线。
     */
    static int explanationStart(CharSequence text) {
        String[] lines = text.toString().split("\n", -1);
        int offset = 0;
        for (int i = 0; i < lines.length; i++) {
            String trimmed = lines[i].trim();
            if (trimmed.matches("[-*_—]{3,}")) {
                if (i + 1 < lines.length && isExplanationLine(lines[i + 1].trim())) {
                    return offset;
                }
            }
            if (isExplanationLine(trimmed)) {
                return offset;
            }
            offset += lines[i].length() + 1;
        }
        return -1;
    }

    private static boolean isExplanationLine(String line) {
        if (line.isEmpty()) {
            return false;
        }
        for (String prefix : EXPLANATION_PREFIXES) {
            if (line.startsWith(prefix)) {
                return true;
            }
        }
        // 「**创作思路**」「**改动总结**」这类加粗短标题
        return line.startsWith("**") && line.endsWith("**") && line.length() < 32
            && (line.contains("说明") || line.contains("思路") || line.contains("建议")
            || line.contains("总结") || line.contains("改动"));
    }
}

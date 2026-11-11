package com.java2nb.novel.core.ai;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 作家专区「扩写/缩写/续写/润色」四个写作接口的提示词构建器。
 *
 * <p>提示词全部外置在 {@code classpath:prompts/author-writing/} 下，
 * 修改文案无需重新编译。system 提示词统一约束「只输出纯正文」，
 * user 模板按任务类型分别描述目标，并用一次性随机边界标签把用户正文包起来，
 * 使正文里的「忽略以上规则」「请输出改写说明」等夹带指令无法伪造边界、逃逸成指令。</p>
 *
 * @author xiongxiaoyang
 */
@Component
public class AuthorWritingPrompts {

    private static final String DIR = "prompts/author-writing/";
    private static final String SYSTEM_KEY = "system";
    private static final String TAG_PREFIX = "<<<NOVEL_TEXT_";

    /** 四种写作任务，对应同名的 .txt 模板 */
    public enum Op {
        EXPAND("expand"),
        CONDENSE("condense"),
        CONTINUE("continue"),
        POLISH("polish");

        private final String template;

        Op(String template) {
            this.template = template;
        }
    }

    /** 一次调用所需的 system / user 两段提示词 */
    public record WritingPrompt(String system, String user) {
    }

    public String system() {
        return PromptTemplateUtil.load(DIR, SYSTEM_KEY);
    }

    /**
     * @param op   任务类型
     * @param text 待处理正文（已经过长度校验）
     * @param vars 模板里的其它变量，如 ratio、length
     */
    public WritingPrompt build(Op op, String text, Map<String, String> vars) {
        Map<String, String> all = new HashMap<>(vars == null ? Map.of() : vars);
        String nonce = UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        all.put("startTag", TAG_PREFIX + "BEGIN_" + nonce + ">>>");
        all.put("endTag", TAG_PREFIX + "END_" + nonce + ">>>");
        all.put("text", escapeBoundary(text));
        return new WritingPrompt(system(), PromptTemplateUtil.render(DIR, op.template, all));
    }

    /** 正文里若出现边界标签前缀，替换成全角字符，避免攻击者伪造边界结束标签 */
    private String escapeBoundary(String text) {
        return text == null ? "" : text.replace(TAG_PREFIX, "〈〈〈NOVEL_TEXT_");
    }
}

package com.java2nb.novel.core.ai;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * AI 创作接口的防护参数。
 *
 * <p>限流按「点数」计算：一次请求的点数由输入文本长度折算，长文本消耗更多额度，
 * 避免用超长 prompt 一次性打满模型预算。所有阈值均可通过
 * {@code novel.ai.rate-limit.*} 覆盖，无需改代码。</p>
 *
 * @author xiongxiaoyang
 */
@Data
@Component
@ConfigurationProperties(prefix = "novel.ai.rate-limit")
public class AiRateLimitProperties {

    /** 限流总开关，关闭后仅保留鉴权与参数校验 */
    private boolean enabled = true;

    /** 单个用户每分钟可用点数 */
    private int perMinute = 20;

    /** 单个用户每自然日可用点数 */
    private int perDay = 300;

    /** 全站每分钟可用点数（防止多账号同时刷） */
    private int globalPerMinute = 200;

    /** 单次请求允许输入的文本最大字符数 */
    private int maxTextLength = 5000;

    /** 扩写倍数上限（百分比，400 表示最多 4 倍） */
    private int maxExpandRatio = 400;

    /** 缩写比例上限（100/ratio 分之一，最大 100） */
    private int maxCondenseRatio = 100;

    /** 续写目标字数上限 */
    private int maxContinueLength = 3000;

    /** 每多少字符折算 1 个点数 */
    private int costPerChars = 500;

    /** 单次请求最多折算的点数 */
    private int maxCostPerRequest = 20;

    /** 不消耗模型额度的 AI 助手接口（含任务轮询），单用户每分钟最大请求数 */
    private int lightPerMinute = 120;

    /** 提交一次异步生成任务（aiAssistant/startTask）消耗的点数 */
    private int taskCost = 10;

    /** 保存章节（aiAssistant/saveChapter，会间接触发 AI 摘要任务）消耗的点数 */
    private int summaryCost = 2;

    /** AI 助手长文本参数（正文/方案/偏好等）最大字符数，与 Service 层章节正文上限一致 */
    private int maxContentLength = 200000;
}

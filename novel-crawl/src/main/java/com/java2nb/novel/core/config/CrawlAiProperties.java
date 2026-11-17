package com.java2nb.novel.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * AI 辅助生成爬虫规则的可调参数，对应 application.yml 中的 crawl.ai.*
 *
 * @author xiongxiaoyang
 */
@Data
@Component
@ConfigurationProperties(prefix = "crawl.ai")
public class CrawlAiProperties {

    /** 单次模型调用硬超时（秒） */
    private int timeoutSeconds = 120;

    /** 每个示例页面送入模型的 HTML 最大字符数 */
    private int maxHtmlChars = 24000;

    /** 同一 IP 两次生成请求的最小间隔（秒），防止连点刷额度 */
    private int minIntervalSeconds = 5;
}

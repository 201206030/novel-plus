package com.java2nb.novel.core.config;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

/**
 * AI 相关配置：与 novel-front 保持同一套客户端构建方式，
 * 仅把读取超时改为可配置，避免大 HTML 上下文导致读超时。
 *
 * @author xiongxiaoyang
 */
@Configuration
@RequiredArgsConstructor
public class AiConfig {

    private final CrawlAiProperties properties;

    /**
     * 自定义 RestClientBuilder：连接 5s，读取超时取 crawl.ai.timeout-seconds
     */
    @Bean
    public RestClient.Builder restClientBuilder() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(properties.getTimeoutSeconds() * 1000);
        return RestClient.builder().requestFactory(factory);
    }

    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder) {
        return chatClientBuilder.build();
    }
}

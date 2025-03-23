package com.java2nb.novel.core.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

/**
 * Ai 相关配置
 *
 * @author xiongxiaoyang
 * @date 2025/2/19
 */
@Configuration
@Slf4j
public class AiConfig {

    /**
     * 配置自定义的 RestClientBuilder 对象
     */
    @Bean
    public RestClient.Builder restClientBuilder() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        // 连接超时时间
        factory.setConnectTimeout(5000);
        // 读取超时时间
        factory.setReadTimeout(60000);
        return RestClient.builder().requestFactory(factory);
    }

    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder) {
        return chatClientBuilder.build();
    }

}

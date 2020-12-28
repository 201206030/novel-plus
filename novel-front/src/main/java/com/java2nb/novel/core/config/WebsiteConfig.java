package com.java2nb.novel.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 11797
 */
@Data
@Component
@ConfigurationProperties(prefix="website")
public class WebsiteConfig {

    private String name;
    private String domain;
    private String keyword;
    private String description;
    private String qq;
}

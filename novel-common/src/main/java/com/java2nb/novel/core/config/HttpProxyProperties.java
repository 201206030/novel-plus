package com.java2nb.novel.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author xiongxiaoyang
 * @date 2022/7/14
 */
@Data
@Component
@ConfigurationProperties(prefix = "http.proxy")
public class HttpProxyProperties {

    private Boolean enabled;

    private String ip;

    private Integer port;

}

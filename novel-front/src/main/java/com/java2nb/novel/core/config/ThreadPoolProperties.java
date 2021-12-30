package com.java2nb.novel.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 线程池配置属性
 * @author xiongxiaoyang
 */
@Data
@Component
@ConfigurationProperties(prefix = "thread.pool")
public class ThreadPoolProperties {

    private Integer corePoolSize;
    private Integer maximumPoolSize;
    private Long keepAliveTime;
    private Integer queueSize;


}

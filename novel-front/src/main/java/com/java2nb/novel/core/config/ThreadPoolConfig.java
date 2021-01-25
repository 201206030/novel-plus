package com.java2nb.novel.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池配置
 * @author xiongxiaoyang
 */
@Configuration
public class ThreadPoolConfig {

    @Bean
    public ThreadPoolExecutor threadPoolExecutor(ThreadPoolProperties properties){
        return new ThreadPoolExecutor(properties.getCorePoolSize(),properties.getMaximumPoolSize(),properties.getKeepAliveTime()
                , TimeUnit.SECONDS, new LinkedBlockingDeque<>(properties.getQueueSize()),new ThreadPoolExecutor.AbortPolicy());
    }

}

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

    /**
     * AI创作生成任务专用线程池：长耗时模型调用与页面渲染线程隔离，避免阻塞前台页面加载
     */
    @Bean("aiTaskExecutor")
    public ThreadPoolExecutor aiTaskExecutor(){
        return new ThreadPoolExecutor(1, 2, 60, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(30), new ThreadPoolExecutor.AbortPolicy());
    }

}

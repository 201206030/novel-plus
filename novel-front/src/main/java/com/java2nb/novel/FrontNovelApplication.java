package com.java2nb.novel;

import com.github.tobato.fastdfs.FdfsClientConfig;
import io.shardingsphere.shardingjdbc.spring.boot.SpringBootConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Administrator
 */
@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling
@EnableCaching
@MapperScan(basePackages = {"com.java2nb.novel.mapper"})
@Import(FdfsClientConfig.class)
public class FrontNovelApplication {

    public static void main(String[] args) {
        SpringApplication.run(FrontNovelApplication.class);
    }

    /**
     * 解决同一时间只能一个定时任务执行的问题
     * */
    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(5);
        return taskScheduler;
    }
}

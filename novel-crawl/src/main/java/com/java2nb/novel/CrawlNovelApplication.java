package com.java2nb.novel;

import io.shardingsphere.shardingjdbc.spring.boot.SpringBootConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Administrator
 */
@SpringBootApplication(exclude = {SpringBootConfiguration.class})
@EnableCaching
@EnableScheduling
@ServletComponentScan
@MapperScan(basePackages = {"com.java2nb.novel.mapper"})
public class CrawlNovelApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrawlNovelApplication.class);
    }


}

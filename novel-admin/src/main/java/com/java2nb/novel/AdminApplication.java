package com.java2nb.novel;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.net.InetAddress;

/**
 * 后台管理系统启动类
 *
 * @author Administrator
 */
@SpringBootApplication
@EnableCaching
@ServletComponentScan
@MapperScan(basePackages = {"com.java2nb.novel.mapper"})
@Slf4j
public class AdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            log.info("项目启动啦，访问路径：{}", "http://" + InetAddress.getLocalHost().getHostAddress() + ":" + ctx.getEnvironment().getProperty("server.port"));
        };
    }

}

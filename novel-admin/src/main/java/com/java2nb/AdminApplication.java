package com.java2nb;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.net.InetAddress;
import java.sql.Connection;


@EnableTransactionManagement
@ServletComponentScan
@MapperScan("com.java2nb.*.dao")
@SpringBootApplication(exclude = {
    org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
})
@EnableCaching
@Slf4j
public class AdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx, DataSource dataSource) {
        return args -> {
            log.info("创建连接池...");
            try (Connection connection = dataSource.getConnection()) {
                log.info("连接池已创建.");
                log.info("数据库：{}", connection.getMetaData().getDatabaseProductName());
                log.info("数据库版本：{}", connection.getMetaData().getDatabaseProductVersion());
            }
            log.info("项目启动啦，访问路径：{}",
                "http://" + InetAddress.getLocalHost().getHostAddress() + ":" + ctx.getEnvironment()
                    .getProperty("server.port"));
        };
    }
}

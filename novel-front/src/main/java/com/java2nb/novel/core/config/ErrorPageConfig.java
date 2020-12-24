package com.java2nb.novel.core.config;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

/**
 * 错误页面配置
 * @author xiongxiaoyang
 */
@Configuration
public class ErrorPageConfig implements ErrorPageRegistrar {

    @Override
    public void registerErrorPages(ErrorPageRegistry registry) {
        /*1.错误类型为404，默认显示404.html网页*/
        ErrorPage e404 = new ErrorPage(HttpStatus.NOT_FOUND, "/404.html");
        /**
        TODO 2.错误类型为500，表示服务器响应错误，默认显示/500.html网页
        */
        registry.addErrorPages(e404);
    }
}

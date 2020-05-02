package com.java2nb.novel.core.config;

import com.java2nb.novel.core.filter.NovelFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 过滤器配置类
 * @author Administrator
 */
@Configuration
public class FilterConfig{

    @Value("${pic.save.path}")
    private String picSavePath;

    @Bean
    public FilterRegistrationBean<NovelFilter> filterRegist() {
        FilterRegistrationBean<NovelFilter> frBean = new FilterRegistrationBean<>();
        frBean.setFilter(new NovelFilter());
        frBean.addUrlPatterns("/*");
        frBean.addInitParameter("picSavePath",picSavePath);
        return frBean;
    }

}

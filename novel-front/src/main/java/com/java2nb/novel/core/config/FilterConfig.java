package com.java2nb.novel.core.config;

import com.java2nb.novel.core.filter.NovelFilter;
import com.java2nb.novel.core.filter.XssFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.DispatcherType;
import java.util.HashMap;
import java.util.Map;

/**
 * 过滤器配置类
 * @author Administrator
 */
@Configuration
public class FilterConfig{

    @Value("${pic.save.path}")
    private String picSavePath;

    @Value("${xss.enabled}")
    private String enabled;

    @Value("${xss.excludes}")
    private String excludes;

    @Value("${xss.urlPatterns}")
    private String urlPatterns;

    @Bean
    public FilterRegistrationBean<NovelFilter> filterRegister() {
        FilterRegistrationBean<NovelFilter> frBean = new FilterRegistrationBean<>();
        frBean.setFilter(new NovelFilter());
        frBean.addUrlPatterns("/*");
        frBean.addInitParameter("picSavePath",picSavePath);
        return frBean;
    }

    @Bean
    public FilterRegistrationBean<XssFilter> xssFilterRegistration()
    {
        FilterRegistrationBean<XssFilter> registration = new FilterRegistrationBean<>();
        //
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        //过滤器类（继承Filter）
        registration.setFilter(new XssFilter());
        //
        registration.addUrlPatterns(StringUtils.split(urlPatterns, ","));
        //
        registration.setName("xssFilter");
        //
        registration.setOrder(Integer.MAX_VALUE);
        Map<String, String> initParameters = new HashMap<>(2);
        initParameters.put("excludes", excludes);
        initParameters.put("enabled", enabled);
        //Filter 初始化参数
        registration.setInitParameters(initParameters);
        return registration;
    }


}

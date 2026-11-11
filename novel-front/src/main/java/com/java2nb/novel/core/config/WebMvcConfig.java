package com.java2nb.novel.core.config;

import com.java2nb.novel.core.converter.DateConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author xiongxiaoyang
 */
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final AiApiAuthInterceptor aiApiAuthInterceptor;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new DateConverter());
    }

    /**
     * 作者后台 AI 接口统一鉴权 + 限流：新增 /author/ai/ 与 /author/aiAssistant/ 下的接口默认被保护
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(aiApiAuthInterceptor)
            .addPathPatterns("/author/ai/**", "/author/aiAssistant/**", "/author/queryAiGenPic");
    }

    /**
     * 跨域配置：供 uni-app 跨端客户端（H5 开发调试/独立部署）直连 REST 接口
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOriginPatterns("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true)
            .maxAge(3600);
    }
}

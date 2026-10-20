package com.java2nb.novel.core.config;

import com.java2nb.novel.core.converter.DateConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * MVC配置，注册全局日期参数转换器
 *
 * @author Administrator
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${pic.save.path}")
    private String picSavePath;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new DateConverter());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 富文本上传的图片通过 /localPic/** 访问，与 novel-front 共用同一存储目录
        String basePath = picSavePath.endsWith("/") ? picSavePath : picSavePath + "/";
        registry.addResourceHandler("/localPic/**").addResourceLocations("file:" + basePath + "localPic/");
    }
}

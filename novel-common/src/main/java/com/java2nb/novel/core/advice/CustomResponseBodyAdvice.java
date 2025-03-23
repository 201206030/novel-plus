package com.java2nb.novel.core.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Objects;

/**
 * 在对 RestController 返回对象 json 序列化时，将所有 Long 类型转为 String 类型返回，避免前端数据精度丢失的问题
 * 取代 spring.jackson.generator.write-numbers-as-strings=true 配置，避免影响全局的 ObjectMapper
 *
 * @author xiongxiaoyang
 * */
@RestControllerAdvice
public class CustomResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    private final ObjectMapper customObjectMapper;

    public CustomResponseBodyAdvice(Jackson2ObjectMapperBuilder builder) {
        customObjectMapper = builder.createXmlMapper(false).build();
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        customObjectMapper.registerModule(simpleModule);
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 返回 true 表示对所有 Controller 的响应都生效
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        // 使用自定义的 ObjectMapper 序列化响应体
        if(Objects.nonNull(body)) {
            return customObjectMapper.valueToTree(body);
        }else{
            return null;
        }
    }

}


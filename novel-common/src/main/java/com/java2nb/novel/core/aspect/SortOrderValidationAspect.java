package com.java2nb.novel.core.aspect;

import com.java2nb.novel.core.annotation.ValidateSortOrder;
import com.java2nb.novel.core.utils.SortWhitelistUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author xiongxiaoyang
 * @date 2025/7/17
 */
@Aspect
@Component
@RequiredArgsConstructor
public class SortOrderValidationAspect {

    /**
     * 拦截所有的mapper方法
     */
    @SneakyThrows
    @Around("execution(* com.java2nb.novel.mapper.*Mapper.*(..))")
    public Object validateSortAndOrder(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        for (int i = 0; i < parameterAnnotations.length; i++) {
            boolean hasAnnotation = Arrays.stream(parameterAnnotations[i])
                .anyMatch(a -> a.annotationType().equals(ValidateSortOrder.class));

            if (hasAnnotation && args[i] != null) {
                processArgument(args[i]);
            }
        }

        return joinPoint.proceed(args);
    }

    @SneakyThrows
    private void processArgument(Object obj) {
        if (obj instanceof Map<?,?> map) {
            processMap(map);
        } else {
            traverseAndSanitize(obj);
        }
    }

    @SneakyThrows
    private void traverseAndSanitize(Object obj) {
        for (Field field : obj.getClass().getDeclaredFields()) {
            switch (field.getName()) {
                case "sort", "order" -> {
                    field.setAccessible(true);
                    Object value = field.get(obj);
                    if (value instanceof String strValue) {
                        String sanitized = "sort".equals(field.getName())
                            ? SortWhitelistUtil.sanitizeColumn(strValue)
                            : SortWhitelistUtil.sanitizeOrder(strValue);
                        field.set(obj, sanitized);
                    }
                }
                default -> {
                }
            }
        }
    }

    private void processMap(Map map) {
        if (map.get("sort") instanceof String sortStr) {
            map.put("sort", SortWhitelistUtil.sanitizeColumn(sortStr));
        }
        if (map.get("order") instanceof String orderStr) {
            map.put("order", SortWhitelistUtil.sanitizeOrder(orderStr));
        }
    }

}

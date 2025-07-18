package com.java2nb.common.aspect;

import com.java2nb.common.annotation.ValidateSortOrder;
import com.java2nb.common.utils.SortWhitelistUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

/**
 * @author xiongxiaoyang
 * @date 2025/7/17
 */
@Aspect
@Component
@RequiredArgsConstructor
public class SortOrderValidationAspect {

    /**
     * 拦截mapper的所有list方法
     */
    @SneakyThrows
    @Around("execution(* com.java2nb.*.dao.*Dao.list*(..))")
    public Object validateSortAndOrder(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        for (int i = 0; i < parameterAnnotations.length; i++) {
            boolean hasAnnotation = Arrays.stream(parameterAnnotations[i])
                .anyMatch(a -> a.annotationType().equals(ValidateSortOrder.class));

            if (hasAnnotation && args[i] instanceof Map map) {
                if (map.get("sort") instanceof String sortStr) {
                    map.put("sort", SortWhitelistUtil.sanitizeColumn(sortStr));
                }
                if (map.get("order") instanceof String orderStr) {
                    map.put("order", SortWhitelistUtil.sanitizeOrder(orderStr));
                }
            }
        }

        return joinPoint.proceed(args);
    }

}

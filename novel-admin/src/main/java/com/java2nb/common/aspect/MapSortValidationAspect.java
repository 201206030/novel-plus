package com.java2nb.common.aspect;

import com.java2nb.common.annotation.SanitizeMap;
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
 * 拦截所有 Mapper 接口的 list* 方法，对带有 @SanitizeMap 注解的 Map 参数进行排序字段和顺序的规范化处理。
 *
 * <p>主要防止 SQL 注入或非法排序字段、非法排序顺序的问题。
 * 例如对 sort 和 order 字段进行白名单过滤和标准化处理。</p>
 */
@Aspect
@Component
@RequiredArgsConstructor
public class MapSortValidationAspect {

    /**
     * 拦截所有 Mapper 接口的 list* 方法（如 list(), listByPage 等）。
     * 对带有 @SanitizeMap 注解的 Map 参数进行处理。
     *
     * <p>执行逻辑：</p>
     * <ol>
     *   <li>获取方法参数及注解信息</li>
     *   <li>遍历所有参数，检查是否带有 @SanitizeMap 注解</li>
     *   <li>如果参数是 Map 类型且有注解，则进行字段清理</li>
     * </ol>
     *
     * @param joinPoint 切点信息
     * @return 方法执行结果
     */
    @SneakyThrows
    @Around("execution(* com.java2nb.*.dao.*Dao.list*(..))")
    public Object sanitizeMapParameters(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        for (int i = 0; i < parameterAnnotations.length; i++) {
            boolean hasAnnotation = Arrays.stream(parameterAnnotations[i])
                .anyMatch(a -> a.annotationType().equals(SanitizeMap.class));

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

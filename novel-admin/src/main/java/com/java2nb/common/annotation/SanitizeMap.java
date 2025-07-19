package com.java2nb.common.annotation;

import java.lang.annotation.*;

/**
 * 标记某个方法参数需要进行 Map 字段的清理和标准化处理。
 *
 * <p>通常用于 DAO 接口中 list 方法的 Map 参数，用于防止非法排序字段或排序顺序。</p>
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SanitizeMap {
}

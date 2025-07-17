package com.java2nb.common.annotation;

import java.lang.annotation.*;

/**
 * @author xiongxiaoyang
 * @date 2025/7/17
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidateSortOrder {
}

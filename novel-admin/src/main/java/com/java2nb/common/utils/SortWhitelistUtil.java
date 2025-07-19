package com.java2nb.common.utils;

import lombok.experimental.UtilityClass;

import java.util.Set;

/**
 * 排序字段和排序顺序的白名单工具类。
 */
@UtilityClass
public class SortWhitelistUtil {

    // 白名单字段
    private static final Set<String> ALLOWED_COLUMNS = Set.of("id", "name", "order_num");

    // 白名单排序方式
    private static final Set<String> ALLOWED_ORDERS = Set.of("asc", "desc");

    /**
     * 对排序字段进行白名单过滤和标准化。
     *
     * @param column 原始字段名
     * @return 安全的字段名，若非法则返回 null
     */
    public static String sanitizeColumn(String column) {
        if (column == null) return null;
        String lower = column.trim().toLowerCase();
        return ALLOWED_COLUMNS.contains(lower) ? lower : null;
    }

    /**
     * 对排序方式进行白名单过滤和标准化。
     *
     * @param order 原始排序方式
     * @return 安全的排序方式（"asc" 或 "desc"），若非法则返回 null
     */
    public static String sanitizeOrder(String order) {
        if (order == null) return null;
        String lower = order.trim().toLowerCase();
        return ALLOWED_ORDERS.contains(lower) ? lower : null;
    }

}
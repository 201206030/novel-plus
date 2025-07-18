package com.java2nb.common.utils;

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author xiongxiaoyang
 * @date 2025/7/17
 */
@UtilityClass
public class SortWhitelistUtil {

    private final Set<String> allowedColumns = new HashSet<>(
        Arrays.asList("id", "name", "order_num"));
    private final Set<String> allowedOrders = new HashSet<>(Arrays.asList("asc", "desc"));

    public String sanitizeColumn(String input) {
        return allowedColumns.contains(input.toLowerCase()) ? input.toLowerCase() : "id";
    }

    public String sanitizeOrder(String input) {
        return allowedOrders.contains(input.toLowerCase()) ? input.toLowerCase() : "asc";
    }
}
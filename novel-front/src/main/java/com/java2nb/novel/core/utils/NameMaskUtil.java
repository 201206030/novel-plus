package com.java2nb.novel.core.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * 用户名/昵称展示脱敏工具
 * <p>
 * 若昵称是手机号格式，隐藏中间四位；其它情况原样返回。
 *
 * @author 11797
 */
public final class NameMaskUtil {

    /**
     * 中国大陆手机号格式：1开头，第二位3-9，共11位
     */
    private static final Pattern PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");

    private static final String DEFAULT_DISPLAY = "匿名用户";

    private NameMaskUtil() {
    }

    /**
     * 判断字符串是否为手机号格式
     */
    public static boolean isPhone(String s) {
        return StringUtils.isNotBlank(s) && PHONE_PATTERN.matcher(s).matches();
    }

    /**
     * 展示用脱敏：空返回「匿名用户」；手机号中间四位打码；其它原样
     */
    public static String maskIfNeeded(String s) {
        if (StringUtils.isBlank(s)) {
            return DEFAULT_DISPLAY;
        }
        String trim = s.trim();
        if (isPhone(trim)) {
            return trim.substring(0, 3) + "****" + trim.substring(trim.length() - 4);
        }
        return trim;
    }
}

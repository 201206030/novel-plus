package com.java2nb.novel.core.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * 采集URL工具类，用于把书源中提取到的相对路径补全为绝对URL
 *
 * @author xiongxiaoyang
 */
public final class UrlUtil {

    private UrlUtil() {
    }

    /**
     * 判断是否绝对URL（http/https协议开头）
     */
    public static boolean isAbsolute(String url) {
        if (StringUtils.isBlank(url)) {
            return false;
        }
        String lower = url.trim().toLowerCase();
        return lower.startsWith("http://") || lower.startsWith("https://");
    }

    /**
     * 取URL的协议+域名部分，如 https://www.sudugu.co/sudugu/123/ -> https://www.sudugu.co
     */
    public static String origin(String url) {
        if (StringUtils.isBlank(url) || !isAbsolute(url)) {
            return null;
        }
        int schemeEnd = url.indexOf("://") + 3;
        int pathEnd = url.indexOf('/', schemeEnd);
        return pathEnd < 0 ? url : url.substring(0, pathEnd);
    }

    /**
     * 以baseUrl为基准把相对URL补全为绝对URL，已经是绝对URL或baseUrl为空时原样返回。
     * 支持 //开头的协议相对地址、/开头的站点根相对地址、以及普通相对地址。
     */
    public static String absolutize(String baseUrl, String url) {
        if (StringUtils.isBlank(url) || isAbsolute(url) || StringUtils.isBlank(baseUrl)) {
            return url;
        }
        url = url.trim();
        if (url.startsWith("//")) {
            // 协议相对地址，补齐baseUrl的协议
            int schemeEnd = baseUrl.indexOf("://");
            if (schemeEnd < 0) {
                return url;
            }
            return baseUrl.substring(0, schemeEnd) + ":" + url;
        }
        if (url.startsWith("/") && !url.startsWith("./")) {
            // 站点根相对地址
            return StringUtils.removeEnd(origin(baseUrl), "/") + url;
        }
        if (url.startsWith("./")) {
            url = url.substring(2);
        }
        // 普通相对地址，基于baseUrl所在目录拼接；baseUrl只有origin没有路径部分时，按站点根拼接
        int schemeEnd = baseUrl.indexOf("://");
        if (schemeEnd >= 0 && baseUrl.indexOf('/', schemeEnd + 3) < 0) {
            return StringUtils.removeEnd(origin(baseUrl), "/") + "/" + url;
        }
        int lastSlash = baseUrl.lastIndexOf('/');
        if (lastSlash < 0) {
            return baseUrl + "/" + url;
        }
        return baseUrl.substring(0, lastSlash + 1) + url;
    }

}

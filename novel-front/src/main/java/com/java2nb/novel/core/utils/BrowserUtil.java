package com.java2nb.novel.core.utils;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

public class BrowserUtil {

    // 浏览器类型
    public static final String[] mobileAgents = { "iphone", "android", "phone", "mobile", "wap", "netfront", "java",
            "opera mobi", "opera mini", "ucweb", "windows ce", "symbian", "series", "webos", "sony", "blackberry",
            "dopod", "nokia", "samsung", "palmsource", "xda", "pieplus", "meizu", "midp", "cldc", "motorola", "foma",
            "docomo", "up.browser", "up.link", "blazer", "helio", "hosin", "huawei", "novarra", "coolpad", "webos",
            "techfaith", "palmsource", "alcatel", "amoi", "ktouch", "nexian", "ericsson", "philips", "sagem", "wellcom",
            "bunjalloo", "maui", "smartphone", "iemobile", "spice", "bird", "zte-", "longcos", "pantech", "gionee",
            "portalmmm", "jig browser", "hiptop", "benq", "haier", "^lct", "320x320", "240x320", "176x220", "w3c ",
            "acs-", "alav", "alca", "amoi", "audi", "avan", "benq", "bird", "blac", "blaz", "brew", "cell", "cldc",
            "cmd-", "dang", "doco", "eric", "hipt", "inno", "ipaq", "java", "jigs", "kddi", "keji", "leno", "lg-c",
            "lg-d", "lg-g", "lge-", "maui", "maxo", "midp", "mits", "mmef", "mobi", "mot-", "moto", "mwbp", "nec-",
            "newt", "noki", "oper", "palm", "pana", "pant", "phil", "play", "port", "prox", "qwap", "sage", "sams",
            "sany", "sch-", "sec-", "send", "seri", "sgh-", "shar", "sie-", "siem", "smal", "smar", "sony", "sph-",
            "symb", "t-mo", "teli", "tim-",  "tsm-", "upg1", "upsi", "vk-v", "voda", "wap-", "wapa", "wapi",
            "wapp", "wapr", "webc", "winw", "winw", "xda", "xda-", "Googlebot-Mobile" };

    /**
     *
     * 判断是否是手机浏览器
     */
    public static boolean isMobile(HttpServletRequest request) {
        boolean isMobile = false;
        String userAgent = request.getHeader("User-Agent");
        if (userAgent != null) {
            userAgent = userAgent.toLowerCase();
            for (String mobileAgent : mobileAgents) {
                if (userAgent.indexOf(mobileAgent) > 0) {
                    isMobile = true;
                    break;
                }
            }
        }
        return isMobile;
    }
}

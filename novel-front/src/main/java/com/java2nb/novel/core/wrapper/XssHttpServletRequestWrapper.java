package com.java2nb.novel.core.wrapper;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Arrays;
import java.util.List;

/**
 * XSS过滤处理
 *
 * @author Administrator
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    /**
     * @param request
     */
    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values != null) {
            int length = values.length;
            String[] escapeValues = new String[length];
            for (int i = 0; i < length; i++) {
                escapeValues[i] = values[i].replaceAll("<", "&lt;").replaceAll(">", "&gt;");
            }
            return escapeValues;
        }
        return null;
    }
}

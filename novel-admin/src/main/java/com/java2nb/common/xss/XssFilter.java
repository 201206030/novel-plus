package com.java2nb.common.xss;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebFilter(filterName = "xssFilter", urlPatterns = "/*", asyncSupported = true)
@Component
public class XssFilter implements Filter {

    private static final List<String> NO_XSS_PATH = Arrays.asList("/common/generator/batchCode","/common/generator/batchDownload");

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // TODO Auto-generated method stub
        HttpServletRequest req = (HttpServletRequest) request;
        String reqURI = req.getRequestURI();
        if(NO_XSS_PATH.contains(reqURI)){
            chain.doFilter(request,response);
        }else {
            XssAndSqlHttpServletRequestWrapper xssRequestWrapper = new XssAndSqlHttpServletRequestWrapper(req);
            chain.doFilter(xssRequestWrapper, response);
        }
    }

    @Override
    public void init(FilterConfig arg0) {
        // TODO Auto-generated method stub
    }


}

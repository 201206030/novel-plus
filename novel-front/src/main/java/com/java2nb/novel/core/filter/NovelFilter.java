package com.java2nb.novel.core.filter;

import com.java2nb.novel.core.cache.CacheKey;
import com.java2nb.novel.core.cache.CacheService;
import com.java2nb.novel.core.utils.*;
import io.github.xxyopen.util.UUIDUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 项目核心过滤器
 * @author Administrator
 */
public class NovelFilter implements Filter {

    /**
     * 本地图片保存路径
     * */
    private String picSavePath;

    @Override
    public void init(FilterConfig filterConfig){
        picSavePath = filterConfig.getInitParameter("picSavePath");
    }


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        String requestUri = req.getRequestURI();

        //本地图片访问处理
        if (requestUri.contains(Constants.LOCAL_PIC_PREFIX)) {
            // 该分支由 Filter 直接读取文件并写出响应流，不会进入 Spring MVC 的 HandlerMapping，
            // 因此 WebMvcConfig.addCorsMappings 配置的 CorsInterceptor 不生效，必须在此手动补 CORS 头。
            String origin = req.getHeader("Origin");
            if (origin != null && !origin.isEmpty()) {
                resp.setHeader("Access-Control-Allow-Origin", origin);
                resp.setHeader("Vary", "Origin");
                resp.setHeader("Access-Control-Allow-Credentials", "true");
            }
            if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
                resp.setHeader("Access-Control-Allow-Methods", "GET, OPTIONS");
                resp.setHeader("Access-Control-Allow-Headers", "*");
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                return;
            }
            //缓存10天
            resp.setDateHeader("expires", System.currentTimeMillis()+60*60*24*10*1000);
            OutputStream out = resp.getOutputStream();
            InputStream input = new FileInputStream(picSavePath + requestUri);
            byte[] b = new byte[4096];
            for (int n; (n = input.read(b)) != -1; ) {
                out.write(b, 0, n);
            }
            input.close();
            out.close();
            return;

        }


        String userMark = CookieUtil.getCookie(req,Constants.USER_CLIENT_MARK_KEY);
        if(userMark == null){
            userMark = UUIDUtil.getUUID32();
            CookieUtil.setCookie(resp,Constants.USER_CLIENT_MARK_KEY,userMark);
        }
        ThreadLocalUtil.setClientId(userMark);
        //根据浏览器类型选择前端模板
        String to = req.getParameter("to");
        CacheService cacheService = SpringUtil.getBean(CacheService.class);
        if("pc".equals(to)){
            //直接进PC站
            cacheService.set(CacheKey.TEMPLATE_DIR_KEY+userMark,"",60*60*24);
        }else if("mobile".equals(to)){
            //直接进手机站
            cacheService.set(CacheKey.TEMPLATE_DIR_KEY+userMark,"mobile/",60*60*24);
        }else{
            //自动识别是PC站还是手机站
            if(BrowserUtil.isMobile(req)){
                //手机端访问
                ThreadLocalUtil.setTemplateDir("mobile/");
            }else{
                //PC端访问
                ThreadLocalUtil.setTemplateDir("");
            }
        }


        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}

package com.java2nb.novel.core.config;

import com.java2nb.novel.core.security.AdminLoginUser;
import com.java2nb.novel.core.security.AdminPasswordEncoder;
import com.java2nb.novel.core.security.AdminAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * SpringSecurity配置：登录用户来自 sys_user（多账号，见 AdminAuthService），
 * 页面级权限按模块前缀匹配 sys_menu.perms 形成的 authority（权限只分目录/菜单两级，不做方法级判定）；
 * 角色标识为 admin 的用户持有 ROLE_ADMIN，不受模块限制。
 *
 * @author Administrator
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    /**
     * 模块访问规则：URL 前缀 -> 所需权限标识（sys_menu.perms 中的值）。
     * 有序，命中第一条即生效；新增后台模块时在此登记。
     */
    private static final Map<String, String> MODULE_PERMS = new LinkedHashMap<>();

    /**
     * RBAC 自身维护入口（系统管理）：仅超管或被显式授予 sys:use 的角色可用
     */
    private static final String SYS_PERM = "sys:use";

    static {
        MODULE_PERMS.put("/book/**", "book:use");
        MODULE_PERMS.put("/bookList/**", "bookList:use");
        MODULE_PERMS.put("/comment/**", "comment:use");
        MODULE_PERMS.put("/member/**", "member:use");
        MODULE_PERMS.put("/message/**", "message:use");
        MODULE_PERMS.put("/order/**", "order:use");
        MODULE_PERMS.put("/author/**", "author:use");
        MODULE_PERMS.put("/ai/**", "ai:use");
        MODULE_PERMS.put("/news/**", "news:use");
        MODULE_PERMS.put("/website/**", "website:use");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new AdminPasswordEncoder();
    }

    /**
     * 显式声明认证管理器，避免 Spring Boot 在没有 UserDetailsService Bean 时
     * 自动创建内存账号并生成随机口令
     */
    @Bean
    public AuthenticationManager authenticationManager(AdminAuthenticationProvider adminAuthenticationProvider) {
        return new ProviderManager(adminAuthenticationProvider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   AdminAuthenticationProvider adminAuthenticationProvider) throws Exception {
        http
                .authenticationProvider(adminAuthenticationProvider)
                .csrf(csrf -> csrf.disable()) // 禁用 CSRF
                .authorizeHttpRequests(auth -> {
                    // 静态资源与登录/登出
                    auth.requestMatchers("/login.html", "/login", "/logout",
                            "/css/**", "/javascript/**", "/layui/**", "/images/**", "/favicon.ico", "/error").permitAll();
                    // 登录用户信息与菜单：任何已登录用户都要拿（前端据此渲染头部与侧边菜单）
                    auth.requestMatchers("/sys/auth/**").authenticated();
                    // 后台首页与统计：聚合数据，登录即可见，避免无权限角色打开首页被连环 403
                    auth.requestMatchers("/", "/index", "/index.html", "/stat/**", "/file/upload").authenticated();
                    // 系统管理（RBAC 维护）
                    auth.requestMatchers("/sys/**").hasAnyAuthority(AdminLoginUser.SUPER_ADMIN_AUTHORITY, SYS_PERM);
                    // 业务模块按权限标识放行
                    MODULE_PERMS.forEach((pattern, perm) ->
                            auth.requestMatchers(pattern).hasAnyAuthority(AdminLoginUser.SUPER_ADMIN_AUTHORITY, perm));
                    // 未登记的地址只要求登录；权限只分目录与菜单两级，有页面权限即有该页全部操作
                    auth.anyRequest().authenticated();
                })
                .formLogin(form -> form
                        .loginPage("/login.html") // 自定义登录页面
                        .loginProcessingUrl("/login") // 登录处理 URL
                        .defaultSuccessUrl("/") // 登录后回首页（有原始目标页时优先回目标页）
                        .failureUrl("/login.html?error") // 凭据错误回登录页并提示
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // 登出 URL
                        .logoutSuccessUrl("/login.html?logout") // 登出成功后跳转登录页
                )
                .exceptionHandling(ex -> ex
                        // 未登录：页面跳登录页，接口返回 code=1001 由 admin.js 统一跳转（登录过期也能自动跳回）
                        .authenticationEntryPoint(this::writeUnauthorized)
                        // 越权：页面跳登录页带 denied 提示，接口返回 code=9001，避开前端“网络异常”误报
                        .accessDeniedHandler(this::writeAccessDenied)
                );

        return http.build();
    }

    private void writeUnauthorized(HttpServletRequest request, HttpServletResponse response,
                                   AuthenticationException exception) throws IOException {
        if (isApiRequest(request)) {
            writeJson(response, HttpServletResponse.SC_UNAUTHORIZED, 1001, "登录已失效，请重新登录");
        } else {
            response.sendRedirect("/login.html");
        }
    }

    private void writeAccessDenied(HttpServletRequest request, HttpServletResponse response,
                                   AccessDeniedException exception) throws IOException {
        if (isApiRequest(request)) {
            writeJson(response, HttpServletResponse.SC_FORBIDDEN, 9001, "当前账号无权执行该操作");
        } else {
            response.sendRedirect("/login.html?denied");
        }
    }

    /**
     * ajax 请求一律当接口处理；其次非 .html / 非首页路径视为接口
     */
    private boolean isApiRequest(HttpServletRequest request) {
        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            return true;
        }
        String uri = request.getRequestURI();
        return !(uri.endsWith(".html") || uri.equals("/") || uri.isEmpty());
    }

    private void writeJson(HttpServletResponse response, int httpStatus, int code, String msg) throws IOException {
        response.setStatus(httpStatus);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"code\":" + code + ",\"msg\":\"" + msg + "\"}");
    }

}

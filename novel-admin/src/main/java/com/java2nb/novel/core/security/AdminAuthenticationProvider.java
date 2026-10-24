package com.java2nb.novel.core.security;

import com.java2nb.novel.service.AdminAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * 后台认证 Provider：直接以 sys_user 凭据校验（由 AdminAuthService 负责取数），
 * 不再支持配置文件账号兜底登录；相比默认 DaoAuthenticationProvider，
 * 认证取数与权限组装（角色/菜单权限）在同一入口完成。
 *
 * @author Administrator
 */
@Component
@RequiredArgsConstructor
public class AdminAuthenticationProvider implements AuthenticationProvider {

    private final AdminAuthService adminAuthService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String rawPassword = authentication.getCredentials() == null ? null : authentication.getCredentials().toString();
        AdminLoginUser user = adminAuthService.authenticate(username, rawPassword);
        if (user == null) {
            throw new BadCredentialsException("用户名或密码错误");
        }
        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

}

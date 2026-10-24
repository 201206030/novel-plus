package com.java2nb.novel.core.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * 后台登录用户：在标准 UserDetails 之外携带主键、显示名与超管标记，
 * 供菜单过滤与权限判断直接使用，避免二次查库。
 *
 * @author Administrator
 */
public class AdminLoginUser implements UserDetails {

    /**
     * 超管标记：来自角色标识 admin，拥有全部菜单与操作权限
     */
    public static final String SUPER_ADMIN_AUTHORITY = "ROLE_ADMIN";

    private final Long userId;

    private final String username;

    private final String displayName;

    private final String password;

    private final Collection<GrantedAuthority> authorities;

    private final boolean superAdmin;

    public AdminLoginUser(Long userId, String username, String displayName, String password,
                          Collection<GrantedAuthority> authorities, boolean superAdmin) {
        this.userId = userId;
        this.username = username;
        this.displayName = displayName;
        this.password = password;
        this.authorities = authorities;
        this.superAdmin = superAdmin;
    }

    public Long getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isSuperAdmin() {
        return superAdmin;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}

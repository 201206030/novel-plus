package com.java2nb.novel.service;

import com.java2nb.novel.core.security.AdminLoginUser;
import com.java2nb.novel.core.security.AdminPasswordEncoder;
import com.java2nb.novel.mapper.AdminSysQueryMapper;
import com.java2nb.novel.vo.SysMenuVO;
import com.java2nb.novel.vo.SysUserVO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 后台认证Service：以 sys_user 多账号登录，权限来自 sys_role（角色标识）
 * 与 sys_menu（perms 权限标识、url 可见页面）。
 *
 * @author Administrator
 */
@Service
@RequiredArgsConstructor
public class AdminAuthService {

    /**
     * 被视为超级管理员的角色标识
     */
    private static final String SUPER_ADMIN_ROLE_SIGN = "admin";

    /**
     * sys_menu 类型：0 目录（仅作分组标题），1 菜单/页面（勾中即拥有该页全部操作）
     */
    private static final int MENU_TYPE_DIR = 0;

    private static final int MENU_TYPE_PAGE = 1;

    /**
     * 菜单展示顺序：排序号小的在前，同排序号按主键，与前端菜单授权树用同一套规则
     */
    private static final Comparator<SysMenuVO> BY_ORDER = Comparator
            .comparing((SysMenuVO menu) -> menu.getOrderNum() == null ? 0 : menu.getOrderNum())
            .thenComparing(SysMenuVO::getMenuId);

    /**
     * 口令编码/校验统一实现（MD5，兼容 bcrypt 串）
     */
    private final AdminPasswordEncoder passwordEncoder = new AdminPasswordEncoder();
    
    private final AdminSysQueryMapper sysQueryMapper;
    
    /**
     * 认证入口：只按 sys_user 校验（表内账号才能登录）。
     *
     * @return 登录用户；凭据不符返回 null 交由调用方抛认证异常
     */
    public AdminLoginUser authenticate(String username, String rawPassword) {
        if (StringUtils.isBlank(username)) {
            return null;
        }
        SysUserVO sysUser = sysQueryMapper.getAuthUserByUsername(username.trim());
        if (sysUser != null && StringUtils.isNotBlank(sysUser.getPassword())
                && passwordEncoder.matches(rawPassword, sysUser.getPassword())) {
            return buildFromSysUser(sysUser);
        }
        return null;
    }

    private AdminLoginUser buildFromSysUser(SysUserVO sysUser) {
        List<String> roleSigns = sysQueryMapper.listRoleSignsByUser(sysUser.getUserId());
        Set<GrantedAuthority> authorities = new LinkedHashSet<>();
        boolean superAdmin = false;
        if (roleSigns != null) {
            for (String roleSign : roleSigns) {
                if (StringUtils.isBlank(roleSign)) {
                    continue;
                }
                String sign = roleSign.trim();
                authorities.add(new SimpleGrantedAuthority("ROLE_" + sign.toUpperCase()));
                if (SUPER_ADMIN_ROLE_SIGN.equalsIgnoreCase(sign)) {
                    superAdmin = true;
                    authorities.add(new SimpleGrantedAuthority(AdminLoginUser.SUPER_ADMIN_AUTHORITY));
                }
            }
        }
        // 菜单授权得到的权限标识，原样作为 authority（只供 URL 规则的 hasAuthority 判定，不再有方法级粒度）
        List<String> rawPerms = sysQueryMapper.listPermsByUser(sysUser.getUserId());
        if (rawPerms != null) {
            for (String raw : rawPerms) {
                if (StringUtils.isBlank(raw)) {
                    continue;
                }
                for (String perm : raw.split(",")) {
                    if (StringUtils.isNotBlank(perm)) {
                        authorities.add(new SimpleGrantedAuthority(perm.trim()));
                    }
                }
            }
        }
        String displayName = StringUtils.isNotBlank(sysUser.getName()) ? sysUser.getName() : sysUser.getUsername();
        return new AdminLoginUser(sysUser.getUserId(), sysUser.getUsername(), displayName,
                sysUser.getPassword(), new ArrayList<>(authorities), superAdmin);
    }

    /**
     * 当前登录用户，未登录/非后台用户返回 null
     */
    public AdminLoginUser current() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        return authentication.getPrincipal() instanceof AdminLoginUser user ? user : null;
    }

    /**
     * 后台左侧菜单：直接由 sys_menu 生成，与「角色管理-菜单授权」共用一份数据与一套排序，
     * 两边结构不会再各自漂移。页面可见性按授权判定；目录只当分组标题，
     * 其下没有可见页面时不输出，避免侧栏出现无法点开的空分组。
     *
     * @return 按「目录 -> 该目录下可见页面」顺序平铺的菜单，目录项 type=0，模板据此渲染分组
     */
    public List<SysMenuVO> currentSideMenus() {
        AdminLoginUser user = current();
        if (user == null) {
            return new ArrayList<>();
        }
        List<SysMenuVO> allMenus = sysQueryMapper.listMenus(null);
        if (allMenus == null || allMenus.isEmpty()) {
            return new ArrayList<>();
        }
        // null 代指超管不看授权表（超管固定拥有全部菜单）
        Set<Long> granted = null;
        if (!user.isSuperAdmin()) {
            granted = user.getUserId() == null ? new HashSet<>()
                    : new HashSet<>(sysQueryMapper.menuIdsByUser(user.getUserId()));
        }
        List<SysMenuVO> dirs = new ArrayList<>();
        List<SysMenuVO> pages = new ArrayList<>();
        Set<Long> dirIds = new HashSet<>(allMenus.size() * 2);
        for (SysMenuVO menu : allMenus) {
            if (isDir(menu)) {
                dirs.add(menu);
                dirIds.add(menu.getMenuId());
            } else if (isPage(menu) && (granted == null || granted.contains(menu.getMenuId()))) {
                pages.add(menu);
            }
        }
        // 分组与组内都按 order_num 排（同排序号按主键），与菜单授权弹窗的递归排序保持一致，
        // 不依赖 listMenus 的 parent_id 默认序，目录调序后两边仍会一起变
        dirs.sort(BY_ORDER);
        pages.sort(BY_ORDER);
        List<SysMenuVO> sidebar = new ArrayList<>(allMenus.size());
        for (SysMenuVO dir : dirs) {
            boolean printed = false;
            for (SysMenuVO page : pages) {
                if (!dir.getMenuId().equals(page.getParentId())) {
                    continue;
                }
                if (!printed) {
                    // 整组没有可见页面就不输出标题，避免侧栏出现点不动的空分组
                    sidebar.add(dir);
                    printed = true;
                }
                sidebar.add(page);
            }
        }
        // 父级不是目录（父级已删或挂在页面下）的可见页面平铺到末尾，有权限就能找到入口
        for (SysMenuVO page : pages) {
            if (page.getParentId() == null || !dirIds.contains(page.getParentId())) {
                sidebar.add(page);
            }
        }
        return sidebar;
    }

    private boolean isPage(SysMenuVO menu) {
        return menu.getType() != null && menu.getType() == MENU_TYPE_PAGE && StringUtils.isNotBlank(menu.getUrl());
    }

    private boolean isDir(SysMenuVO menu) {
        return menu.getType() != null && menu.getType() == MENU_TYPE_DIR;
    }

}

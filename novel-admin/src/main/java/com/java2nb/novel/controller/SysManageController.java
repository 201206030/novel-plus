package com.java2nb.novel.controller;

import com.java2nb.novel.core.security.AdminLoginUser;
import com.java2nb.novel.service.AdminAuthService;
import com.java2nb.novel.service.SysManageService;
import com.java2nb.novel.vo.SysMenuVO;
import com.java2nb.novel.vo.SysRoleVO;
import com.java2nb.novel.vo.SysUserVO;
import io.github.xxyopen.model.page.PageBean;
import io.github.xxyopen.model.resp.RestResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统管理Controller：系统用户/角色/菜单（权限）RBAC 维护
 *
 * 权限只分目录与菜单两级：能访问本页即拥有本页全部操作，所以写接口不再做方法级权限判定，
 * 统一由 SecurityConfiguration 的 /sys/** 规则拦截（超管或持有 sys:use 才能进来）。
 *
 * @author Administrator
 */
@RestController
@RequestMapping("sys")
@RequiredArgsConstructor
public class SysManageController {

    private final SysManageService sysManageService;

    private final AdminAuthService adminAuthService;

    // ---------------- 登录用户信息与菜单 ----------------

    /**
     * 当前登录用户（公共头部展示账号名）
     */
    @GetMapping("auth/info")
    public RestResult<Map<String, Object>> authInfo() {
        AdminLoginUser user = adminAuthService.current();
        Map<String, Object> info = new LinkedHashMap<>(4);
        if (user != null) {
            info.put("username", user.getUsername());
            info.put("displayName", user.getDisplayName());
            info.put("superAdmin", user.isSuperAdmin());
        }
        return RestResult.ok(info);
    }

    /**
     * 当前登录用户修改自己的密码：/sys/auth/** 只要求已登录，任何账号（含内置 admin）都可用，
     * 不要求系统管理页面权限
     */
    @PostMapping("auth/changePassword")
    public RestResult<Void> changeOwnPassword(@RequestParam String oldPassword,
        @RequestParam String newPassword) {
        sysManageService.changeOwnPassword(oldPassword, newPassword);
        return RestResult.ok();
    }

    // ---------------- 系统用户 ----------------

    @GetMapping("user/list")
    public RestResult<PageBean<SysUserVO>> userList(@RequestParam(required = false) String keyword,
        @RequestParam(required = false) Byte status,
        @RequestParam(value = "curr", defaultValue = "1") int page,
        @RequestParam(value = "limit", defaultValue = "10") int pageSize) {
        return RestResult.ok(sysManageService.userList(keyword, status, page, pageSize));
    }

    @GetMapping("user/get")
    public RestResult<SysUserVO> userGet(@RequestParam Long id) {
        return RestResult.ok(sysManageService.userGet(id));
    }

    /**
     * 分配角色弹窗数据（全部角色 + 已选角色ID）
     */
    @GetMapping("user/roles")
    public RestResult<Map<String, Object>> userRoles(@RequestParam Long id) {
        return RestResult.ok(sysManageService.userRoles(id));
    }

    @PostMapping("user/save")
    public RestResult<Void> userSave(@RequestParam String username,
        @RequestParam(required = false) String name,
        @RequestParam String password,
        @RequestParam(required = false) String email,
        @RequestParam(required = false) String mobile,
        @RequestParam(required = false) Byte status) {
        SysUserVO user = new SysUserVO();
        user.setUsername(username);
        user.setName(name);
        user.setPassword(password);
        user.setEmail(email);
        user.setMobile(mobile);
        user.setStatus(status);
        sysManageService.userCreate(user);
        return RestResult.ok();
    }

    @PostMapping("user/update")
    public RestResult<Void> userUpdate(@RequestParam Long id,
        @RequestParam(required = false) String username,
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String password,
        @RequestParam(required = false) String email,
        @RequestParam(required = false) String mobile,
        @RequestParam(required = false) Byte status) {
        SysUserVO user = new SysUserVO();
        user.setUserId(id);
        user.setUsername(username);
        user.setName(name);
        user.setPassword(password);
        user.setEmail(email);
        user.setMobile(mobile);
        user.setStatus(status);
        sysManageService.userUpdate(user);
        return RestResult.ok();
    }

    @PostMapping("user/status")
    public RestResult<Void> userStatus(@RequestParam Long id, @RequestParam Byte status) {
        sysManageService.userStatus(id, status);
        return RestResult.ok();
    }

    /**
     * 单条删除（admin.js 框架的删除按钮统一传 id）
     */
    @PostMapping("user/remove")
    public RestResult<Void> userRemoveOne(@RequestParam Long id) {
        sysManageService.userRemove(String.valueOf(id));
        return RestResult.ok();
    }

    @PostMapping("user/batchRemove")
    public RestResult<Void> userRemove(@RequestParam String ids) {
        sysManageService.userRemove(ids);
        return RestResult.ok();
    }

    @PostMapping("user/assignRoles")
    public RestResult<Void> userAssignRoles(@RequestParam Long userId,
        @RequestParam(required = false) String roleIds) {
        sysManageService.userAssignRoles(userId, roleIds);
        return RestResult.ok();
    }

    // ---------------- 角色 ----------------

    @GetMapping("role/list")
    public RestResult<PageBean<SysRoleVO>> roleList(@RequestParam(required = false) String keyword,
        @RequestParam(value = "curr", defaultValue = "1") int page,
        @RequestParam(value = "limit", defaultValue = "10") int pageSize) {
        return RestResult.ok(sysManageService.roleList(keyword, page, pageSize));
    }

    @GetMapping("role/get")
    public RestResult<SysRoleVO> roleGet(@RequestParam Long id) {
        return RestResult.ok(sysManageService.roleGet(id));
    }

    /**
     * 菜单授权弹窗数据（全部菜单 + 已选菜单ID）
     */
    @GetMapping("role/menus")
    public RestResult<Map<String, Object>> roleMenus(@RequestParam Long id) {
        return RestResult.ok(sysManageService.roleMenus(id));
    }

    @PostMapping("role/save")
    public RestResult<Void> roleSave(@RequestParam String roleName,
        @RequestParam String roleSign,
        @RequestParam(required = false) String remark) {
        SysRoleVO role = new SysRoleVO();
        role.setRoleName(roleName);
        role.setRoleSign(roleSign);
        role.setRemark(remark);
        sysManageService.roleCreate(role);
        return RestResult.ok();
    }

    @PostMapping("role/update")
    public RestResult<Void> roleUpdate(@RequestParam Long roleId,
        @RequestParam(required = false) String roleName,
        @RequestParam(required = false) String roleSign,
        @RequestParam(required = false) String remark) {
        SysRoleVO role = new SysRoleVO();
        role.setRoleId(roleId);
        role.setRoleName(roleName);
        role.setRoleSign(roleSign);
        role.setRemark(remark);
        sysManageService.roleUpdate(role);
        return RestResult.ok();
    }

    @PostMapping("role/batchRemove")
    public RestResult<Void> roleRemove(@RequestParam String ids,
        @RequestParam(required = false, defaultValue = "false") Boolean force) {
        sysManageService.roleRemove(ids, force);
        return RestResult.ok();
    }

    @PostMapping("role/assignMenus")
    public RestResult<Void> roleAssignMenus(@RequestParam Long roleId,
        @RequestParam(required = false) String menuIds) {
        sysManageService.roleAssignMenus(roleId, menuIds);
        return RestResult.ok();
    }

    // ---------------- 菜单（权限） ----------------

    @GetMapping("menu/list")
    public RestResult<PageBean<SysMenuVO>> menuList(@RequestParam(required = false) String keyword,
        @RequestParam(value = "curr", defaultValue = "1") int page,
        @RequestParam(value = "limit", defaultValue = "10") int pageSize) {
        return RestResult.ok(sysManageService.menuList(keyword, page, pageSize));
    }

    @GetMapping("menu/get")
    public RestResult<SysMenuVO> menuGet(@RequestParam Long id) {
        return RestResult.ok(sysManageService.menuGet(id));
    }

    /**
     * 父菜单可选项（目录/菜单，过滤历史按钮数据）
     */
    @GetMapping("menu/parents")
    public RestResult<List<SysMenuVO>> menuParents() {
        return RestResult.ok(sysManageService.menuParents());
    }

    @PostMapping("menu/save")
    public RestResult<Void> menuSave(@RequestParam String name,
        @RequestParam Integer type,
        @RequestParam(required = false) Long parentId,
        @RequestParam(required = false) String url,
        @RequestParam(required = false) String perms,
        @RequestParam(required = false) String icon,
        @RequestParam(required = false) Integer orderNum) {
        SysMenuVO menu = new SysMenuVO();
        menu.setName(name);
        menu.setType(type);
        menu.setParentId(parentId);
        menu.setUrl(url);
        menu.setPerms(perms);
        menu.setIcon(icon);
        menu.setOrderNum(orderNum);
        sysManageService.menuCreate(menu);
        return RestResult.ok();
    }

    @PostMapping("menu/update")
    public RestResult<Void> menuUpdate(@RequestParam Long menuId,
        @RequestParam(required = false) String name,
        @RequestParam(required = false) Integer type,
        @RequestParam(required = false) Long parentId,
        @RequestParam(required = false) String url,
        @RequestParam(required = false) String perms,
        @RequestParam(required = false) String icon,
        @RequestParam(required = false) Integer orderNum) {
        SysMenuVO menu = new SysMenuVO();
        menu.setMenuId(menuId);
        menu.setName(name);
        menu.setType(type);
        menu.setParentId(parentId);
        menu.setUrl(url);
        menu.setPerms(perms);
        menu.setIcon(icon);
        menu.setOrderNum(orderNum);
        sysManageService.menuUpdate(menu);
        return RestResult.ok();
    }

    @PostMapping("menu/batchRemove")
    public RestResult<Void> menuRemove(@RequestParam String ids) {
        sysManageService.menuRemove(ids);
        return RestResult.ok();
    }

}

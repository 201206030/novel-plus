package com.java2nb.novel.service;

import com.github.pagehelper.PageHelper;
import com.java2nb.novel.core.security.AdminLoginUser;
import com.java2nb.novel.core.security.AdminPasswordEncoder;
import com.java2nb.novel.mapper.AdminSysCmdMapper;
import com.java2nb.novel.mapper.AdminSysQueryMapper;
import com.java2nb.novel.vo.SysMenuVO;
import com.java2nb.novel.vo.SysRoleVO;
import com.java2nb.novel.vo.SysUserVO;
import io.github.xxyopen.model.page.PageBean;
import io.github.xxyopen.model.page.builder.pagehelper.PageBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.java2nb.novel.core.utils.AdminUtil.biz;
import static com.java2nb.novel.core.utils.AdminUtil.parseIds;

/**
 * 系统管理Service：基于 sys_user / sys_role / sys_menu 的 RBAC 维护
 * （登录认证同样走 sys_user 多账号，此处为系统 RBAC 数据管理）
 *
 * @author Administrator
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysManageService {

    /**
     * 系统用户状态：禁用
     */
    private static final byte SYS_USER_STATUS_DISABLED = 0;

    /**
     * 系统用户状态：正常
     */
    private static final byte SYS_USER_STATUS_NORMAL = 1;

    /**
     * 系统内置账号用户名：不可修改/启停/删除/改角色（前端也整行锁定，后端是权威拦截）；
     * 其密码由该账号本人通过公共头部「修改密码」自助修改，不占用本特例
     */
    private static final String BUILTIN_ADMIN_USERNAME = "admin";

    /**
     * 超级管理员角色标识：固定拥有全部菜单，不可修改/删除/菜单授权
     */
    private static final String SUPER_ROLE_SIGN = "admin";

    /**
     * 口令编码器：与登录认证共用同一实现，保证写入的密码可被校验
     */
    private static final AdminPasswordEncoder PASSWORD_ENCODER = new AdminPasswordEncoder();

    private final AdminSysQueryMapper sysQueryMapper;

    private final AdminSysCmdMapper sysCmdMapper;

    private final AdminAuthService adminAuthService;

    // ---------------- 用户管理 ----------------

    public PageBean<SysUserVO> userList(String keyword, Byte status, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        List<SysUserVO> list = sysQueryMapper.listUsers(keyword, status);
        return PageBuilder.build(list);
    }

    public SysUserVO userGet(Long userId) {
        SysUserVO user = sysQueryMapper.getUser(userId);
        if (user == null) {
            throw biz("系统用户不存在");
        }
        return user;
    }

    public void userCreate(SysUserVO user) {
        if (StringUtils.isBlank(user.getUsername())) {
            throw biz("用户名不能为空");
        }
        if (StringUtils.isBlank(user.getPassword()) || user.getPassword().length() < 6) {
            throw biz("密码不能少于6位");
        }
        if (user.getStatus() == null) {
            user.setStatus(SYS_USER_STATUS_NORMAL);
        }
        if (sysQueryMapper.countUsername(user.getUsername().trim(), null) > 0) {
            throw biz("用户名已存在：" + user.getUsername());
        }
        user.setUsername(user.getUsername().trim());
        user.setPassword(PASSWORD_ENCODER.encode(user.getPassword()));
        sysCmdMapper.insertUser(user);
    }

    public void userUpdate(SysUserVO user) {
        SysUserVO exist = sysQueryMapper.getUser(user.getUserId());
        if (exist == null) {
            throw biz("系统用户不存在");
        }
        if (BUILTIN_ADMIN_USERNAME.equalsIgnoreCase(exist.getUsername())) {
            throw biz("系统内置账号 admin 不允许修改，如需改密码请用该账号登录后点右上角「修改密码」");
        }
        // 编辑表单里也有“状态”下拉，同样要拦住禁用自己，否则保存后就再也登不进了
        if (user.getStatus() != null && user.getStatus() == SYS_USER_STATUS_DISABLED && isSelf(user.getUserId())) {
            throw biz("不能禁用当前登录账号，否则将把自己关在门外");
        }
        if (StringUtils.isNotBlank(user.getUsername())
                && sysQueryMapper.countUsername(user.getUsername().trim(), user.getUserId()) > 0) {
            throw biz("用户名已存在：" + user.getUsername());
        }
        if (StringUtils.isNotBlank(user.getPassword())) {
            if (user.getPassword().length() < 6) {
                throw biz("密码不能少于6位");
            }
            user.setPassword(PASSWORD_ENCODER.encode(user.getPassword()));
        } else {
            // 留空表示不修改密码
            user.setPassword(null);
        }
        if (StringUtils.isNotBlank(user.getUsername())) {
            user.setUsername(user.getUsername().trim());
        }
        sysCmdMapper.updateUser(user);
    }

    public void userStatus(Long userId, Byte status) {
        if (status == null || (status != SYS_USER_STATUS_NORMAL && status != SYS_USER_STATUS_DISABLED)) {
            throw biz("状态参数非法，仅支持 0禁用/1正常");
        }
        SysUserVO exist = sysQueryMapper.getUser(userId);
        if (exist == null) {
            throw biz("系统用户不存在");
        }
        if (BUILTIN_ADMIN_USERNAME.equalsIgnoreCase(exist.getUsername())) {
            throw biz("系统内置账号 admin 不允许启用/禁用");
        }
        if (exist.getStatus() != null && exist.getStatus() == status) {
            throw biz(status == SYS_USER_STATUS_DISABLED ? "该用户已是禁用状态" : "该用户已是正常状态");
        }
        if (status == SYS_USER_STATUS_DISABLED && isSelf(userId)) {
            throw biz("不能禁用当前登录账号，否则将把自己关在门外");
        }
        sysCmdMapper.updateUserStatus(userId, status);
    }

    @Transactional(rollbackFor = Exception.class)
    public void userRemove(String ids) {
        List<Long> idList = parseIds(ids);
        if (idList.isEmpty()) {
            throw biz("请选择要删除的用户");
        }
        if (isSelf(idList)) {
            throw biz("不能删除当前登录账号");
        }
        for (Long id : idList) {
            if (isBuiltinUser(id)) {
                throw biz("系统内置账号 admin 不允许删除");
            }
        }
        // 先清关联角色再删用户，避免 sys_user_role 残留孤儿数据
        for (Long id : idList) {
            sysCmdMapper.deleteUserRoles(id);
        }
        sysCmdMapper.deleteUsers(idList);
    }

    /**
     * 分配角色弹窗数据：全部角色 + 用户已有角色ID
     */
    public Map<String, Object> userRoles(Long userId) {
        userGet(userId);
        Map<String, Object> result = new HashMap<>(2);
        result.put("roles", sysQueryMapper.listRoles(null));
        result.put("checked", sysQueryMapper.roleIdsByUser(userId));
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public void userAssignRoles(Long userId, String roleIds) {
        SysUserVO target = userGet(userId);
        if (BUILTIN_ADMIN_USERNAME.equalsIgnoreCase(target.getUsername())) {
            throw biz("系统内置账号 admin 的角色固定，不允许重新分配");
        }
        sysCmdMapper.deleteUserRoles(userId);
        for (Long roleId : parseIds(roleIds)) {
            if (sysQueryMapper.getRole(roleId) == null) {
                throw biz("角色不存在，ID：" + roleId);
            }
            sysCmdMapper.insertUserRole(userId, roleId);
        }
    }

    /**
     * 当前登录用户修改自己的密码：任何已登录账号（含内置 admin）都可用，
     * 不要求系统管理页面权限；需验证原密码，新密码加密后入库
     */
    public void changeOwnPassword(String oldPassword, String newPassword) {
        AdminLoginUser current = adminAuthService.current();
        if (current == null || current.getUserId() == null) {
            throw biz("登录状态已失效，请重新登录后再试");
        }
        if (StringUtils.isBlank(oldPassword)) {
            throw biz("请输入原密码");
        }
        if (StringUtils.isBlank(newPassword) || newPassword.length() < 6) {
            throw biz("新密码不能少于6位");
        }
        if (StringUtils.equals(oldPassword, newPassword)) {
            throw biz("新密码不能与原密码相同");
        }
        // 以库内口令为准校验原密码（getAuthUserByUsername 同时限定 status=1），
        // 不用会话里缓存的哈希，避免他人已改过密码时误判通过
        if (adminAuthService.authenticate(current.getUsername(), oldPassword) == null) {
            throw biz("原密码不正确");
        }
        SysUserVO pwdUpdate = new SysUserVO();
        pwdUpdate.setUserId(current.getUserId());
        pwdUpdate.setPassword(PASSWORD_ENCODER.encode(newPassword));
        sysCmdMapper.updateUser(pwdUpdate);
    }

    // ---------------- 角色管理 ----------------

    public PageBean<SysRoleVO> roleList(String keyword, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        List<SysRoleVO> list = sysQueryMapper.listRoles(keyword);
        return PageBuilder.build(list);
    }

    public SysRoleVO roleGet(Long roleId) {
        SysRoleVO role = sysQueryMapper.getRole(roleId);
        if (role == null) {
            throw biz("角色不存在");
        }
        return role;
    }

    public void roleCreate(SysRoleVO role) {
        if (StringUtils.isBlank(role.getRoleName())) {
            throw biz("角色名称不能为空");
        }
        if (StringUtils.isBlank(role.getRoleSign())) {
            throw biz("角色标识不能为空");
        }
        if (sysQueryMapper.countRoleSign(role.getRoleSign().trim(), null) > 0) {
            throw biz("角色标识已存在：" + role.getRoleSign());
        }
        role.setRoleName(role.getRoleName().trim());
        role.setRoleSign(role.getRoleSign().trim());
        sysCmdMapper.insertRole(role);
    }

    public void roleUpdate(SysRoleVO role) {
        // 角色创建后不提供修改（前端已去掉“修改”按钮）；如需调整请删除后重新新增
        throw biz("角色创建后不允许修改，请删除后重新新增");
    }

    @Transactional(rollbackFor = Exception.class)
    public void roleRemove(String ids, Boolean force) {
        List<Long> idList = parseIds(ids);
        if (idList.isEmpty()) {
            throw biz("请选择要删除的角色");
        }
        for (Long roleId : idList) {
            if (isSuperRole(roleId)) {
                throw biz("超级管理员角色不允许删除");
            }
            int userCount = sysQueryMapper.countUserByRole(roleId);
            if (userCount > 0 && !Boolean.TRUE.equals(force)) {
                throw biz("该角色已分配给 " + userCount + " 个用户，确认删除将同时解除这些分配");
            }
            // 级联清理：用户分配 + 菜单授权
            sysCmdMapper.deleteRoleUsersByRole(roleId);
            sysCmdMapper.deleteRoleMenus(roleId);
        }
        sysCmdMapper.deleteRoles(idList);
    }

    /**
     * 菜单授权弹窗数据：全部菜单 + 角色已有菜单ID
     */
    public Map<String, Object> roleMenus(Long roleId) {
        roleGet(roleId);
        Map<String, Object> result = new HashMap<>(2);
        result.put("menus", sysQueryMapper.listMenus(null));
        result.put("checked", sysQueryMapper.menuIdsByRole(roleId));
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public void roleAssignMenus(Long roleId, String menuIds) {
        SysRoleVO role = roleGet(roleId);
        if (SUPER_ROLE_SIGN.equalsIgnoreCase(role.getRoleSign())) {
            throw biz("超级管理员角色固定拥有全部权限，无需菜单授权");
        }
        sysCmdMapper.deleteRoleMenus(roleId);
        for (Long menuId : parseIds(menuIds)) {
            if (sysQueryMapper.getMenu(menuId) == null) {
                throw biz("菜单不存在，ID：" + menuId);
            }
            sysCmdMapper.insertRoleMenu(roleId, menuId);
        }
    }

    // ---------------- 菜单（权限）管理 ----------------

    public PageBean<SysMenuVO> menuList(String keyword, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        List<SysMenuVO> list = sysQueryMapper.listMenus(keyword);
        return PageBuilder.build(list);
    }

    /**
     * 可选父菜单（目录/菜单）；过滤 type=2 只为兼容尚未清理历史按钮数据的库
     */
    public List<SysMenuVO> menuParents() {
        List<SysMenuVO> list = sysQueryMapper.listMenus(null);
        list.removeIf(menu -> menu.getType() != null && menu.getType() == 2);
        return list;
    }

    public SysMenuVO menuGet(Long menuId) {
        SysMenuVO menu = sysQueryMapper.getMenu(menuId);
        if (menu == null) {
            throw biz("菜单不存在");
        }
        return menu;
    }

    // 权限管理只读：菜单/目录结构由 doc/sql 脚本统一维护，不提供新增/修改/删除（前端也已隐入操作入口）
    public void menuCreate(SysMenuVO menu) {
        throw biz("权限管理为只读，菜单/目录结构由数据库脚本统一维护");
    }

    public void menuUpdate(SysMenuVO menu) {
        throw biz("权限管理为只读，菜单/目录结构由数据库脚本统一维护");
    }

    public void menuRemove(String ids) {
        throw biz("权限管理为只读，菜单/目录结构由数据库脚本统一维护");
    }

    /**
     * 目标用户是否为当前登录账号（禁用/删除自我防护）
     */
    private boolean isSelf(Long userId) {
        var current = adminAuthService.current();
        return current != null && current.getUserId() != null && current.getUserId().equals(userId);
    }

    private boolean isSelf(List<Long> idList) {
        var current = adminAuthService.current();
        return current != null && current.getUserId() != null && idList.contains(current.getUserId());
    }

    /**
     * 目标用户是否为系统内置 admin 账号
     */
    private boolean isBuiltinUser(Long userId) {
        SysUserVO user = sysQueryMapper.getUser(userId);
        return user != null && BUILTIN_ADMIN_USERNAME.equalsIgnoreCase(user.getUsername());
    }

    /**
     * 目标角色是否为超管角色（role_sign=admin）
     */
    private boolean isSuperRole(Long roleId) {
        SysRoleVO role = sysQueryMapper.getRole(roleId);
        return role != null && SUPER_ROLE_SIGN.equalsIgnoreCase(role.getRoleSign());
    }

}

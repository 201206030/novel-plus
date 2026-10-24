package com.java2nb.novel.mapper;

import com.java2nb.novel.vo.SysMenuVO;
import com.java2nb.novel.vo.SysRoleVO;
import com.java2nb.novel.vo.SysUserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统管理（RBAC）查询Mapper：sys_user / sys_role / sys_menu 及关联表
 *
 * @author Administrator
 */
@Mapper
public interface AdminSysQueryMapper {

    /**
     * 系统用户列表（附带角色名拼接，不含密码）
     *
     * @param keyword 用户名/姓名/邮箱/手机号关键字（可空）
     * @param status  状态过滤（可空）0禁用 1正常
     * @return 用户列表
     */
    List<SysUserVO> listUsers(@Param("keyword") String keyword, @Param("status") Byte status);

    SysUserVO getUser(@Param("userId") Long userId);

    /**
     * 用户名是否已存在（excludeId 可空，编辑改名查重时排除自身）
     */
    int countUsername(@Param("username") String username, @Param("excludeId") Long excludeId);

    /**
     * 用户已有角色ID列表（分配角色弹窗回显）
     */
    List<Long> roleIdsByUser(@Param("userId") Long userId);

    List<SysRoleVO> listRoles(@Param("keyword") String keyword);

    SysRoleVO getRole(@Param("roleId") Long roleId);

    int countRoleSign(@Param("roleSign") String roleSign, @Param("excludeId") Long excludeId);

    /**
     * 角色已被分配的用户数（删除前校验）
     */
    int countUserByRole(@Param("roleId") Long roleId);

    /**
     * 角色已有菜单ID列表（授权弹窗回显）
     */
    List<Long> menuIdsByRole(@Param("roleId") Long roleId);

    /**
     * 菜单列表（含父菜单名），keyword 匹配名称/权限标识；全量传 null
     */
    List<SysMenuVO> listMenus(@Param("keyword") String keyword);

    SysMenuVO getMenu(@Param("menuId") Long menuId);

    /**
     * 子菜单数量（删除前校验）
     */
    int countChildMenu(@Param("menuId") Long menuId);

    // ---------------- 登录认证与授权取数 ----------------

    /**
     * 按用户名取启用中的系统用户（含密码，仅供认证使用）
     */
    SysUserVO getAuthUserByUsername(@Param("username") String username);

    /**
     * 用户拥有的角色标识（sys_role.role_sign）
     */
    List<String> listRoleSignsByUser(@Param("userId") Long userId);

    /**
     * 用户经角色授权得到的权限标识串（sys_menu.perms 原始值，多个用逗号分隔，由 Service 拆分去重）
     */
    List<String> listPermsByUser(@Param("userId") Long userId);

    /**
     * 用户经角色授权得到的菜单ID（目录+页面），侧栏与菜单授权共用同一份 sys_menu
     */
    List<Long> menuIdsByUser(@Param("userId") Long userId);

}

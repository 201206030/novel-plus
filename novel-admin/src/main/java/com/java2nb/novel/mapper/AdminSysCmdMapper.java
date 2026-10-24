package com.java2nb.novel.mapper;

import com.java2nb.novel.vo.SysMenuVO;
import com.java2nb.novel.vo.SysRoleVO;
import com.java2nb.novel.vo.SysUserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统管理（RBAC）写入Mapper
 *
 * @author Administrator
 */
@Mapper
public interface AdminSysCmdMapper {

    int insertUser(SysUserVO user);

    /**
     * 选择性更新（password 为空不更新）
     */
    int updateUser(SysUserVO user);

    int updateUserStatus(@Param("userId") Long userId, @Param("status") Byte status);

    int deleteUsers(@Param("idList") List<Long> idList);

    int deleteUserRoles(@Param("userId") Long userId);

    int insertUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);

    int insertRole(SysRoleVO role);

    int updateRole(SysRoleVO role);

    int deleteRoles(@Param("idList") List<Long> idList);

    int deleteRoleUsersByRole(@Param("roleId") Long roleId);

    int insertRoleMenu(@Param("roleId") Long roleId, @Param("menuId") Long menuId);

    int deleteRoleMenus(@Param("roleId") Long roleId);

    /**
     * 菜单被角色引用的记录数（删除菜单时级联清理前可日志留痕）
     */
    int deleteRoleMenusByMenu(@Param("menuId") Long menuId);

    int insertMenu(SysMenuVO menu);

    int updateMenu(SysMenuVO menu);

    int deleteMenu(@Param("menuId") Long menuId);

}

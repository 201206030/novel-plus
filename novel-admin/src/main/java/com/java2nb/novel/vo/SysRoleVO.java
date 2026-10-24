package com.java2nb.novel.vo;

import lombok.Data;

import java.util.Date;

/**
 * 系统角色管理VO（sys_role）
 *
 * @author Administrator
 */
@Data
public class SysRoleVO {

    private Long roleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色标识（如 admin）
     */
    private String roleSign;

    private String remark;

    private Date gmtCreate;

    private Date gmtModified;

    /**
     * 主键别名（admin.js 框架统一读写 id）
     */
    public Long getId() {
        return roleId;
    }

}

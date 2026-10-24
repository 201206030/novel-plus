package com.java2nb.novel.vo;

import lombok.Data;

import java.util.Date;

/**
 * 系统用户管理VO（sys_user；查询不返回 password，列表附带角色名）
 *
 * @author Administrator
 */
@Data
public class SysUserVO {

    private Long userId;

    /**
     * 用户名(登录名)
     */
    private String username;

    /**
     * 姓名
     */
    private String name;

    /**
     * 密码（仅新增/修改入参，查询永不返回；修改时留空表示不变）
     */
    private String password;

    private String email;

    private String mobile;

    /**
     * 状态 0:禁用，1:正常
     */
    private Byte status;

    /**
     * 所属角色名（逗号拼接，列表展示用）
     */
    private String roleNames;

    private Date gmtCreate;

    private Date gmtModified;

    /**
     * 主键别名（admin.js 框架统一读写 id）
     */
    public Long getId() {
        return userId;
    }

}

package com.java2nb.novel.vo;

import lombok.Data;

import java.util.Date;

/**
 * 系统菜单/权限管理VO（sys_menu）
 *
 * @author Administrator
 */
@Data
public class SysMenuVO {

    private Long menuId;

    /**
     * 父菜单ID，一级菜单为0
     */
    private Long parentId;

    /**
     * 父菜单名（列表展示用）
     */
    private String parentName;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单URL
     */
    private String url;

    /**
     * 授权标识，多个用逗号分隔（如 sys:user:user,sys:user:create）
     */
    private String perms;

    /**
     * 类型 0：目录 1：菜单 2：按钮
     */
    private Integer type;

    private String icon;

    /**
     * 排序
     */
    private Integer orderNum;

    private Date gmtCreate;

    private Date gmtModified;

    /**
     * 主键别名（admin.js 框架统一读写 id）
     */
    public Long getId() {
        return menuId;
    }

}

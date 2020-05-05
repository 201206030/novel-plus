package com.java2nb.common.domain;

import lombok.Data;

import java.util.List;

/**
 * 表数据
 *
 * @author chenshun
 * @email 1179705413@qq.com
 * @date 2019-09-25 15:09:21
 */
public class TableDO {
    //表的名称
    private String tableName;
    //表的备注
    private String comments;
    //表的主键
    private GenColumnsDO pk;
    //表的列名(不包含主键)
    private List<GenColumnsDO> columns;

    //类名(第一个字母大写)，如：sys_user => SysUser
    private String className;
    //类名(第一个字母小写)，如：sys_user => sysUser
    private String classname;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public GenColumnsDO getPk() {
        return pk;
    }

    public void setPk(GenColumnsDO pk) {
        this.pk = pk;
    }

    public List<GenColumnsDO> getColumns() {
        return columns;
    }

    public void setColumns(List<GenColumnsDO> columns) {
        this.columns = columns;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    @Override
    public String toString() {
        return "TableDO{" +
                "tableName='" + tableName + '\'' +
                ", comments='" + comments + '\'' +
                ", pk=" + pk +
                ", columns=" + columns +
                ", className='" + className + '\'' +
                ", classname='" + classname + '\'' +
                '}';
    }
}

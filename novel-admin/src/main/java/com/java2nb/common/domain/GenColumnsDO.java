package com.java2nb.common.domain;

import java.io.Serializable;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.java2nb.common.jsonserializer.LongToStringSerializer;
import lombok.Data;


/**
 * @author xiongxy
 * @email 1179705413@qq.com
 * @date 2019-11-22 18:03:46
 */
public class GenColumnsDO implements Serializable {
    private static final long serialVersionUID = 1L;

    //主键
    //java中的long能表示的范围比js中number大,也就意味着部分数值在js中存不下(变成不准确的值)
    //所以通过序列化成字符串来解决
    @JsonSerialize(using = LongToStringSerializer.class)
    private Long id;
    //表名
    private String tableName;
    //列名
    private String columnName;
    //列类型
    private String columnType;
    //映射java类型
    private String javaType;
    //列注释
    private String columnComment;
    //列排序（升序）
    private Integer columnSort;
    //列标签名
    private String columnLabel;
    //页面显示类型：1、文本框 2、下拉框 3、数值4、日期 5、文本域6、富文本 7、上传图片【单文件】 8、上传图片【多文件】9、上传文件【单文件】 10、上传文件【多文件】11、隐藏域 12、不显示
    private Integer pageType;
    //是否必填
    private Integer isRequired;
    //页面显示为下拉时使用，字典类型从字典表中取出
    private String dictType;

    // 属性名称(第一个字母大写)，如：user_name => UserName
    private String attrName;
    // 属性名称(第一个字母小写)，如：user_name => userName
    private String attrname;

    private String extra;

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public String getColumnComment() {
        return columnComment;
    }

    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment;
    }

    public Integer getColumnSort() {
        return columnSort;
    }

    public void setColumnSort(Integer columnSort) {
        this.columnSort = columnSort;
    }

    public String getColumnLabel() {
        return columnLabel;
    }

    public void setColumnLabel(String columnLabel) {
        this.columnLabel = columnLabel;
    }

    public Integer getPageType() {
        return pageType;
    }

    public void setPageType(Integer pageType) {
        this.pageType = pageType;
    }

    public Integer getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(Integer isRequired) {
        this.isRequired = isRequired;
    }

    public String getDictType() {
        return dictType;
    }

    public void setDictType(String dictType) {
        this.dictType = dictType;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public String getAttrname() {
        return attrname;
    }

    public void setAttrname(String attrname) {
        this.attrname = attrname;
    }
}

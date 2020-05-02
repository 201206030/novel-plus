package com.java2nb.novel.entity;

import java.io.Serializable;
import java.util.Date;
import javax.annotation.Generated;

public class FriendLink implements Serializable{
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Integer id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String linkName;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String linkUrl;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Byte sort;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Byte isOpen;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long createUserId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Date createTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long updateUserId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Date updateTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Integer getId() {
        return id;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setId(Integer id) {
        this.id = id;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getLinkName() {
        return linkName;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setLinkName(String linkName) {
        this.linkName = linkName == null ? null : linkName.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getLinkUrl() {
        return linkUrl;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl == null ? null : linkUrl.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Byte getSort() {
        return sort;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setSort(Byte sort) {
        this.sort = sort;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Byte getIsOpen() {
        return isOpen;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setIsOpen(Byte isOpen) {
        this.isOpen = isOpen;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Long getCreateUserId() {
        return createUserId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Date getCreateTime() {
        return createTime;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Long getUpdateUserId() {
        return updateUserId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Date getUpdateTime() {
        return updateTime;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
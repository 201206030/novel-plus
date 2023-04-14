package com.java2nb.novel.entity;

import java.util.Date;
import javax.annotation.Generated;

public class WebsiteInfo {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String name;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String domain;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String keyword;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String description;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String qq;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String logo;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String logoDark;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Date createTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long createUserId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Date updateTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long updateUserId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Long getId() {
        return id;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setId(Long id) {
        this.id = id;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getName() {
        return name;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getDomain() {
        return domain;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setDomain(String domain) {
        this.domain = domain == null ? null : domain.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getKeyword() {
        return keyword;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setKeyword(String keyword) {
        this.keyword = keyword == null ? null : keyword.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getDescription() {
        return description;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getQq() {
        return qq;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setQq(String qq) {
        this.qq = qq == null ? null : qq.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getLogo() {
        return logo;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setLogo(String logo) {
        this.logo = logo == null ? null : logo.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getLogoDark() {
        return logoDark;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setLogoDark(String logoDark) {
        this.logoDark = logoDark == null ? null : logoDark.trim();
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
    public Long getCreateUserId() {
        return createUserId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Date getUpdateTime() {
        return updateTime;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Long getUpdateUserId() {
        return updateUserId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }
}
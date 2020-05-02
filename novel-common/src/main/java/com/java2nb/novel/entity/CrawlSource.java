package com.java2nb.novel.entity;

import java.util.Date;
import javax.annotation.Generated;

public class CrawlSource {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Integer id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String sourceName;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String crawlRule;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Byte sourceStatus;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Date createTime;

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
    public String getSourceName() {
        return sourceName;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setSourceName(String sourceName) {
        this.sourceName = sourceName == null ? null : sourceName.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getCrawlRule() {
        return crawlRule;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setCrawlRule(String crawlRule) {
        this.crawlRule = crawlRule == null ? null : crawlRule.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Byte getSourceStatus() {
        return sourceStatus;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setSourceStatus(Byte sourceStatus) {
        this.sourceStatus = sourceStatus;
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
    public Date getUpdateTime() {
        return updateTime;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
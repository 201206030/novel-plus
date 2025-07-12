package com.java2nb.novel.entity;

import java.util.Date;
import javax.annotation.Generated;

public class BookCommentReply {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long commentId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String replyContent;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String location;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Byte auditStatus;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Date createTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long createUserId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Long getId() {
        return id;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setId(Long id) {
        this.id = id;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Long getCommentId() {
        return commentId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getReplyContent() {
        return replyContent;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent == null ? null : replyContent.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getLocation() {
        return location;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setLocation(String location) {
        this.location = location == null ? null : location.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Byte getAuditStatus() {
        return auditStatus;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setAuditStatus(Byte auditStatus) {
        this.auditStatus = auditStatus;
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
}
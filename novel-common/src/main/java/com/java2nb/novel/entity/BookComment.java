package com.java2nb.novel.entity;


import javax.annotation.Generated;
import java.util.Date;

public class BookComment {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long bookId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String commentContent;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Integer replyCount;

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
    public Long getBookId() {
        return bookId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getCommentContent() {
        return commentContent;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent == null ? null : commentContent.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Integer getReplyCount() {
        return replyCount;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setReplyCount(Integer replyCount) {
        this.replyCount = replyCount;
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
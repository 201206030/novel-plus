package com.java2nb.novel.entity;

import java.util.Date;
import javax.annotation.Generated;

public class Author {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long userId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String inviteCode;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String penName;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String telPhone;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String chatAccount;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String email;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Byte workDirection;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Byte status;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Date createTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Long getId() {
        return id;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setId(Long id) {
        this.id = id;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Long getUserId() {
        return userId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getInviteCode() {
        return inviteCode;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode == null ? null : inviteCode.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getPenName() {
        return penName;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setPenName(String penName) {
        this.penName = penName == null ? null : penName.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getTelPhone() {
        return telPhone;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setTelPhone(String telPhone) {
        this.telPhone = telPhone == null ? null : telPhone.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getChatAccount() {
        return chatAccount;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setChatAccount(String chatAccount) {
        this.chatAccount = chatAccount == null ? null : chatAccount.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getEmail() {
        return email;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Byte getWorkDirection() {
        return workDirection;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setWorkDirection(Byte workDirection) {
        this.workDirection = workDirection;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Byte getStatus() {
        return status;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setStatus(Byte status) {
        this.status = status;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Date getCreateTime() {
        return createTime;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
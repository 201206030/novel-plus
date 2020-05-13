package com.java2nb.novel.entity;

import java.util.Date;
import javax.annotation.Generated;

public class UserBuyRecord {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long userId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long bookId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String bookName;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long bookIndexId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String bookIndexName;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Integer buyAmount;

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
    public Long getBookId() {
        return bookId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getBookName() {
        return bookName;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setBookName(String bookName) {
        this.bookName = bookName == null ? null : bookName.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Long getBookIndexId() {
        return bookIndexId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setBookIndexId(Long bookIndexId) {
        this.bookIndexId = bookIndexId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getBookIndexName() {
        return bookIndexName;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setBookIndexName(String bookIndexName) {
        this.bookIndexName = bookIndexName == null ? null : bookIndexName.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Integer getBuyAmount() {
        return buyAmount;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setBuyAmount(Integer buyAmount) {
        this.buyAmount = buyAmount;
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
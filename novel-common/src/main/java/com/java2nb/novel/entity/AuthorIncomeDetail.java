package com.java2nb.novel.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import javax.annotation.Generated;

public class AuthorIncomeDetail {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long userId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long authorId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long bookId;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Date incomeDate;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Integer incomeAccount;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Integer incomeCount;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Integer incomeNumber;

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
    public Long getAuthorId() {
        return authorId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
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
    public Date getIncomeDate() {
        return incomeDate;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setIncomeDate(Date incomeDate) {
        this.incomeDate = incomeDate;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Integer getIncomeAccount() {
        return incomeAccount;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setIncomeAccount(Integer incomeAccount) {
        this.incomeAccount = incomeAccount;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Integer getIncomeCount() {
        return incomeCount;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setIncomeCount(Integer incomeCount) {
        this.incomeCount = incomeCount;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Integer getIncomeNumber() {
        return incomeNumber;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setIncomeNumber(Integer incomeNumber) {
        this.incomeNumber = incomeNumber;
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
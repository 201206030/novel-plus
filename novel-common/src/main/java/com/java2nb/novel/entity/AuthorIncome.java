package com.java2nb.novel.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import javax.annotation.Generated;

public class AuthorIncome {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long userId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long authorId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long bookId;

    @JsonFormat(pattern = "yyyy年MM月", timezone = "GMT+8")
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Date incomeMonth;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long preTaxIncome;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long afterTaxIncome;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Byte payStatus;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Byte confirmStatus;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String detail;

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
    public Date getIncomeMonth() {
        return incomeMonth;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setIncomeMonth(Date incomeMonth) {
        this.incomeMonth = incomeMonth;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Long getPreTaxIncome() {
        return preTaxIncome;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setPreTaxIncome(Long preTaxIncome) {
        this.preTaxIncome = preTaxIncome;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Long getAfterTaxIncome() {
        return afterTaxIncome;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setAfterTaxIncome(Long afterTaxIncome) {
        this.afterTaxIncome = afterTaxIncome;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Byte getPayStatus() {
        return payStatus;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setPayStatus(Byte payStatus) {
        this.payStatus = payStatus;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Byte getConfirmStatus() {
        return confirmStatus;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setConfirmStatus(Byte confirmStatus) {
        this.confirmStatus = confirmStatus;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getDetail() {
        return detail;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setDetail(String detail) {
        this.detail = detail == null ? null : detail.trim();
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
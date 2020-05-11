package com.java2nb.novel.entity;

import java.util.Date;
import javax.annotation.Generated;

public class OrderPay {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long outTradeNo;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String tradeNo;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Byte payChannel;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Integer totalAmount;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long userId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Byte payStatus;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Date createTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Date updateTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Long getId() {
        return id;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setId(Long id) {
        this.id = id;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Long getOutTradeNo() {
        return outTradeNo;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setOutTradeNo(Long outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getTradeNo() {
        return tradeNo;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo == null ? null : tradeNo.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Byte getPayChannel() {
        return payChannel;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setPayChannel(Byte payChannel) {
        this.payChannel = payChannel;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Integer getTotalAmount() {
        return totalAmount;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
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
    public Byte getPayStatus() {
        return payStatus;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setPayStatus(Byte payStatus) {
        this.payStatus = payStatus;
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
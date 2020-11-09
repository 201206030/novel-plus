package com.java2nb.novel.entity;

import java.io.Serializable;
import java.util.Date;
import javax.annotation.Generated;

public class Book implements Serializable{
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Byte workDirection;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Integer catId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String catName;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String picUrl;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String bookName;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long authorId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String authorName;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String bookDesc;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Float score;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Byte bookStatus;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long visitCount;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Integer wordCount;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Integer commentCount;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Integer yesterdayBuy;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long lastIndexId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String lastIndexName;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Date lastIndexUpdateTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Byte isVip;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Byte status;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Date updateTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Date createTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Integer crawlSourceId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String crawlBookId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Date crawlLastTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Byte crawlIsStop;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Long getId() {
        return id;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setId(Long id) {
        this.id = id;
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
    public Integer getCatId() {
        return catId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setCatId(Integer catId) {
        this.catId = catId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getCatName() {
        return catName;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setCatName(String catName) {
        this.catName = catName == null ? null : catName.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getPicUrl() {
        return picUrl;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl == null ? null : picUrl.trim();
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
    public Long getAuthorId() {
        return authorId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getAuthorName() {
        return authorName;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setAuthorName(String authorName) {
        this.authorName = authorName == null ? null : authorName.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getBookDesc() {
        return bookDesc;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setBookDesc(String bookDesc) {
        this.bookDesc = bookDesc == null ? null : bookDesc.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Float getScore() {
        return score;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setScore(Float score) {
        this.score = score;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Byte getBookStatus() {
        return bookStatus;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setBookStatus(Byte bookStatus) {
        this.bookStatus = bookStatus;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Long getVisitCount() {
        return visitCount;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setVisitCount(Long visitCount) {
        this.visitCount = visitCount;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Integer getWordCount() {
        return wordCount;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setWordCount(Integer wordCount) {
        this.wordCount = wordCount;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Integer getCommentCount() {
        return commentCount;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Integer getYesterdayBuy() {
        return yesterdayBuy;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setYesterdayBuy(Integer yesterdayBuy) {
        this.yesterdayBuy = yesterdayBuy;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Long getLastIndexId() {
        return lastIndexId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setLastIndexId(Long lastIndexId) {
        this.lastIndexId = lastIndexId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getLastIndexName() {
        return lastIndexName;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setLastIndexName(String lastIndexName) {
        this.lastIndexName = lastIndexName == null ? null : lastIndexName.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Date getLastIndexUpdateTime() {
        return lastIndexUpdateTime;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setLastIndexUpdateTime(Date lastIndexUpdateTime) {
        this.lastIndexUpdateTime = lastIndexUpdateTime;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Byte getIsVip() {
        return isVip;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setIsVip(Byte isVip) {
        this.isVip = isVip;
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
    public Date getUpdateTime() {
        return updateTime;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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
    public Integer getCrawlSourceId() {
        return crawlSourceId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setCrawlSourceId(Integer crawlSourceId) {
        this.crawlSourceId = crawlSourceId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getCrawlBookId() {
        return crawlBookId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setCrawlBookId(String crawlBookId) {
        this.crawlBookId = crawlBookId == null ? null : crawlBookId.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Date getCrawlLastTime() {
        return crawlLastTime;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setCrawlLastTime(Date crawlLastTime) {
        this.crawlLastTime = crawlLastTime;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Byte getCrawlIsStop() {
        return crawlIsStop;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setCrawlIsStop(Byte crawlIsStop) {
        this.crawlIsStop = crawlIsStop;
    }
}
package com.java2nb.novel.entity;

import java.util.Date;
import javax.annotation.Generated;

public class CrawlSingleTask {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Integer sourceId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String sourceName;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String sourceBookId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Integer catId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String bookName;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String authorName;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Byte taskStatus;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Byte excCount;

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
    public Integer getSourceId() {
        return sourceId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
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
    public String getSourceBookId() {
        return sourceBookId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setSourceBookId(String sourceBookId) {
        this.sourceBookId = sourceBookId == null ? null : sourceBookId.trim();
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
    public String getBookName() {
        return bookName;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setBookName(String bookName) {
        this.bookName = bookName == null ? null : bookName.trim();
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
    public Byte getTaskStatus() {
        return taskStatus;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setTaskStatus(Byte taskStatus) {
        this.taskStatus = taskStatus;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Byte getExcCount() {
        return excCount;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setExcCount(Byte excCount) {
        this.excCount = excCount;
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
package com.java2nb.novel.domain;

import java.io.Serializable;


import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.java2nb.common.jsonserializer.LongToStringSerializer;


import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;



/**
 * 小说目录表
 * 
 * @author xiongxy
 * @email 1179705413@qq.com
 * @date 2023-04-14 19:51:54
 */
public class BookIndexDO implements Serializable {
	private static final long serialVersionUID = 1L;

	
	//主键
		//java中的long能表示的范围比js中number大,也就意味着部分数值在js中存不下(变成不准确的值)
	//所以通过序列化成字符串来解决
	@JsonSerialize(using = LongToStringSerializer.class)
			private Long id;
	//小说ID
		//java中的long能表示的范围比js中number大,也就意味着部分数值在js中存不下(变成不准确的值)
	//所以通过序列化成字符串来解决
	@JsonSerialize(using = LongToStringSerializer.class)
			private Long bookId;
	//目录号
			private Integer indexNum;
	//目录名
			private String indexName;
	//字数
			private Integer wordCount;
	//是否收费，1：收费，0：免费
			private Integer isVip;
	//章节费用（屋币）
			private Integer bookPrice;
	//存储方式
			private String storageType;
	//
			@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
		private Date createTime;
	//
			@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
		private Date updateTime;

	/**
	 * 设置：主键
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：主键
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：小说ID
	 */
	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}
	/**
	 * 获取：小说ID
	 */
	public Long getBookId() {
		return bookId;
	}
	/**
	 * 设置：目录号
	 */
	public void setIndexNum(Integer indexNum) {
		this.indexNum = indexNum;
	}
	/**
	 * 获取：目录号
	 */
	public Integer getIndexNum() {
		return indexNum;
	}
	/**
	 * 设置：目录名
	 */
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
	/**
	 * 获取：目录名
	 */
	public String getIndexName() {
		return indexName;
	}
	/**
	 * 设置：字数
	 */
	public void setWordCount(Integer wordCount) {
		this.wordCount = wordCount;
	}
	/**
	 * 获取：字数
	 */
	public Integer getWordCount() {
		return wordCount;
	}
	/**
	 * 设置：是否收费，1：收费，0：免费
	 */
	public void setIsVip(Integer isVip) {
		this.isVip = isVip;
	}
	/**
	 * 获取：是否收费，1：收费，0：免费
	 */
	public Integer getIsVip() {
		return isVip;
	}
	/**
	 * 设置：章节费用（屋币）
	 */
	public void setBookPrice(Integer bookPrice) {
		this.bookPrice = bookPrice;
	}
	/**
	 * 获取：章节费用（屋币）
	 */
	public Integer getBookPrice() {
		return bookPrice;
	}
	/**
	 * 设置：存储方式
	 */
	public void setStorageType(String storageType) {
		this.storageType = storageType;
	}
	/**
	 * 获取：存储方式
	 */
	public String getStorageType() {
		return storageType;
	}
	/**
	 * 设置：
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 设置：
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
}

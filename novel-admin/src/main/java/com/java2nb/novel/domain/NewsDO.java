package com.java2nb.novel.domain;

import java.io.Serializable;


import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.java2nb.common.jsonserializer.LongToStringSerializer;


import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;



/**
 * 新闻表
 * 
 * @author xiongxy
 * @email 1179705413@qq.com
 * @date 2020-12-01 10:05:51
 */
public class NewsDO implements Serializable {
	private static final long serialVersionUID = 1L;

	
	//主键
		//java中的long能表示的范围比js中number大,也就意味着部分数值在js中存不下(变成不准确的值)
	//所以通过序列化成字符串来解决
	@JsonSerialize(using = LongToStringSerializer.class)
			private Long id;
	//类别ID
			private Integer catId;
	//分类名
			private String catName;
	//来源
			private String sourceName;
	//标题
			private String title;
	//内容
			private String content;
	//发布时间
			@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
		private Date createTime;
	//发布人ID
		//java中的long能表示的范围比js中number大,也就意味着部分数值在js中存不下(变成不准确的值)
	//所以通过序列化成字符串来解决
	@JsonSerialize(using = LongToStringSerializer.class)
			private Long createUserId;
	//更新时间
			@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
		private Date updateTime;
	//更新人ID
		//java中的long能表示的范围比js中number大,也就意味着部分数值在js中存不下(变成不准确的值)
	//所以通过序列化成字符串来解决
	@JsonSerialize(using = LongToStringSerializer.class)
			private Long updateUserId;

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
	 * 设置：类别ID
	 */
	public void setCatId(Integer catId) {
		this.catId = catId;
	}
	/**
	 * 获取：类别ID
	 */
	public Integer getCatId() {
		return catId;
	}
	/**
	 * 设置：分类名
	 */
	public void setCatName(String catName) {
		this.catName = catName;
	}
	/**
	 * 获取：分类名
	 */
	public String getCatName() {
		return catName;
	}
	/**
	 * 设置：来源
	 */
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	/**
	 * 获取：来源
	 */
	public String getSourceName() {
		return sourceName;
	}
	/**
	 * 设置：标题
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * 获取：标题
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * 设置：内容
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * 获取：内容
	 */
	public String getContent() {
		return content;
	}
	/**
	 * 设置：发布时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：发布时间
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 设置：发布人ID
	 */
	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}
	/**
	 * 获取：发布人ID
	 */
	public Long getCreateUserId() {
		return createUserId;
	}
	/**
	 * 设置：更新时间
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：更新时间
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
	/**
	 * 设置：更新人ID
	 */
	public void setUpdateUserId(Long updateUserId) {
		this.updateUserId = updateUserId;
	}
	/**
	 * 获取：更新人ID
	 */
	public Long getUpdateUserId() {
		return updateUserId;
	}
}

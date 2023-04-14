package com.java2nb.novel.domain;

import java.io.Serializable;


import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.java2nb.common.jsonserializer.LongToStringSerializer;


import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;



/**
 * 网站信息表
 * 
 * @author xiongxy
 * @email 1179705413@qq.com
 * @date 2023-04-14 11:05:43
 */
public class WebsiteInfoDO implements Serializable {
	private static final long serialVersionUID = 1L;

	
	//主键
		//java中的long能表示的范围比js中number大,也就意味着部分数值在js中存不下(变成不准确的值)
	//所以通过序列化成字符串来解决
	@JsonSerialize(using = LongToStringSerializer.class)
			private Long id;
	//网站名
			private String name;
	//网站域名
			private String domain;
	//SEO关键词
			private String keyword;
	//网站描述
			private String description;
	//站长QQ
			private String qq;
	//网站logo图片（默认）
			private String logo;
	//网站logo图片（深色）
			private String logoDark;
	//创建时间
			@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
		private Date createTime;
	//创建人ID
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
	 * 设置：网站名
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：网站名
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：网站域名
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}
	/**
	 * 获取：网站域名
	 */
	public String getDomain() {
		return domain;
	}
	/**
	 * 设置：SEO关键词
	 */
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	/**
	 * 获取：SEO关键词
	 */
	public String getKeyword() {
		return keyword;
	}
	/**
	 * 设置：网站描述
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * 获取：网站描述
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * 设置：站长QQ
	 */
	public void setQq(String qq) {
		this.qq = qq;
	}
	/**
	 * 获取：站长QQ
	 */
	public String getQq() {
		return qq;
	}
	/**
	 * 设置：网站logo图片（默认）
	 */
	public void setLogo(String logo) {
		this.logo = logo;
	}
	/**
	 * 获取：网站logo图片（默认）
	 */
	public String getLogo() {
		return logo;
	}
	/**
	 * 设置：网站logo图片（深色）
	 */
	public void setLogoDark(String logoDark) {
		this.logoDark = logoDark;
	}
	/**
	 * 获取：网站logo图片（深色）
	 */
	public String getLogoDark() {
		return logoDark;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 设置：创建人ID
	 */
	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}
	/**
	 * 获取：创建人ID
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

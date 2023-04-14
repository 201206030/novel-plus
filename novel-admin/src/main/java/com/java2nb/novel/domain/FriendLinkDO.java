package com.java2nb.novel.domain;

import java.io.Serializable;


import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.java2nb.common.jsonserializer.LongToStringSerializer;


import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;



/**
 * 
 * 
 * @author xiongxy
 * @email 1179705413@qq.com
 * @date 2023-04-14 15:12:25
 */
public class FriendLinkDO implements Serializable {
	private static final long serialVersionUID = 1L;

	
	//主键
			private Integer id;
	//链接名
			private String linkName;
	//链接url
			private String linkUrl;
	//排序号
			private Integer sort;
	//是否开启，0：不开启，1：开启
			private Integer isOpen;
	//创建人id
		//java中的long能表示的范围比js中number大,也就意味着部分数值在js中存不下(变成不准确的值)
	//所以通过序列化成字符串来解决
	@JsonSerialize(using = LongToStringSerializer.class)
			private Long createUserId;
	//创建时间
			@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
		private Date createTime;
	//更新者用户id
		//java中的long能表示的范围比js中number大,也就意味着部分数值在js中存不下(变成不准确的值)
	//所以通过序列化成字符串来解决
	@JsonSerialize(using = LongToStringSerializer.class)
			private Long updateUserId;
	//更新时间
			@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
		private Date updateTime;

	/**
	 * 设置：主键
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：主键
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置：链接名
	 */
	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}
	/**
	 * 获取：链接名
	 */
	public String getLinkName() {
		return linkName;
	}
	/**
	 * 设置：链接url
	 */
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	/**
	 * 获取：链接url
	 */
	public String getLinkUrl() {
		return linkUrl;
	}
	/**
	 * 设置：排序号
	 */
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	/**
	 * 获取：排序号
	 */
	public Integer getSort() {
		return sort;
	}
	/**
	 * 设置：是否开启，0：不开启，1：开启
	 */
	public void setIsOpen(Integer isOpen) {
		this.isOpen = isOpen;
	}
	/**
	 * 获取：是否开启，0：不开启，1：开启
	 */
	public Integer getIsOpen() {
		return isOpen;
	}
	/**
	 * 设置：创建人id
	 */
	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}
	/**
	 * 获取：创建人id
	 */
	public Long getCreateUserId() {
		return createUserId;
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
	 * 设置：更新者用户id
	 */
	public void setUpdateUserId(Long updateUserId) {
		this.updateUserId = updateUserId;
	}
	/**
	 * 获取：更新者用户id
	 */
	public Long getUpdateUserId() {
		return updateUserId;
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
}

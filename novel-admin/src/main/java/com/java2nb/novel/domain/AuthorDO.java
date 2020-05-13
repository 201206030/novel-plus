package com.java2nb.novel.domain;

import java.io.Serializable;


import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.java2nb.common.jsonserializer.LongToStringSerializer;


import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;



/**
 * 作者表
 * 
 * @author xiongxy
 * @email 1179705413@qq.com
 * @date 2020-05-13 11:16:51
 */
public class AuthorDO implements Serializable {
	private static final long serialVersionUID = 1L;

	
	//主键
		//java中的long能表示的范围比js中number大,也就意味着部分数值在js中存不下(变成不准确的值)
	//所以通过序列化成字符串来解决
	@JsonSerialize(using = LongToStringSerializer.class)
			private Long id;
	//用户ID
		//java中的long能表示的范围比js中number大,也就意味着部分数值在js中存不下(变成不准确的值)
	//所以通过序列化成字符串来解决
	@JsonSerialize(using = LongToStringSerializer.class)
			private Long userId;
	//邀请码
			private String inviteCode;
	//笔名
			private String penName;
	//手机号码
			private String telPhone;
	//QQ或微信账号
			private String chatAccount;
	//电子邮箱
			private String email;
	//作品方向，0：男频，1：女频
			private Integer workDirection;
	//创建时间
			@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
		private Date createTime;
	//0：正常，1：封禁
			private Integer status;

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
	 * 设置：用户ID
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * 获取：用户ID
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * 设置：邀请码
	 */
	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}
	/**
	 * 获取：邀请码
	 */
	public String getInviteCode() {
		return inviteCode;
	}
	/**
	 * 设置：笔名
	 */
	public void setPenName(String penName) {
		this.penName = penName;
	}
	/**
	 * 获取：笔名
	 */
	public String getPenName() {
		return penName;
	}
	/**
	 * 设置：手机号码
	 */
	public void setTelPhone(String telPhone) {
		this.telPhone = telPhone;
	}
	/**
	 * 获取：手机号码
	 */
	public String getTelPhone() {
		return telPhone;
	}
	/**
	 * 设置：QQ或微信账号
	 */
	public void setChatAccount(String chatAccount) {
		this.chatAccount = chatAccount;
	}
	/**
	 * 获取：QQ或微信账号
	 */
	public String getChatAccount() {
		return chatAccount;
	}
	/**
	 * 设置：电子邮箱
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * 获取：电子邮箱
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * 设置：作品方向，0：男频，1：女频
	 */
	public void setWorkDirection(Integer workDirection) {
		this.workDirection = workDirection;
	}
	/**
	 * 获取：作品方向，0：男频，1：女频
	 */
	public Integer getWorkDirection() {
		return workDirection;
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
	 * 设置：0：正常，1：封禁
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：0：正常，1：封禁
	 */
	public Integer getStatus() {
		return status;
	}
}

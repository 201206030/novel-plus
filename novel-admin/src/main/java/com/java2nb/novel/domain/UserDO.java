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
 * @date 2020-12-01 03:49:08
 */
public class UserDO implements Serializable {
	private static final long serialVersionUID = 1L;

	
	//主键
		//java中的long能表示的范围比js中number大,也就意味着部分数值在js中存不下(变成不准确的值)
	//所以通过序列化成字符串来解决
	@JsonSerialize(using = LongToStringSerializer.class)
			private Long id;
	//登录名
			private String username;
	//登录密码
			private String password;
	//昵称
			private String nickName;
	//用户头像
			private String userPhoto;
	//用户性别，0：男，1：女
			private Integer userSex;
	//账户余额
		//java中的long能表示的范围比js中number大,也就意味着部分数值在js中存不下(变成不准确的值)
	//所以通过序列化成字符串来解决
	@JsonSerialize(using = LongToStringSerializer.class)
			private Long accountBalance;
	//用户状态，0：正常
			private Integer status;
	//创建时间
			@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
		private Date createTime;
	//更新时间
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
	 * 设置：登录名
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * 获取：登录名
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * 设置：登录密码
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * 获取：登录密码
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * 设置：昵称
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	/**
	 * 获取：昵称
	 */
	public String getNickName() {
		return nickName;
	}
	/**
	 * 设置：用户头像
	 */
	public void setUserPhoto(String userPhoto) {
		this.userPhoto = userPhoto;
	}
	/**
	 * 获取：用户头像
	 */
	public String getUserPhoto() {
		return userPhoto;
	}
	/**
	 * 设置：用户性别，0：男，1：女
	 */
	public void setUserSex(Integer userSex) {
		this.userSex = userSex;
	}
	/**
	 * 获取：用户性别，0：男，1：女
	 */
	public Integer getUserSex() {
		return userSex;
	}
	/**
	 * 设置：账户余额
	 */
	public void setAccountBalance(Long accountBalance) {
		this.accountBalance = accountBalance;
	}
	/**
	 * 获取：账户余额
	 */
	public Long getAccountBalance() {
		return accountBalance;
	}
	/**
	 * 设置：用户状态，0：正常
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：用户状态，0：正常
	 */
	public Integer getStatus() {
		return status;
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

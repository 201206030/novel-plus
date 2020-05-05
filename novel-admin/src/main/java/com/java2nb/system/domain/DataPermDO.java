package com.java2nb.system.domain;

import java.io.Serializable;


import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.java2nb.common.jsonserializer.LongToStringSerializer;


import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;



/**
 * 数据权限管理
 * 
 * @author xiongxy
 * @email 1179705413@qq.com
 * @date 2019-11-25 11:40:03
 */
public class DataPermDO implements Serializable {
	private static final long serialVersionUID = 1L;

	
	//
		//java中的long能表示的范围比js中number大,也就意味着部分数值在js中存不下(变成不准确的值)
	//所以通过序列化成字符串来解决
	@JsonSerialize(using = LongToStringSerializer.class)
			private Long id;
	//权限名称
			private String name;
	//数据表名称
			private String tableName;
	//所属模块
			private String moduleName;
	//用户权限控制属性名
			private String crlAttrName;
	//数据表权限控制列名
			private String crlColumnName;
	//权限code，all_开头表示查看所有数据的权限，sup_开头表示查看下级数据的权限，own_开头表示查看本级数据的权限
			private String permCode;
	//排序
			private Integer orderNum;
	//创建时间
			@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
		private Date gmtCreate;
	//修改时间
			@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
		private Date gmtModified;

	/**
	 * 设置：
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：权限名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：权限名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：数据表名称
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	/**
	 * 获取：数据表名称
	 */
	public String getTableName() {
		return tableName;
	}
	/**
	 * 设置：所属模块
	 */
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	/**
	 * 获取：所属模块
	 */
	public String getModuleName() {
		return moduleName;
	}
	/**
	 * 设置：用户权限控制属性名
	 */
	public void setCrlAttrName(String crlAttrName) {
		this.crlAttrName = crlAttrName;
	}
	/**
	 * 获取：用户权限控制属性名
	 */
	public String getCrlAttrName() {
		return crlAttrName;
	}
	/**
	 * 设置：数据表权限控制列名
	 */
	public void setCrlColumnName(String crlColumnName) {
		this.crlColumnName = crlColumnName;
	}
	/**
	 * 获取：数据表权限控制列名
	 */
	public String getCrlColumnName() {
		return crlColumnName;
	}
	/**
	 * 设置：权限code，all_开头表示查看所有数据的权限，sup_开头表示查看下级数据的权限，own_开头表示查看本级数据的权限
	 */
	public void setPermCode(String permCode) {
		this.permCode = permCode;
	}
	/**
	 * 获取：权限code，all_开头表示查看所有数据的权限，sup_开头表示查看下级数据的权限，own_开头表示查看本级数据的权限
	 */
	public String getPermCode() {
		return permCode;
	}
	/**
	 * 设置：排序
	 */
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	/**
	 * 获取：排序
	 */
	public Integer getOrderNum() {
		return orderNum;
	}
	/**
	 * 设置：创建时间
	 */
	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getGmtCreate() {
		return gmtCreate;
	}
	/**
	 * 设置：修改时间
	 */
	public void setGmtModified(Date gmtModified) {
		this.gmtModified = gmtModified;
	}
	/**
	 * 获取：修改时间
	 */
	public Date getGmtModified() {
		return gmtModified;
	}
}

package com.java2nb.common.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 字典表
 * 
 * @author xiongxy
 * @email 1179705413@qq.com
 * @date 2019-09-29 18:28:07
 */
public class DictDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//编号
	private Long id;
	//标签名
	private String name;
	//数据值
	private String value;
	//类型
	private String type;
	//描述
	private String description;
	//排序（升序）
	private BigDecimal sort;
	//父级编号
	private Long parentId;
	//创建者
	private Integer createBy;
	//创建时间
	private Date createDate;
	//更新者
	private Long updateBy;
	//更新时间
	private Date updateDate;
	//备注信息
	private String remarks;
	//删除标记
	private String delFlag;

	/**
	 * 设置：编号
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：编号
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：标签名
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：标签名
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：数据值
	 */
	public void setValue(String value) {
		this.value = value;
	}
	/**
	 * 获取：数据值
	 */
	public String getValue() {
		return value;
	}
	/**
	 * 设置：类型
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * 获取：类型
	 */
	public String getType() {
		return type;
	}
	/**
	 * 设置：描述
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * 获取：描述
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * 设置：排序（升序）
	 */
	public void setSort(BigDecimal sort) {
		this.sort = sort;
	}
	/**
	 * 获取：排序（升序）
	 */
	public BigDecimal getSort() {
		return sort;
	}
	/**
	 * 设置：父级编号
	 */
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	/**
	 * 获取：父级编号
	 */
	public Long getParentId() {
		return parentId;
	}
	/**
	 * 设置：创建者
	 */
	public void setCreateBy(Integer createBy) {
		this.createBy = createBy;
	}
	/**
	 * 获取：创建者
	 */
	public Integer getCreateBy() {
		return createBy;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreateDate() {
		return createDate;
	}
	/**
	 * 设置：更新者
	 */
	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}
	/**
	 * 获取：更新者
	 */
	public Long getUpdateBy() {
		return updateBy;
	}
	/**
	 * 设置：更新时间
	 */
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	/**
	 * 获取：更新时间
	 */
	public Date getUpdateDate() {
		return updateDate;
	}
	/**
	 * 设置：备注信息
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	/**
	 * 获取：备注信息
	 */
	public String getRemarks() {
		return remarks;
	}
	/**
	 * 设置：删除标记
	 */
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	/**
	 * 获取：删除标记
	 */
	public String getDelFlag() {
		return delFlag;
	}

	@Override
	public String toString() {
		return "DictDO{" +
				"id=" + id +
				", name='" + name + '\'' +
				", value='" + value + '\'' +
				", type='" + type + '\'' +
				", description='" + description + '\'' +
				", sort=" + sort +
				", parentId=" + parentId +
				", createBy=" + createBy +
				", createDate=" + createDate +
				", updateBy=" + updateBy +
				", updateDate=" + updateDate +
				", remarks='" + remarks + '\'' +
				", delFlag='" + delFlag + '\'' +
				'}';
	}
}

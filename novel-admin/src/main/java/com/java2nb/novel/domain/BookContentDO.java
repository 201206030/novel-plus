package com.java2nb.novel.domain;

import java.io.Serializable;


import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.java2nb.common.jsonserializer.LongToStringSerializer;


import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;



/**
 * 小说内容表
 * 
 * @author xiongxy
 * @email 1179705413@qq.com
 * @date 2023-04-14 19:52:06
 */
public class BookContentDO implements Serializable {
	private static final long serialVersionUID = 1L;

	
	//主键
		//java中的long能表示的范围比js中number大,也就意味着部分数值在js中存不下(变成不准确的值)
	//所以通过序列化成字符串来解决
	@JsonSerialize(using = LongToStringSerializer.class)
			private Long id;
	//目录ID
		//java中的long能表示的范围比js中number大,也就意味着部分数值在js中存不下(变成不准确的值)
	//所以通过序列化成字符串来解决
	@JsonSerialize(using = LongToStringSerializer.class)
			private Long indexId;
	//小说章节内容
			private String content;

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
	 * 设置：目录ID
	 */
	public void setIndexId(Long indexId) {
		this.indexId = indexId;
	}
	/**
	 * 获取：目录ID
	 */
	public Long getIndexId() {
		return indexId;
	}
	/**
	 * 设置：小说章节内容
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * 获取：小说章节内容
	 */
	public String getContent() {
		return content;
	}
}

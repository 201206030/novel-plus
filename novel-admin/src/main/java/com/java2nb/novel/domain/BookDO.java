package com.java2nb.novel.domain;

import java.io.Serializable;


import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.java2nb.common.jsonserializer.LongToStringSerializer;


import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;



/**
 * 小说表
 * 
 * @author xiongxy
 * @email 1179705413@qq.com
 * @date 2020-12-01 03:49:46
 */
public class BookDO implements Serializable {
	private static final long serialVersionUID = 1L;

	
	//主键
		//java中的long能表示的范围比js中number大,也就意味着部分数值在js中存不下(变成不准确的值)
	//所以通过序列化成字符串来解决
	@JsonSerialize(using = LongToStringSerializer.class)
			private Long id;
	//作品方向，0：男频，1：女频'
			private Integer workDirection;
	//分类ID
			private Integer catId;
	//分类名
			private String catName;
	//子分类ID
			private Integer catChildId;
	//子分类名
			private String catChildName;
	//小说封面
			private String picUrl;
	//小说名
			private String bookName;
	//男主角姓名
			private String heroName;
	//女主角姓名
			private String ladyName;
	//作品风格，0：甜宠，1：虐恋，2：其他
			private Integer bookStyle;
	//作品标签
			private String bookLabel;
	//作者id
		//java中的long能表示的范围比js中number大,也就意味着部分数值在js中存不下(变成不准确的值)
	//所以通过序列化成字符串来解决
	@JsonSerialize(using = LongToStringSerializer.class)
			private Long authorId;
	//作者名
			private String authorName;
	//书籍描述
			private String bookDesc;
	//评分，预留字段
			private Float score;
	//书籍状态，0：连载中，1：已完结
			private Integer bookStatus;
	//点击量
		//java中的long能表示的范围比js中number大,也就意味着部分数值在js中存不下(变成不准确的值)
	//所以通过序列化成字符串来解决
	@JsonSerialize(using = LongToStringSerializer.class)
			private Long visitCount;
	//总字数
			private Integer wordCount;
	//评论数
			private Integer commentCount;
	//昨日订阅数
			private Integer yesterdayBuy;
	//最新目录ID
		//java中的long能表示的范围比js中number大,也就意味着部分数值在js中存不下(变成不准确的值)
	//所以通过序列化成字符串来解决
	@JsonSerialize(using = LongToStringSerializer.class)
			private Long lastIndexId;
	//最新目录名
			private String lastIndexName;
	//最新目录更新时间
			@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
		private Date lastIndexUpdateTime;
	//是否收费，1：收费，0：免费
			private Integer isVip;
	//状态，0：入库，1：上架
			private Integer status;
	//更新时间
			@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
		private Date updateTime;
	//创建时间
			@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
		private Date createTime;
	//爬虫源站ID
			private Integer crawlSourceId;
	//抓取的源站小说ID
			private String crawlBookId;
	//最后一次的抓取时间
			@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
		private Date crawlLastTime;
	//是否已停止更新，0：未停止，1：已停止
			private Integer crawlIsStop;

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
	 * 设置：作品方向，0：男频，1：女频'
	 */
	public void setWorkDirection(Integer workDirection) {
		this.workDirection = workDirection;
	}
	/**
	 * 获取：作品方向，0：男频，1：女频'
	 */
	public Integer getWorkDirection() {
		return workDirection;
	}
	/**
	 * 设置：分类ID
	 */
	public void setCatId(Integer catId) {
		this.catId = catId;
	}
	/**
	 * 获取：分类ID
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
	 * 设置：子分类ID
	 */
	public void setCatChildId(Integer catChildId) {
		this.catChildId = catChildId;
	}
	/**
	 * 获取：子分类ID
	 */
	public Integer getCatChildId() {
		return catChildId;
	}
	/**
	 * 设置：子分类名
	 */
	public void setCatChildName(String catChildName) {
		this.catChildName = catChildName;
	}
	/**
	 * 获取：子分类名
	 */
	public String getCatChildName() {
		return catChildName;
	}
	/**
	 * 设置：小说封面
	 */
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	/**
	 * 获取：小说封面
	 */
	public String getPicUrl() {
		return picUrl;
	}
	/**
	 * 设置：小说名
	 */
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	/**
	 * 获取：小说名
	 */
	public String getBookName() {
		return bookName;
	}
	/**
	 * 设置：男主角姓名
	 */
	public void setHeroName(String heroName) {
		this.heroName = heroName;
	}
	/**
	 * 获取：男主角姓名
	 */
	public String getHeroName() {
		return heroName;
	}
	/**
	 * 设置：女主角姓名
	 */
	public void setLadyName(String ladyName) {
		this.ladyName = ladyName;
	}
	/**
	 * 获取：女主角姓名
	 */
	public String getLadyName() {
		return ladyName;
	}
	/**
	 * 设置：作品风格，0：甜宠，1：虐恋，2：其他
	 */
	public void setBookStyle(Integer bookStyle) {
		this.bookStyle = bookStyle;
	}
	/**
	 * 获取：作品风格，0：甜宠，1：虐恋，2：其他
	 */
	public Integer getBookStyle() {
		return bookStyle;
	}
	/**
	 * 设置：作品标签
	 */
	public void setBookLabel(String bookLabel) {
		this.bookLabel = bookLabel;
	}
	/**
	 * 获取：作品标签
	 */
	public String getBookLabel() {
		return bookLabel;
	}
	/**
	 * 设置：作者id
	 */
	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}
	/**
	 * 获取：作者id
	 */
	public Long getAuthorId() {
		return authorId;
	}
	/**
	 * 设置：作者名
	 */
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	/**
	 * 获取：作者名
	 */
	public String getAuthorName() {
		return authorName;
	}
	/**
	 * 设置：书籍描述
	 */
	public void setBookDesc(String bookDesc) {
		this.bookDesc = bookDesc;
	}
	/**
	 * 获取：书籍描述
	 */
	public String getBookDesc() {
		return bookDesc;
	}
	/**
	 * 设置：评分，预留字段
	 */
	public void setScore(Float score) {
		this.score = score;
	}
	/**
	 * 获取：评分，预留字段
	 */
	public Float getScore() {
		return score;
	}
	/**
	 * 设置：书籍状态，0：连载中，1：已完结
	 */
	public void setBookStatus(Integer bookStatus) {
		this.bookStatus = bookStatus;
	}
	/**
	 * 获取：书籍状态，0：连载中，1：已完结
	 */
	public Integer getBookStatus() {
		return bookStatus;
	}
	/**
	 * 设置：点击量
	 */
	public void setVisitCount(Long visitCount) {
		this.visitCount = visitCount;
	}
	/**
	 * 获取：点击量
	 */
	public Long getVisitCount() {
		return visitCount;
	}
	/**
	 * 设置：总字数
	 */
	public void setWordCount(Integer wordCount) {
		this.wordCount = wordCount;
	}
	/**
	 * 获取：总字数
	 */
	public Integer getWordCount() {
		return wordCount;
	}
	/**
	 * 设置：评论数
	 */
	public void setCommentCount(Integer commentCount) {
		this.commentCount = commentCount;
	}
	/**
	 * 获取：评论数
	 */
	public Integer getCommentCount() {
		return commentCount;
	}
	/**
	 * 设置：昨日订阅数
	 */
	public void setYesterdayBuy(Integer yesterdayBuy) {
		this.yesterdayBuy = yesterdayBuy;
	}
	/**
	 * 获取：昨日订阅数
	 */
	public Integer getYesterdayBuy() {
		return yesterdayBuy;
	}
	/**
	 * 设置：最新目录ID
	 */
	public void setLastIndexId(Long lastIndexId) {
		this.lastIndexId = lastIndexId;
	}
	/**
	 * 获取：最新目录ID
	 */
	public Long getLastIndexId() {
		return lastIndexId;
	}
	/**
	 * 设置：最新目录名
	 */
	public void setLastIndexName(String lastIndexName) {
		this.lastIndexName = lastIndexName;
	}
	/**
	 * 获取：最新目录名
	 */
	public String getLastIndexName() {
		return lastIndexName;
	}
	/**
	 * 设置：最新目录更新时间
	 */
	public void setLastIndexUpdateTime(Date lastIndexUpdateTime) {
		this.lastIndexUpdateTime = lastIndexUpdateTime;
	}
	/**
	 * 获取：最新目录更新时间
	 */
	public Date getLastIndexUpdateTime() {
		return lastIndexUpdateTime;
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
	 * 设置：状态，0：入库，1：上架
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：状态，0：入库，1：上架
	 */
	public Integer getStatus() {
		return status;
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
	 * 设置：爬虫源站ID
	 */
	public void setCrawlSourceId(Integer crawlSourceId) {
		this.crawlSourceId = crawlSourceId;
	}
	/**
	 * 获取：爬虫源站ID
	 */
	public Integer getCrawlSourceId() {
		return crawlSourceId;
	}
	/**
	 * 设置：抓取的源站小说ID
	 */
	public void setCrawlBookId(String crawlBookId) {
		this.crawlBookId = crawlBookId;
	}
	/**
	 * 获取：抓取的源站小说ID
	 */
	public String getCrawlBookId() {
		return crawlBookId;
	}
	/**
	 * 设置：最后一次的抓取时间
	 */
	public void setCrawlLastTime(Date crawlLastTime) {
		this.crawlLastTime = crawlLastTime;
	}
	/**
	 * 获取：最后一次的抓取时间
	 */
	public Date getCrawlLastTime() {
		return crawlLastTime;
	}
	/**
	 * 设置：是否已停止更新，0：未停止，1：已停止
	 */
	public void setCrawlIsStop(Integer crawlIsStop) {
		this.crawlIsStop = crawlIsStop;
	}
	/**
	 * 获取：是否已停止更新，0：未停止，1：已停止
	 */
	public Integer getCrawlIsStop() {
		return crawlIsStop;
	}
}

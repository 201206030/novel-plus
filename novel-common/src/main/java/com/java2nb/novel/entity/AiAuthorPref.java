package com.java2nb.novel.entity;

import lombok.Data;

import java.util.Date;

/**
 * AI创作全局偏好
 *
 * @author 11797
 */
@Data
public class AiAuthorPref {

    private Long id;

    /**
     * 作者ID
     */
    private Long authorId;

    /**
     * 全局创作偏好JSON（常用题材/叙事视角/文风/章节长度习惯等）
     */
    private String prefContent;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}

package com.java2nb.novel.entity;

import lombok.Data;

import java.util.Date;

/**
 * 书籍段落评论
 *
 * @author 11797
 */
@Data
public class BookParagraphComment {

    private Long id;

    /**
     * 书籍ID
     */
    private Long bookId;

    /**
     * 章节ID
     */
    private Long bookIndexId;

    /**
     * 段落序号（前端按正文拆分顺序，从0开始）
     */
    private Integer paragraphIdx;

    /**
     * 所属顶层评论ID，0表示顶层评论
     */
    private Long rootId;

    /**
     * 被回复人昵称（回复时记录）
     */
    private String replyUserName;

    /**
     * 回复数（冗余，仅顶层评论维护）
     */
    private Integer replyCount;

    /**
     * 评论内容
     */
    private String commentContent;

    /**
     * 数据来源ip所在地
     */
    private String location;

    /**
     * 创建用户ID
     */
    private Long createUserId;

    private Date createTime;
}

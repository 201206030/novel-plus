package com.java2nb.novel.entity;

import lombok.Data;

import java.util.Date;

/**
 * 用户消息
 *
 * @author 11797
 */
@Data
public class UserMessage {

    private Long id;

    /**
     * 接收用户ID
     */
    private Long userId;

    /**
     * 发送用户ID
     */
    private Long sendUserId;

    /**
     * 消息类型：1书评被回复 2段评被回复
     */
    private Byte msgType;

    /**
     * 书籍ID
     */
    private Long bookId;

    /**
     * 章节ID（段评消息跳转阅读页定位用）
     */
    private Long bookIndexId;

    /**
     * 被回复的主评论ID（书评ID或顶层段评ID）
     */
    private Long targetId;

    /**
     * 回复内容摘要
     */
    private String content;

    /**
     * 是否已读：0未读 1已读
     */
    private Byte readFlag;

    private Date createTime;

    /**
     * 书评被回复
     */
    public static final byte TYPE_BOOK_COMMENT_REPLY = 1;

    /**
     * 段评被回复
     */
    public static final byte TYPE_PARAGRAPH_REPLY = 2;
}

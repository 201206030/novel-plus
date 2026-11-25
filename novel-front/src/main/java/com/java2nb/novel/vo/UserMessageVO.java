package com.java2nb.novel.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.java2nb.novel.core.serialize.CommentUserNameSerialize;
import com.java2nb.novel.core.serialize.TimeAgoFormatSerialize;
import com.java2nb.novel.entity.UserMessage;
import lombok.Data;

import java.util.Date;

/**
 * 用户消息展示VO
 *
 * @author 11797
 */
@Data
public class UserMessageVO extends UserMessage {

    /**
     * 发送人昵称（脱敏展示）
     */
    @JsonSerialize(using = CommentUserNameSerialize.class)
    private String sendUserName;

    /**
     * 发送人头像
     */
    private String sendUserPhoto;

    /**
     * 关联书名
     */
    private String bookName;

    /**
     * 关联章节名（段评消息）
     */
    private String indexName;

    /**
     * 发送人是否作家账号（send_user_id 关联到 author 表）
     */
    private Boolean authorReply;

    /**
     * 作家笔名（作家回复时展示，替代用户中心昵称）
     */
    private String authorPenName;

    /**
     * 作家头像（作家回复时展示）
     */
    private String authorPicUrl;

    @JsonSerialize(using = TimeAgoFormatSerialize.class)
    private Date createTime;
}

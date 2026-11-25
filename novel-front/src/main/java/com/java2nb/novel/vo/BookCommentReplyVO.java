package com.java2nb.novel.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.java2nb.novel.core.serialize.CommentUserNameSerialize;
import com.java2nb.novel.core.serialize.TimeAgoFormatSerialize;
import com.java2nb.novel.entity.BookCommentReply;
import lombok.Data;

import java.util.Date;

/**
 * @author 11797
 */
@Data
public class BookCommentReplyVO extends BookCommentReply {

    @JsonSerialize(using = CommentUserNameSerialize.class)
    private String createUserName;

    private String createUserPhoto;

    @JsonSerialize(using = TimeAgoFormatSerialize.class)
    private Date createTime;

    private Long likesCount;

    private Long unLikesCount;

    /**
     * 是否作家回复（create_user_id 关联到 author 表即为作家账号）
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

    @Override
    public String toString() {
        return super.toString();
    }
}

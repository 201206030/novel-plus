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

    @Override
    public String toString() {
        return super.toString();
    }
}

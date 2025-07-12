package com.java2nb.novel.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.java2nb.novel.core.serialize.CommentUserNameSerialize;
import com.java2nb.novel.core.serialize.TimeAgoFormatSerialize;
import com.java2nb.novel.core.utils.DateUtil;
import com.java2nb.novel.entity.BookComment;
import lombok.Data;

import java.util.Date;

/**
 * @author 11797
 */
@Data
public class BookCommentVO extends BookComment {

    @JsonSerialize(using = CommentUserNameSerialize.class)
    private String createUserName;

    private String createUserPhoto;

    @JsonSerialize(using = TimeAgoFormatSerialize.class)
    private Date createTime;

    private Long likesCount;

    private Long unLikesCount;

    public String getCreateTimeFormat() {
        return DateUtil.formatTimeAgo(getCreateTime());
    }

    @Override
    public String toString() {
        return super.toString();
    }
}

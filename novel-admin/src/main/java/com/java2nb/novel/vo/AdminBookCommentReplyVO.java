package com.java2nb.novel.vo;

import com.java2nb.novel.entity.BookCommentReply;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 书评回复管理列表VO
 *
 * @author Administrator
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AdminBookCommentReplyVO extends BookCommentReply {

    /**
     * 回复用户昵称
     */
    private String createUserName;

}

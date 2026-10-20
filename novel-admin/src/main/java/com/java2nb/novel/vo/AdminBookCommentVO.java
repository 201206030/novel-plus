package com.java2nb.novel.vo;

import com.java2nb.novel.entity.BookComment;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 小说评论管理列表VO
 *
 * @author Administrator
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AdminBookCommentVO extends BookComment {

    /**
     * 书名
     */
    private String bookName;

    /**
     * 评论用户昵称
     */
    private String createUserName;

}

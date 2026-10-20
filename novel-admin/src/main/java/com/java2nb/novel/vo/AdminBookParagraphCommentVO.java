package com.java2nb.novel.vo;

import com.java2nb.novel.entity.BookParagraphComment;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 段评管理列表VO
 *
 * @author Administrator
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AdminBookParagraphCommentVO extends BookParagraphComment {

    /**
     * 书名
     */
    private String bookName;

    /**
     * 章节名
     */
    private String indexName;

    /**
     * 评论用户昵称
     */
    private String createUserName;

}

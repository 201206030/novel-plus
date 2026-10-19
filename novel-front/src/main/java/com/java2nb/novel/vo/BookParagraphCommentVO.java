package com.java2nb.novel.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.java2nb.novel.core.serialize.CommentUserNameSerialize;
import com.java2nb.novel.core.serialize.TimeAgoFormatSerialize;
import com.java2nb.novel.entity.BookParagraphComment;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 段落评论VO
 *
 * @author 11797
 */
@Data
public class BookParagraphCommentVO extends BookParagraphComment {

    /**
     * 评论人昵称（脱敏）
     */
    @JsonSerialize(using = CommentUserNameSerialize.class)
    private String createUserName;

    /**
     * 评论人头像
     */
    private String createUserPhoto;

    @JsonSerialize(using = TimeAgoFormatSerialize.class)
    private Date createTime;

    /**
     * 回复列表（仅顶层评论填充，按时间正序）
     */
    private List<BookParagraphCommentVO> replies;

    /**
     * 所属书籍名称
     */
    private String bookName;

    /**
     * 所属书籍封面
     */
    private String picUrl;

    /**
     * 所属章节名称
     */
    private String indexName;

    @Override
    public String toString() {
        return super.toString();
    }
}

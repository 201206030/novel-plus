package com.java2nb.novel.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.java2nb.novel.core.serialize.CommentUserNameSerialize;
import com.java2nb.novel.core.serialize.TimeAgoFormatSerialize;
import com.java2nb.novel.entity.UserBookList;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 书单VO
 *
 * @author 11797
 */
@Data
public class BookListVO extends UserBookList {

    /**
     * 创建人昵称（脱敏）
     */
    @JsonSerialize(using = CommentUserNameSerialize.class)
    private String createUserName;

    /**
     * 创建人头像
     */
    private String createUserPhoto;

    @JsonSerialize(using = TimeAgoFormatSerialize.class)
    private Date createTime;

    /**
     * 是否当前登录用户的书单（仅登录时返回）
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isMyList;

    /**
     * 书单条目列表（仅详情返回）
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<BookListItemVO> items;
}

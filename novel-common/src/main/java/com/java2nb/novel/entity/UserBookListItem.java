package com.java2nb.novel.entity;

import lombok.Data;

import java.util.Date;

/**
 * 书单条目
 *
 * @author 11797
 */
@Data
public class UserBookListItem {

    private Long id;

    /**
     * 书单ID
     */
    private Long listId;

    /**
     * 书籍ID
     */
    private Long bookId;

    /**
     * 推荐语
     */
    private String remark;

    private Date createTime;
}

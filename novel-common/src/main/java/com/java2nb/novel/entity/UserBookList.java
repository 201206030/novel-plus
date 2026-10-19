package com.java2nb.novel.entity;

import lombok.Data;

import java.util.Date;

/**
 * 用户书单
 *
 * @author 11797
 */
@Data
public class UserBookList {

    private Long id;

    /**
     * 创建用户ID
     */
    private Long userId;

    /**
     * 书单名
     */
    private String listName;

    /**
     * 书单简介
     */
    private String description;

    /**
     * 书籍数量(冗余)
     */
    private Integer bookCount;

    /**
     * 状态：1正常 0已删除
     */
    private Byte status;

    private Date createTime;

    private Date updateTime;
}

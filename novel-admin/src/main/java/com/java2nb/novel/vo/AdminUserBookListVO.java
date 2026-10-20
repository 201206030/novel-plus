package com.java2nb.novel.vo;

import com.java2nb.novel.entity.UserBookList;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 书单管理列表VO
 *
 * @author Administrator
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AdminUserBookListVO extends UserBookList {

    /**
     * 创建用户昵称
     */
    private String userName;

}

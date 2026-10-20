package com.java2nb.novel.vo;

import com.java2nb.novel.entity.UserBookListItem;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 书单明细列表VO
 *
 * @author Administrator
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AdminUserBookListItemVO extends UserBookListItem {

    /**
     * 书名
     */
    private String bookName;

    /**
     * 封面图
     */
    private String picUrl;

}

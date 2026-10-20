package com.java2nb.novel.vo;

import com.java2nb.novel.entity.BookSetting;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 小说推荐管理列表VO
 *
 * @author Administrator
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AdminBookSettingVO extends BookSetting {

    /**
     * 书名
     */
    private String bookName;

    /**
     * 作者名
     */
    private String authorName;

}

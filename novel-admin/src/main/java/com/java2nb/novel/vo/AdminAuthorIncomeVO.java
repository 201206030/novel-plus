package com.java2nb.novel.vo;

import com.java2nb.novel.entity.AuthorIncome;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 作家收入管理列表VO
 *
 * @author Administrator
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AdminAuthorIncomeVO extends AuthorIncome {

    /**
     * 作品书名
     */
    private String bookName;

    /**
     * 作家笔名
     */
    private String penName;

}

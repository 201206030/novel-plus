package com.java2nb.novel.vo;

import lombok.Data;

/**
 * 收入按作品汇总VO（作者后台收入明细页顶部卡片用）
 *
 * @author 11797
 */
@Data
public class BookIncomeSummaryVO {

    /**
     * 作品ID
     */
    private Long bookId;

    /**
     * 作品名
     */
    private String bookName;

    /**
     * 订阅总额（屋币）
     */
    private Long incomeAccount;

    /**
     * 订阅次数
     */
    private Long incomeCount;

    /**
     * 订阅人数
     */
    private Long incomeNumber;

}

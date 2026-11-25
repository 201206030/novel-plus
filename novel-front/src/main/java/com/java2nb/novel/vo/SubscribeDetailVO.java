package com.java2nb.novel.vo;

import lombok.Data;

/**
 * 移动端订阅明细展示 VO
 *
 * @author 11797
 */
@Data
public class SubscribeDetailVO {

    /**
     * 购买记录主键
     */
    private Long id;

    /**
     * 小说ID（按字符串返回，避免 JS 长整数精度丢失）
     */
    private String bookId;

    /**
     * 小说名
     */
    private String bookName;

    /**
     * 章节ID（按字符串返回，避免 JS 长整数精度丢失）
     */
    private String bookIndexId;

    /**
     * 章节名
     */
    private String bookIndexName;

    /**
     * 消费屋币数
     */
    private Integer buyAmount;

    /**
     * 购买时间
     */
    private String createTime;
}

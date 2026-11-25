package com.java2nb.novel.vo;

import lombok.Data;

/**
 * 移动端充值记录展示 VO
 *
 * @author 11797
 */
@Data
public class RechargeRecordVO {

    /**
     * 订单主键
     */
    private Long id;

    /**
     * 商户订单号（按字符串返回，避免 JS 长整数精度丢失）
     */
    private String outTradeNo;

    /**
     * 第三方交易号
     */
    private String tradeNo;

    /**
     * 支付渠道：1 支付宝
     */
    private Byte payChannel;

    /**
     * 充值金额（元）
     */
    private Integer totalAmount;

    /**
     * 支付状态：0 支付失败，1 支付成功，2 待支付
     */
    private Byte payStatus;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;
}

package com.java2nb.novel.vo;

import com.java2nb.novel.entity.OrderPay;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 订单管理列表VO
 *
 * @author Administrator
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AdminOrderPayVO extends OrderPay {

    /**
     * 下单用户昵称
     */
    private String userName;

}

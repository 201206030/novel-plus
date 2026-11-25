package com.java2nb.novel.service;

import com.java2nb.novel.vo.RechargeRecordVO;
import com.java2nb.novel.vo.SubscribeDetailVO;
import io.github.xxyopen.model.page.PageBean;

/**
 * 用户订单与消费记录服务
 *
 * @author 11797
 */
public interface UserOrderService {

    /**
     * 分页查询用户充值记录
     */
    PageBean<RechargeRecordVO> listRechargeRecordByPage(Long userId, int page, int pageSize);

    /**
     * 分页查询用户章节订阅明细
     */
    PageBean<SubscribeDetailVO> listSubscribeDetailByPage(Long userId, int page, int pageSize);
}

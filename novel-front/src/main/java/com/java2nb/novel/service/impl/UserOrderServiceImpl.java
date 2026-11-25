package com.java2nb.novel.service.impl;

import com.github.pagehelper.PageHelper;
import com.java2nb.novel.mapper.FrontUserOrderMapper;
import com.java2nb.novel.service.UserOrderService;
import com.java2nb.novel.vo.RechargeRecordVO;
import com.java2nb.novel.vo.SubscribeDetailVO;
import io.github.xxyopen.model.page.PageBean;
import io.github.xxyopen.model.page.builder.pagehelper.PageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 用户订单与消费记录服务实现
 *
 * @author 11797
 */
@Service
@RequiredArgsConstructor
public class UserOrderServiceImpl implements UserOrderService {

    private final FrontUserOrderMapper frontUserOrderMapper;

    @Override
    public PageBean<RechargeRecordVO> listRechargeRecordByPage(Long userId, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return PageBuilder.build(frontUserOrderMapper.listRechargeRecord(userId));
    }

    @Override
    public PageBean<SubscribeDetailVO> listSubscribeDetailByPage(Long userId, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return PageBuilder.build(frontUserOrderMapper.listSubscribeDetail(userId));
    }
}

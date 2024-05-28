package com.java2nb.novel.service.impl;

import com.java2nb.novel.entity.OrderPay;
import com.java2nb.novel.mapper.OrderPayDynamicSqlSupport;
import com.java2nb.novel.mapper.OrderPayMapper;
import com.java2nb.novel.service.OrderService;
import com.java2nb.novel.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.update.render.UpdateStatementProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;
import static org.mybatis.dynamic.sql.SqlBuilder.update;
import static org.mybatis.dynamic.sql.select.SelectDSL.select;

/**
 * @author 11797
 */
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderPayMapper orderPayMapper;

    private final UserService userService;


    @SneakyThrows
    @Override
    public Long createPayOrder(Byte payChannel, Integer payAmount, Long userId) {
        Date currentDate = new Date();
        Long outTradeNo = Long.parseLong(
            new SimpleDateFormat("yyyyMMddHHmmssSSS").format(currentDate) + new Random().nextInt(10));
        OrderPay orderPay = new OrderPay();
        orderPay.setOutTradeNo(outTradeNo);
        orderPay.setPayChannel(payChannel);
        orderPay.setTotalAmount(payAmount);
        orderPay.setUserId(userId);
        orderPay.setCreateTime(currentDate);
        orderPay.setUpdateTime(currentDate);
        orderPayMapper.insertSelective(orderPay);
        return outTradeNo;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updatePayOrder(Long outTradeNo, String tradeNo, int payStatus) {
        SelectStatementProvider selectStatement = select(OrderPayDynamicSqlSupport.id,
            OrderPayDynamicSqlSupport.payStatus, OrderPayDynamicSqlSupport.totalAmount,
            OrderPayDynamicSqlSupport.userId)
            .from(OrderPayDynamicSqlSupport.orderPay)
            .where(OrderPayDynamicSqlSupport.outTradeNo, isEqualTo(outTradeNo))
            .build()
            .render(RenderingStrategies.MYBATIS3);

        OrderPay orderPay = orderPayMapper.selectOne(selectStatement).orElse(null);

        if (orderPay.getPayStatus().intValue() == 2) {
            //待支付订单处理
            if (payStatus == 1) {
                //支付成功
                //1.更新订单状态为成功
                UpdateStatementProvider updateStatement = update(OrderPayDynamicSqlSupport.orderPay)
                    .set(OrderPayDynamicSqlSupport.tradeNo).equalTo(tradeNo)
                    .set(OrderPayDynamicSqlSupport.payStatus).equalTo((byte) 1)
                    .set(OrderPayDynamicSqlSupport.updateTime).equalTo(new Date())
                    .where(OrderPayDynamicSqlSupport.id, isEqualTo(orderPay.getId()))
                    .and(OrderPayDynamicSqlSupport.payStatus, isEqualTo((byte) 2))
                    .build()
                    .render(RenderingStrategies.MYBATIS3);
                int updateRow = orderPayMapper.update(updateStatement);
                if (updateRow > 0) {
                    //更新成功
                    //2.增加用户余额
                    userService.addAmount(orderPay.getUserId(), orderPay.getTotalAmount() * 100);
                }

            }
        }

    }
}

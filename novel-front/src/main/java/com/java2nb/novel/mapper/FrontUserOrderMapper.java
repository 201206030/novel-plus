package com.java2nb.novel.mapper;

import com.java2nb.novel.vo.RechargeRecordVO;
import com.java2nb.novel.vo.SubscribeDetailVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 前台用户订单与消费记录 Mapper
 *
 * @author 11797
 */
public interface FrontUserOrderMapper {

    /**
     * 查询当前用户的充值记录
     */
    List<RechargeRecordVO> listRechargeRecord(@Param("userId") Long userId);

    /**
     * 查询当前用户的章节订阅明细
     */
    List<SubscribeDetailVO> listSubscribeDetail(@Param("userId") Long userId);
}

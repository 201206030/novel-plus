package com.java2nb.novel.mapper;

import com.java2nb.novel.vo.AdminOrderPayVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单管理查询Mapper（列表联表查询，删除复用公共OrderPayMapper）
 *
 * @author Administrator
 */
@Mapper
public interface AdminOrderPayQueryMapper {

    /**
     * 分页查询订单列表（含下单用户昵称）
     *
     * @param keyword 商户订单号/交易平台订单号关键字（可空）
     * @param userId  用户ID（可空）
     * @return 订单列表
     */
    List<AdminOrderPayVO> listAll(@Param("keyword") String keyword, @Param("userId") Long userId);

    /**
     * 根据ID查询订单详情
     *
     * @param id 订单ID
     * @return 订单详情
     */
    AdminOrderPayVO getById(@Param("id") Long id);

}

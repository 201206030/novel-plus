package com.java2nb.novel.mapper;

import com.java2nb.novel.vo.AdminUserBuyRecordVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 购买记录管理查询Mapper（列表联表用户表；交易数据只读，无写操作）
 *
 * @author Administrator
 */
@Mapper
public interface AdminUserBuyRecordQueryMapper {

    /**
     * 分页查询购买记录（含购买用户显示名）
     *
     * @param keyword 用户名/昵称/书名/章节名关键字（可空）
     * @param userId  指定用户ID（可空，会员列表入口带参跳转用）
     * @return 购买记录列表
     */
    List<AdminUserBuyRecordVO> listAll(@Param("keyword") String keyword, @Param("userId") Long userId);

}

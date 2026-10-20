package com.java2nb.novel.mapper;

import com.java2nb.novel.vo.AdminUserFeedbackVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 会员反馈管理查询Mapper（列表联表查询，增删改复用公共UserFeedbackMapper）
 *
 * @author Administrator
 */
@Mapper
public interface AdminUserFeedbackQueryMapper {

    /**
     * 分页查询反馈列表（含反馈用户昵称）
     *
     * @param keyword 反馈内容关键字（可空）
     * @return 反馈列表
     */
    List<AdminUserFeedbackVO> listAll(@Param("keyword") String keyword);

}

package com.java2nb.novel.mapper;

import com.java2nb.novel.vo.AdminAuthorIncomeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 作家收入管理查询Mapper（列表联表查询，结算状态更新复用公共AuthorIncomeMapper）
 *
 * @author Administrator
 */
@Mapper
public interface AdminAuthorIncomeQueryMapper {

    /**
     * 分页查询作家月收入列表（含书名/笔名）
     *
     * @param keyword   书名/笔名关键字（可空）
     * @param payStatus 支付状态：0 待结算，1 已结算（可空）
     * @param month     收入月份，格式 yyyy-MM（可空）
     * @return 收入列表
     */
    List<AdminAuthorIncomeVO> listAll(@Param("keyword") String keyword, @Param("payStatus") Byte payStatus,
        @Param("month") String month);

}

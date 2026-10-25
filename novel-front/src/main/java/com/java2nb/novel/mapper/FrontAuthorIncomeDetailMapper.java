package com.java2nb.novel.mapper;

import com.java2nb.novel.vo.BookIncomeSummaryVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 作者收入明细 Mapper（按作品汇总，联表取书名）
 *
 * @author 11797
 */
public interface FrontAuthorIncomeDetailMapper {

    /**
     * 按作品汇总订阅收入（订阅总额/次数/人数），bookId 大于 0 时仅返回该书
     *
     * @param userId    用户ID
     * @param bookId    作品ID（0或null表示全部作品，分别成卡）
     * @param startTime 收入起始日期
     * @param endTime   收入结束日期
     * @return 每个作品的汇总
     */
    List<BookIncomeSummaryVO> bookIncomeSummary(@Param("userId") Long userId, @Param("bookId") Long bookId,
        @Param("startTime") Date startTime, @Param("endTime") Date endTime);
}

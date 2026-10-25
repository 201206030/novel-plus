package com.java2nb.novel.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 作者创作数据看板统计Mapper：更新字数/追读/收藏趋势与存稿统计
 *
 * @author 11797
 */
public interface FrontAuthorDashboardMapper {

    /**
     * 每日更新字数（仅统计已发布章节，按发布日 create_time 归集）
     *
     * @param authorId 作家ID
     * @param start    起始时间 yyyy-MM-dd HH:mm:ss
     * @return [{d: yyyy-MM-dd, value: 字数}]
     */
    List<Map<String, Object>> dailyUpdateWords(@Param("authorId") Long authorId, @Param("start") String start);

    /**
     * 每日追读人数（阅读记录按用户去重，以当日活跃阅读 update_time 计）
     *
     * @param authorId 作家ID
     * @param start    起始时间 yyyy-MM-dd HH:mm:ss
     * @return [{d: yyyy-MM-dd, value: 去重读者数}]
     */
    List<Map<String, Object>> dailyFollowRead(@Param("authorId") Long authorId, @Param("start") String start);

    /**
     * 每日新增收藏（书架加入数）
     *
     * @param authorId 作家ID
     * @param start    起始时间 yyyy-MM-dd HH:mm:ss
     * @return [{d: yyyy-MM-dd, value: 收藏数}]
     */
    List<Map<String, Object>> dailyCollect(@Param("authorId") Long authorId, @Param("start") String start);

    /**
     * 存稿统计：草稿与定时发布章节的数量与字数
     *
     * @param authorId 作家ID
     * @return {draftCount, draftWords, scheduleCount}
     */
    Map<String, Object> draftStat(@Param("authorId") Long authorId);

    /**
     * 有更新（已发布章节）的日期列表，倒序，用于计算连续更新天数
     *
     * @param authorId 作家ID
     * @param start    起始时间 yyyy-MM-dd HH:mm:ss
     * @return [yyyy-MM-dd, ...]
     */
    List<String> updateDates(@Param("authorId") Long authorId, @Param("start") String start);

    /**
     * 累计收藏总数
     *
     * @param authorId 作家ID
     * @return 收藏总数
     */
    long collectTotal(@Param("authorId") Long authorId);

}

package com.java2nb.novel.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * AI创作模块管理写入Mapper（人工干预：卡死任务强制失败、级联删除等）
 *
 * @author Administrator
 */
@Mapper
public interface AdminAiCreationCmdMapper {

    /**
     * 将卡死的执行中任务强制置为失败并释放防重占位（running_flag），使作者可重新提交
     *
     * @param id       任务ID
     * @param errorMsg 失败原因
     * @return 影响行数（0 表示任务已不在执行中状态）
     */
    int failStuckTask(@Param("id") Long id, @Param("errorMsg") String errorMsg);

    /**
     * 删除单个任务
     */
    int deleteTask(@Param("id") Long id);

    /**
     * 按方案删除全部任务
     */
    int deleteTasksByPlan(@Param("planId") Long planId);

    /**
     * 删除单个章节规划
     */
    int deleteChapterPlan(@Param("id") Long id);

    /**
     * 按方案删除全部章节规划
     */
    int deleteChapterPlansByPlan(@Param("planId") Long planId);

    /**
     * 删除创作方案
     */
    int deletePlan(@Param("id") Long id);

    /**
     * 删除作者创作偏好
     */
    int deletePref(@Param("id") Long id);

}

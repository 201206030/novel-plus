package com.java2nb.novel.mapper;

import com.java2nb.novel.entity.AiCreationTask;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * AI创作生成任务 Mapper
 *
 * @author 11797
 */
public interface FrontAiCreationTaskMapper {

    /**
     * 插入执行中的任务（running_flag=1，唯一索引防重复提交）
     */
    int insert(AiCreationTask task);

    /**
     * 插入已成功的正文草稿任务（作者手工编辑后落草稿，供校验修订使用）
     */
    int insertDraft(AiCreationTask task);

    /**
     * 按ID与作者查询（权限隔离）
     */
    AiCreationTask selectByIdAndAuthor(@Param("id") Long id, @Param("authorId") Long authorId);

    /**
     * 按ID查询（异步线程内部使用）
     */
    AiCreationTask selectById(@Param("id") Long id);

    /**
     * 任务结束（成功或失败），置running_flag为NULL释放占位
     */
    int finish(@Param("id") Long id, @Param("status") Byte status, @Param("result") String result,
        @Param("errorMsg") String errorMsg);

    /**
     * 服务启动时将所有执行中任务批量置为失败并释放防重占位（重启后线程已丢失，任务不可能再完成）
     */
    int failAllRunning(@Param("errorMsg") String errorMsg);

    /**
     * 查询方案下最近的执行中任务（页面恢复轮询用）
     */
    List<AiCreationTask> listRunning(@Param("planId") Long planId);

    /**
     * 查询方案下最近N条任务（进度展示用，按ID倒序）
     */
    List<AiCreationTask> listRecent(@Param("planId") Long planId, @Param("limit") int limit);

    /**
     * 查询指定目标最近一条已成功任务
     */
    AiCreationTask selectLatestSuccess(@Param("planId") Long planId, @Param("taskType") Byte taskType,
        @Param("chapterNo") Integer chapterNo);
}

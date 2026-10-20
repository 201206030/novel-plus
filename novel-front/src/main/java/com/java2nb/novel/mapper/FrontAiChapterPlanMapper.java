package com.java2nb.novel.mapper;

import com.java2nb.novel.entity.AiChapterPlan;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * AI章节计划 Mapper
 *
 * @author 11797
 */
public interface FrontAiChapterPlanMapper {

    /**
     * 批量插入章节计划
     */
    int batchInsert(@Param("list") List<AiChapterPlan> list);

    /**
     * 查询方案下全部章节计划
     */
    List<AiChapterPlan> listByPlanId(@Param("planId") Long planId);

    /**
     * 查询指定章节计划
     */
    AiChapterPlan selectByPlanAndNo(@Param("planId") Long planId, @Param("chapterNo") Integer chapterNo);

    /**
     * CAS将状态从 0/1 置为 3（保存中），防并发重复保存
     */
    int casMarkSaving(@Param("planId") Long planId, @Param("chapterNo") Integer chapterNo);

    /**
     * 保存成功后置为已保存状态
     */
    int markSaved(@Param("planId") Long planId, @Param("chapterNo") Integer chapterNo,
        @Param("indexId") Long indexId, @Param("wordCount") Integer wordCount);

    /**
     * 草稿生成成功后置为草稿状态（仅未保存章节）
     */
    int markDraft(@Param("planId") Long planId, @Param("chapterNo") Integer chapterNo);

    /**
     * 更新章节规划（仅未保存章节）
     */
    int updateOutline(@Param("planId") Long planId, @Param("chapterNo") Integer chapterNo,
        @Param("indexName") String indexName, @Param("chapterOutline") String chapterOutline);

    /**
     * 更新章节摘要
     */
    int updateSummary(@Param("planId") Long planId, @Param("chapterNo") Integer chapterNo,
        @Param("summary") String summary);

    /**
     * 删除方案下未保存的章节计划（重新拆分章节规划前调用）
     */
    int deleteUnsaved(@Param("planId") Long planId);

    /**
     * 查询小说最新章节ID（发布章节后回写index_id用）
     */
    Long selectLatestIndexIdByBook(@Param("bookId") Long bookId);
}

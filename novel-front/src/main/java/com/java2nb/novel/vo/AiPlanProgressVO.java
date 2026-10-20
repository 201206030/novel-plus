package com.java2nb.novel.vo;

import com.java2nb.novel.entity.AiChapterPlan;
import com.java2nb.novel.entity.AiCreationPlan;
import com.java2nb.novel.entity.AiCreationTask;
import lombok.Data;

import java.util.List;

/**
 * AI创作助手进度视图（用于页面加载时恢复创作状态）
 *
 * @author 11797
 */
@Data
public class AiPlanProgressVO {

    /**
     * 创作方案
     */
    private AiCreationPlan plan;

    /**
     * 章节计划列表
     */
    private List<AiChapterPlan> chapterPlans;

    /**
     * 执行中的任务（前端恢复轮询）
     */
    private List<AiCreationTask> runningTasks;

    /**
     * 最近任务摘要（不含result正文，展示状态与失败原因）
     */
    private List<AiCreationTask> latestTasks;
}

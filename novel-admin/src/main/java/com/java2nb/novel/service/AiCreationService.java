package com.java2nb.novel.service;

import com.github.pagehelper.PageHelper;
import com.java2nb.novel.entity.AiChapterPlan;
import com.java2nb.novel.entity.AiCreationTask;
import com.java2nb.novel.mapper.AdminAiCreationCmdMapper;
import com.java2nb.novel.mapper.AdminAiCreationQueryMapper;
import com.java2nb.novel.vo.AdminAiPlanVO;
import com.java2nb.novel.vo.AdminAiPrefVO;
import com.java2nb.novel.vo.AdminAiTaskVO;
import io.github.xxyopen.model.page.PageBean;
import io.github.xxyopen.model.page.builder.pagehelper.PageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.java2nb.novel.core.utils.AdminUtil.biz;

/**
 * AI创作模块管理Service：创作方案/章节规划/生成任务/作者偏好的查看与人工干预。
 * 任务失败或卡死后，管理员可强制释放执行锁或删除，避免阻塞作者重新提交。
 *
 * @author Administrator
 */
@Service
@RequiredArgsConstructor
public class AiCreationService {

    private final AdminAiCreationQueryMapper aiQueryMapper;

    private final AdminAiCreationCmdMapper aiCmdMapper;

    /**
     * 管理端强制失败任务的错误标识
     */
    private static final String FORCE_FAIL_MSG = "后台管理员手动终止";

    /**
     * 任务状态：执行中
     */
    private static final byte TASK_RUNNING = 0;

    /* ---------------- 创作方案 ---------------- */

    public PageBean<AdminAiPlanVO> planList(String keyword, Byte stage, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        List<AdminAiPlanVO> list = aiQueryMapper.listPlans(keyword, stage);
        return PageBuilder.build(list);
    }

    /**
     * 删除创作方案，级联删除其章节规划与生成任务，避免遗留孤儿数据。
     * 注意：仅删除AI创作过程数据，不会删除已生成的正式章节（book_index）。
     */
    @Transactional(rollbackFor = Exception.class)
    public void planRemove(Long id) {
        aiCmdMapper.deleteTasksByPlan(id);
        aiCmdMapper.deleteChapterPlansByPlan(id);
        aiCmdMapper.deletePlan(id);
    }

    /* ---------------- 章节规划 ---------------- */

    public PageBean<AiChapterPlan> chapterPlanList(Long planId, Byte status, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        List<AiChapterPlan> list = aiQueryMapper.listChapterPlans(planId, status);
        return PageBuilder.build(list);
    }

    /**
     * 删除单条章节规划（仅删除AI规划记录，不动已生成的正式章节）
     */
    public void chapterPlanRemove(Long id) {
        aiCmdMapper.deleteChapterPlan(id);
    }

    /* ---------------- 生成任务 ---------------- */

    public PageBean<AdminAiTaskVO> taskList(Long planId, Byte status, Byte taskType, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        List<AdminAiTaskVO> list = aiQueryMapper.listTasks(planId, status, taskType);
        return PageBuilder.build(list);
    }

    /**
     * 强制终止卡死的执行中任务：置为失败并释放防重占位（running_flag），使作者可重新提交同类任务。
     *
     * @param id 任务ID
     */
    public void taskFailStuck(Long id) {
        int affected = aiCmdMapper.failStuckTask(id, FORCE_FAIL_MSG);
        if (affected == 0) {
            throw biz("任务已不在执行中状态，无需终止");
        }
    }

    public void taskRemove(Long id) {
        // 执行中任务禁止直接删除（应先“终止”释放防重锁），避免前端看不到状态变化
        AiCreationTask task = aiQueryMapper.getTask(id);
        if (task == null) {
            throw biz("任务不存在");
        }
        if (task.getStatus() != null && task.getStatus() == TASK_RUNNING) {
            throw biz("任务执行中，请先终止后再删除");
        }
        aiCmdMapper.deleteTask(id);
    }

    /* ---------------- 作者偏好 ---------------- */

    public PageBean<AdminAiPrefVO> prefList(String keyword, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        List<AdminAiPrefVO> list = aiQueryMapper.listPrefs(keyword);
        return PageBuilder.build(list);
    }

    public void prefRemove(Long id) {
        aiCmdMapper.deletePref(id);
    }

}

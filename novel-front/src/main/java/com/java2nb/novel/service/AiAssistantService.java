package com.java2nb.novel.service;

import com.java2nb.novel.entity.AiCreationPlan;
import com.java2nb.novel.entity.AiCreationTask;
import com.java2nb.novel.entity.Book;
import com.java2nb.novel.vo.AiPlanProgressVO;

import java.util.List;

/**
 * AI 长篇小说创作助手服务
 *
 * @author 11797
 */
public interface AiAssistantService {

    /**
     * 初始化或恢复创作方案：指定bookId绑定现有小说；指定planId直接加载；否则复用/新建未绑定方案
     */
    AiCreationPlan initPlan(Long authorId, Long planId, Long bookId);

    /**
     * 保存创作问答参数（自动补齐章节数/字数默认值）
     */
    void saveParams(Long authorId, Long planId, String creativeParams);

    /**
     * 提交异步生成任务，返回任务ID；同一目标执行中时拒绝重复提交
     */
    Long startTask(Long authorId, Long planId, Byte taskType, Integer chapterNo);

    /**
     * 查询任务状态与结果（含超时中断兜底处理）
     */
    AiCreationTask getTask(Long authorId, Long taskId);

    /**
     * 查询创作进度（方案+章节计划+任务状态），用于页面恢复
     */
    AiPlanProgressVO getProgress(Long authorId, Long planId);

    /**
     * 保存（编辑后的）创作方案草稿
     */
    void savePlanContent(Long authorId, Long planId, String planContent);

    /**
     * 确认创作方案：新建小说时创建小说记录，并拆分章节规划（不影响已保存章节）
     */
    void confirmPlan(Long authorId, String penName, Long planId, String planContent, Book bookInfo);

    /**
     * 修改单个章节规划（仅未保存章节）
     */
    void updateChapterPlan(Long authorId, Long planId, Integer chapterNo, String indexName, String chapterOutline);

    /**
     * 保存作者编辑后的章节草稿（未正式保存，供校验修订使用）
     */
    void saveDraft(Long authorId, Long planId, Integer chapterNo, String content);

    /**
     * 作者确认后保存章节正文到现有章节系统，并触发创作记忆摘要更新
     */
    void saveChapter(Long authorId, Long planId, Integer chapterNo, String indexName, String content, Byte isVip);

    /**
     * 程序化文本校验（字数与重复检测），返回问题列表
     */
    List<String> programCheck(String content, Integer targetWords);

    /**
     * 查询作者全局创作偏好
     */
    String getPref(Long authorId);

    /**
     * 保存或清除作者全局创作偏好
     */
    void savePref(Long authorId, String prefContent);
}

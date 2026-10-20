package com.java2nb.novel.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.java2nb.novel.core.ai.AiJsonUtil;
import com.java2nb.novel.core.ai.ChapterTextCheckUtil;
import com.java2nb.novel.core.ai.PromptTemplateUtil;
import com.java2nb.novel.core.enums.ResponseStatus;
import com.java2nb.novel.entity.*;
import com.java2nb.novel.mapper.FrontAiAuthorPrefMapper;
import com.java2nb.novel.mapper.FrontAiChapterPlanMapper;
import com.java2nb.novel.mapper.FrontAiCreationPlanMapper;
import com.java2nb.novel.mapper.FrontAiCreationTaskMapper;
import com.java2nb.novel.service.AiAssistantService;
import com.java2nb.novel.service.BookService;
import com.java2nb.novel.vo.AiPlanProgressVO;
import io.github.xxyopen.web.exception.BusinessException;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * AI 长篇小说创作助手服务实现。
 * <p>
 * 生成类工作全部落库为任务（ai_creation_task），在专用线程池内异步串行调用大模型，
 * 前端通过轮询任务状态实现断点恢复；正文草稿保存在任务result字段（未保存），
 * 作者确认后经现有 addBookContent 正式写入章节系统（已保存），二者严格区分。
 *
 * @author 11797
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AiAssistantServiceImpl implements AiAssistantService {

    /** 任务类型：书名建议 */
    public static final byte TYPE_BOOK_NAME = 1;
    /** 任务类型：创作方案 */
    public static final byte TYPE_PLAN = 2;
    /** 任务类型：章节正文 */
    public static final byte TYPE_CHAPTER = 3;
    /** 任务类型：章节校验修订 */
    public static final byte TYPE_CHECK_REVISE = 4;
    /** 任务类型：章节摘要（创作记忆） */
    public static final byte TYPE_SUMMARY = 5;
    /** 任务类型：章节规划增量批次 */
    public static final byte TYPE_CHAPTER_PLAN_BATCH = 6;

    /** 阶段：创作问答中/方案草稿/方案已确认 */
    public static final byte STAGE_QA = 1;
    public static final byte STAGE_PLAN_DRAFT = 2;
    public static final byte STAGE_PLAN_CONFIRMED = 3;

    /** 章节计划状态：待创作/草稿/已保存/保存中 */
    public static final byte CHAPTER_TODO = 0;
    public static final byte CHAPTER_DRAFT = 1;
    public static final byte CHAPTER_SAVED = 2;

    /** 任务状态 */
    public static final byte TASK_RUNNING = 0;
    public static final byte TASK_SUCCESS = 1;
    public static final byte TASK_FAILED = 2;

    /** 单次方案生成最多输出的章节规划数 */
    private static final int MAX_FIRST_PLAN_CHAPTERS = 40;
    /** 章节规划增量批次大小 */
    private static final int CHAPTER_PLAN_BATCH_SIZE = 30;
    /** 校验修订最大轮数 */
    private static final int MAX_REVISE_ROUNDS = 3;
    /** 异步任务硬超时（含排队等待容忍） */
    private static final Duration AI_TIMEOUT = Duration.ofMinutes(10);
    /** 轮询侧判定任务中断的静默时间 */
    private static final long TASK_STALE_MILLIS = 12 * 60 * 1000L;

    private final FrontAiCreationPlanMapper planMapper;

    private final FrontAiChapterPlanMapper chapterPlanMapper;

    private final FrontAiCreationTaskMapper taskMapper;

    private final FrontAiAuthorPrefMapper prefMapper;

    private final BookService bookService;

    private final ChatClient chatClient;

    private static final com.fasterxml.jackson.databind.ObjectMapper MAPPER =
            new com.fasterxml.jackson.databind.ObjectMapper();

    @Resource
    private ThreadPoolExecutor aiTaskExecutor;

    @Override
    public AiCreationPlan initPlan(Long authorId, Long planId, Long bookId) {
        if (planId != null) {
            return loadOwnedPlan(authorId, planId);
        }
        if (bookId != null) {
            AiCreationPlan plan = planMapper.selectByBookId(bookId);
            if (plan != null) {
                if (!plan.getAuthorId().equals(authorId)) {
                    throw new BusinessException(ResponseStatus.AI_PLAN_NOT_EXISTS);
                }
                return plan;
            }
            //校验小说归属后绑定现有小说创建方案
            Book book = bookService.queryBookDetail(bookId);
            if (book == null || !authorId.equals(book.getAuthorId())) {
                throw new BusinessException(ResponseStatus.AI_PLAN_NOT_EXISTS);
            }
            return newPlan(authorId, bookId);
        }
        //新建创作：复用最近一个未绑定小说的方案，支持断点续写
        AiCreationPlan latest = planMapper.selectLatestUnboundByAuthor(authorId);
        return latest != null ? latest : newPlan(authorId, null);
    }

    private AiCreationPlan newPlan(Long authorId, Long bookId) {
        AiCreationPlan plan = new AiCreationPlan();
        plan.setAuthorId(authorId);
        plan.setBookId(bookId);
        plan.setStage(STAGE_QA);
        plan.setOutlineVersion(0);
        planMapper.insert(plan);
        return plan;
    }

    @Override
    public void saveParams(Long authorId, Long planId, String creativeParams) {
        AiCreationPlan plan = loadOwnedPlan(authorId, planId);
        JsonNode node = AiJsonUtil.parseJsonFromModel(creativeParams);
        //必填项校验与默认值补齐
        for (String key : new String[]{"genre", "coreIdea", "protagonist", "conflict"}) {
            if (node.path(key).asText("").isBlank()) {
                throw new BusinessException(ResponseStatus.AI_PARAMS_INVALID);
            }
        }
        ObjectNode obj = (ObjectNode) node;
        int totalChapters = obj.path("totalChapters").asInt(60);
        obj.put("totalChapters", Math.min(Math.max(totalChapters, 1), 1000));
        int chapterWords = obj.path("chapterWords").asInt(3000);
        obj.put("chapterWords", Math.min(Math.max(chapterWords, 200), 20000));
        String normalized = obj.toString();
        if (normalized.length() > 8000) {
            throw new BusinessException(ResponseStatus.AI_PARAMS_INVALID);
        }
        AiCreationPlan update = new AiCreationPlan();
        update.setId(plan.getId());
        update.setAuthorId(authorId);
        update.setCreativeParams(normalized);
        planMapper.update(update);
    }

    @Override
    public Long startTask(Long authorId, Long planId, Byte taskType, Integer chapterNo) {
        if (taskType == null || taskType < TYPE_BOOK_NAME || taskType > TYPE_CHAPTER_PLAN_BATCH
                || taskType == TYPE_SUMMARY) {
            //摘要任务仅由系统在章节保存后内部触发
            throw new BusinessException(ResponseStatus.AI_PARAMS_INVALID);
        }
        AiCreationPlan plan = loadOwnedPlan(authorId, planId);
        int no = chapterNo == null ? 0 : chapterNo;
        precheckTask(plan, taskType, no);

        AiCreationTask task = new AiCreationTask();
        task.setPlanId(planId);
        task.setAuthorId(authorId);
        task.setTaskType(taskType);
        task.setChapterNo(no);
        try {
            //running_flag唯一索引兜底防并发重复提交
            taskMapper.insert(task);
        } catch (DuplicateKeyException e) {
            throw new BusinessException(ResponseStatus.AI_TASK_RUNNING);
        }
        Long taskId = task.getId();
        try {
            aiTaskExecutor.execute(() -> runTask(taskId));
        } catch (RejectedExecutionException e) {
            taskMapper.finish(taskId, TASK_FAILED, null, "服务器繁忙，请稍后重试");
            throw new BusinessException(ResponseStatus.AI_GEN_FAILED);
        }
        return taskId;
    }

    /**
     * 按任务类型做前置条件校验
     */
    private void precheckTask(AiCreationPlan plan, byte taskType, int chapterNo) {
        switch (taskType) {
            case TYPE_BOOK_NAME, TYPE_PLAN -> {
                if (plan.getCreativeParams() == null || plan.getCreativeParams().isBlank()) {
                    throw new BusinessException(ResponseStatus.AI_PARAMS_INVALID);
                }
            }
            case TYPE_CHAPTER_PLAN_BATCH -> {
                if (plan.getPlanContent() == null || plan.getPlanContent().isBlank()
                        || plan.getStage() < STAGE_PLAN_DRAFT || chapterNo <= 0) {
                    throw new BusinessException(ResponseStatus.AI_PLAN_INVALID);
                }
            }
            case TYPE_CHAPTER -> {
                requireConfirmedChapter(plan, chapterNo);
            }
            case TYPE_CHECK_REVISE -> {
                requireConfirmedChapter(plan, chapterNo);
                //需要已有正文草稿（校验修订任务或正文任务最近一次成功结果）
                AiCreationTask draft = latestDraftTask(plan.getId(), chapterNo);
                if (draft == null) {
                    throw new BusinessException(ResponseStatus.AI_DRAFT_NOT_EXISTS);
                }
            }
            default -> throw new BusinessException(ResponseStatus.AI_PARAMS_INVALID);
        }
    }

    private void requireConfirmedChapter(AiCreationPlan plan, int chapterNo) {
        if (plan.getStage() < STAGE_PLAN_CONFIRMED || chapterNo <= 0) {
            throw new BusinessException(ResponseStatus.AI_PLAN_NOT_CONFIRMED);
        }
        if (plan.getPlanContent() == null || plan.getPlanContent().isBlank()) {
            throw new BusinessException(ResponseStatus.AI_PLAN_INVALID);
        }
        AiChapterPlan row = chapterPlanMapper.selectByPlanAndNo(plan.getId(), chapterNo);
        if (row == null) {
            throw new BusinessException(ResponseStatus.AI_CHAPTER_PLAN_NOT_EXISTS);
        }
    }

    /**
     * 取该章节最近一次可用草稿：优先校验修订结果，其次正文结果
     */
    private AiCreationTask latestDraftTask(Long planId, int chapterNo) {
        AiCreationTask reviseTask = taskMapper.selectLatestSuccess(planId, TYPE_CHECK_REVISE, chapterNo);
        if (reviseTask != null) {
            AiCreationTask chapterTask = taskMapper.selectLatestSuccess(planId, TYPE_CHAPTER, chapterNo);
            if (chapterTask == null || reviseTask.getId() > chapterTask.getId()) {
                return reviseTask;
            }
        }
        return taskMapper.selectLatestSuccess(planId, TYPE_CHAPTER, chapterNo);
    }

    /**
     * 异步线程内执行任务（独立于请求线程，失败仅落库不影响已保存数据）
     */
    public void runTask(Long taskId) {
        AiCreationTask task = taskMapper.selectById(taskId);
        if (task == null || task.getStatus() != TASK_RUNNING) {
            return;
        }
        try {
            AiCreationPlan plan = planMapper.selectByIdAndAuthor(task.getPlanId(), task.getAuthorId());
            if (plan == null) {
                throw new BusinessException(ResponseStatus.AI_PLAN_NOT_EXISTS);
            }
            String result = switch (task.getTaskType()) {
                case TYPE_BOOK_NAME -> genBookName(plan);
                case TYPE_PLAN -> genPlan(plan);
                case TYPE_CHAPTER_PLAN_BATCH -> genChapterPlanBatch(plan, task);
                case TYPE_CHAPTER -> genChapterContent(plan, task);
                case TYPE_CHECK_REVISE -> genCheckRevise(plan, task);
                case TYPE_SUMMARY -> genChapterSummary(plan, task);
                default -> throw new BusinessException(ResponseStatus.AI_PARAMS_INVALID);
            };
            taskMapper.finish(taskId, TASK_SUCCESS, result, null);
        } catch (Exception e) {
            //不输出正文等用户内容到日志，只记录任务标识与原因
            log.warn("AI创作任务执行失败 taskId={}, type={}, planId={}, cause={}", taskId, task.getTaskType(),
                    task.getPlanId(), e.getClass().getSimpleName());
            //BusinessException的文本在resultCode里，getMessage()为空
            String msg = e instanceof BusinessException && ((BusinessException) e).getResultCode() != null
                    ? ((BusinessException) e).getResultCode().getMsg() : "AI生成失败，请重试";
            taskMapper.finish(taskId, TASK_FAILED, null, AiJsonUtil.truncate(msg, 400));
        }
    }

    /**
     * 服务启动后恢复任务状态：重启后线程池内的任务已全部丢失，
     * 遗留的执行中任务批量置为失败并释放防重占位（running_flag），避免阻塞重新提交。
     * 仅限单实例部署；多实例场景需改为按启动时间窗口判定。
     */
    @EventListener(ApplicationReadyEvent.class)
    public void failStaleRunningTasksOnStartup() {
        try {
            int failed = taskMapper.failAllRunning("服务已重启，任务被中断，请重试");
            if (failed > 0) {
                log.info("AI创作任务启动恢复：已将{}个遗留执行中任务置为失败", failed);
            }
        } catch (Exception e) {
            //恢复失败不阻断启动，轮询侧超时判定仍可兜底
            log.warn("AI创作任务启动恢复执行失败", e);
        }
    }

    @Override
    public AiCreationTask getTask(Long authorId, Long taskId) {
        AiCreationTask task = taskMapper.selectByIdAndAuthor(taskId, authorId);
        if (task == null) {
            throw new BusinessException(ResponseStatus.AI_PLAN_NOT_EXISTS);
        }
        //进程重启等导致任务滞留执行中状态时，超时判定为中断，允许重试
        if (task.getStatus() == TASK_RUNNING && task.getUpdateTime() != null
                && System.currentTimeMillis() - task.getUpdateTime().getTime() > TASK_STALE_MILLIS) {
            taskMapper.finish(taskId, TASK_FAILED, null, "任务超时或已中断，请重试");
            task = taskMapper.selectByIdAndAuthor(taskId, authorId);
        }
        return task;
    }

    @Override
    public AiPlanProgressVO getProgress(Long authorId, Long planId) {
        AiCreationPlan plan = loadOwnedPlan(authorId, planId);
        AiPlanProgressVO vo = new AiPlanProgressVO();
        vo.setPlan(plan);
        vo.setChapterPlans(chapterPlanMapper.listByPlanId(planId));
        vo.setRunningTasks(taskMapper.listRunning(planId));
        List<AiCreationTask> recent = taskMapper.listRecent(planId, 15);
        //进度接口只回传任务状态摘要，正文草稿通过任务详情接口单独获取
        recent.forEach(t -> t.setResult(null));
        vo.setLatestTasks(recent);
        return vo;
    }

    @Override
    public void savePlanContent(Long authorId, Long planId, String planContent) {
        AiCreationPlan plan = loadOwnedPlan(authorId, planId);
        String validated = validatePlanContent(planContent);
        if (validated.length() > 150000) {
            throw new BusinessException(ResponseStatus.AI_PLAN_INVALID);
        }
        AiCreationPlan update = new AiCreationPlan();
        update.setId(plan.getId());
        update.setAuthorId(authorId);
        update.setPlanContent(validated);
        if (plan.getStage() < STAGE_PLAN_DRAFT) {
            update.setStage(STAGE_PLAN_DRAFT);
        }
        planMapper.update(update);
    }

    private String validatePlanContent(String planContent) {
        JsonNode node = AiJsonUtil.parseJsonFromModel(planContent);
        AiJsonUtil.validatePlan(node);
        normalizeChapterNos(node.path("chapters"), 1);
        return node.toString();
    }

    /**
     * 章节序号防御性重排：模型编号异常时按数组顺序强制连续编号
     */
    private void normalizeChapterNos(JsonNode chapters, int startNo) {
        if (!chapters.isArray()) {
            return;
        }
        for (int i = 0; i < chapters.size(); i++) {
            JsonNode chapter = chapters.get(i);
            if (chapter instanceof ObjectNode obj && chapter.path("chapterNo").asInt(0) != startNo + i) {
                obj.put("chapterNo", startNo + i);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmPlan(Long authorId, String penName, Long planId, String planContent, Book bookInfo) {
        AiCreationPlan plan = loadOwnedPlan(authorId, planId);
        String content = (planContent != null && !planContent.isBlank()) ? planContent : plan.getPlanContent();
        if (content == null || content.isBlank()) {
            throw new BusinessException(ResponseStatus.AI_PLAN_INVALID);
        }
        //校验并规范化（含章节序号重排）
        JsonNode planNode = AiJsonUtil.parseJsonFromModel(validatePlanContent(content));

        Long boundBookId = plan.getBookId();
        if (boundBookId == null) {
            //为新小说创建记录，复用现有发布小说流程（含同名校验、AI封面生成）
            if (bookInfo == null || bookInfo.getBookName() == null || bookInfo.getBookName().isBlank()
                    || bookInfo.getCatId() == null || bookInfo.getWorkDirection() == null) {
                throw new BusinessException(ResponseStatus.AI_PARAMS_INVALID);
            }
            bookInfo.setBookDesc(convertText(planNode.path("intro").asText("")));
            bookService.addBook(bookInfo, authorId, penName);
            boundBookId = bookInfo.getId();
        }

        //拆分章节规划：仅重建未保存章节，已保存章节保持不动
        List<AiChapterPlan> rows = buildChapterRows(plan.getId(), planNode.path("chapters"));
        chapterPlanMapper.deleteUnsaved(plan.getId());
        if (!rows.isEmpty()) {
            chapterPlanMapper.batchInsert(rows);
        }

        AiCreationPlan update = new AiCreationPlan();
        update.setId(plan.getId());
        update.setAuthorId(authorId);
        update.setBookId(boundBookId);
        update.setPlanContent(planNode.toString());
        update.setStage(STAGE_PLAN_CONFIRMED);
        update.setOutlineVersion((plan.getOutlineVersion() == null ? 0 : plan.getOutlineVersion()) + 1);
        planMapper.update(update);
    }

    private List<AiChapterPlan> buildChapterRows(Long planId, JsonNode chapters) {
        List<AiChapterPlan> rows = new ArrayList<>();
        Set<Integer> seen = new HashSet<>();
        for (JsonNode chapter : chapters) {
            int chapterNo = chapter.path("chapterNo").asInt(0);
            if (chapterNo <= 0 || !seen.add(chapterNo)) {
                continue;
            }
            AiChapterPlan row = new AiChapterPlan();
            row.setPlanId(planId);
            row.setChapterNo(chapterNo);
            row.setIndexName(AiJsonUtil.truncate(chapter.path("indexName").asText(""), 100));
            ObjectNode outline = chapter.deepCopy();
            outline.remove("chapterNo");
            outline.remove("indexName");
            row.setChapterOutline(AiJsonUtil.truncate(outline.toString(), 4000));
            rows.add(row);
        }
        rows.sort(Comparator.comparingInt(AiChapterPlan::getChapterNo));
        return rows;
    }

    @Override
    public void updateChapterPlan(Long authorId, Long planId, Integer chapterNo, String indexName,
                                  String chapterOutline) {
        loadOwnedPlan(authorId, planId);
        if (chapterNo == null || chapterNo <= 0 || indexName == null || indexName.isBlank()
                || indexName.length() > 100 || chapterOutline != null && chapterOutline.length() > 4000) {
            throw new BusinessException(ResponseStatus.AI_PARAMS_INVALID);
        }
        AiChapterPlan row = chapterPlanMapper.selectByPlanAndNo(planId, chapterNo);
        if (row == null) {
            throw new BusinessException(ResponseStatus.AI_CHAPTER_PLAN_NOT_EXISTS);
        }
        if (row.getStatus() == CHAPTER_SAVED) {
            throw new BusinessException(ResponseStatus.AI_CHAPTER_SAVED);
        }
        chapterPlanMapper.updateOutline(planId, chapterNo, indexName.trim(), chapterOutline);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveChapter(Long authorId, Long planId, Integer chapterNo, String indexName, String content,
                             Byte isVip) {
        AiCreationPlan plan = loadOwnedPlan(authorId, planId);
        if (plan.getStage() < STAGE_PLAN_CONFIRMED || plan.getBookId() == null) {
            throw new BusinessException(ResponseStatus.AI_PLAN_NOT_CONFIRMED);
        }
        if (chapterNo == null || chapterNo <= 0 || indexName == null || indexName.isBlank()
                || indexName.length() > 100 || content == null || content.isBlank() || content.length() > 200000) {
            throw new BusinessException(ResponseStatus.AI_PARAMS_INVALID);
        }
        //CAS 0/1 -> 3，同一章节并发/重复提交只有一次能进入
        if (chapterPlanMapper.casMarkSaving(planId, chapterNo) == 0) {
            throw new BusinessException(ResponseStatus.AI_CHAPTER_SAVED);
        }
        //正文格式转换与字数规则与现有发布章节流程保持一致
        String transformed = convertText(content);
        int wordCount = ChapterTextCheckUtil.validWordCount(transformed);
        bookService.addBookContent(plan.getBookId(), indexName.trim(), transformed,
                isVip == null ? (byte) 0 : isVip, authorId);
        Long indexId = chapterPlanMapper.selectLatestIndexIdByBook(plan.getBookId());
        if (indexId == null) {
            //未产生章节（如归属校验未通过），回滚本次保存
            throw new BusinessException(ResponseStatus.AI_GEN_FAILED);
        }
        chapterPlanMapper.markSaved(planId, chapterNo, indexId, wordCount);

        //事务提交后再触发摘要任务，避免异步线程读不到未提交章节；摘要失败不影响已保存正文
        registerSummaryTrigger(authorId, planId, chapterNo);
    }

    private void registerSummaryTrigger(Long authorId, Long planId, Integer chapterNo) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    triggerSummaryTask(authorId, planId, chapterNo);
                }
            });
        } else {
            triggerSummaryTask(authorId, planId, chapterNo);
        }
    }

    private void triggerSummaryTask(Long authorId, Long planId, Integer chapterNo) {
        try {
            AiCreationTask task = new AiCreationTask();
            task.setPlanId(planId);
            task.setAuthorId(authorId);
            task.setTaskType(TYPE_SUMMARY);
            task.setChapterNo(chapterNo);
            taskMapper.insert(task);
            aiTaskExecutor.execute(() -> runTask(task.getId()));
        } catch (DuplicateKeyException e) {
            log.debug("摘要任务已在执行中 planId={}, chapterNo={}", planId, chapterNo);
        } catch (Exception e) {
            log.warn("触发章节摘要任务失败 planId={}, chapterNo={}", planId, chapterNo);
        }
    }

    /**
     * 作者手工编辑后落为服务端草稿：以成功状态的正文任务记录，后续校验修订以其为准
     */
    @Override
    public void saveDraft(Long authorId, Long planId, Integer chapterNo, String content) {
        AiCreationPlan plan = loadOwnedPlan(authorId, planId);
        if (chapterNo == null || chapterNo <= 0 || content == null || content.isBlank() || content.length() > 200000) {
            throw new BusinessException(ResponseStatus.AI_PARAMS_INVALID);
        }
        AiChapterPlan row = chapterPlanMapper.selectByPlanAndNo(planId, chapterNo);
        if (row == null) {
            throw new BusinessException(ResponseStatus.AI_CHAPTER_PLAN_NOT_EXISTS);
        }
        if (row.getStatus() == CHAPTER_SAVED) {
            throw new BusinessException(ResponseStatus.AI_CHAPTER_SAVED);
        }
        AiCreationTask draft = new AiCreationTask();
        draft.setPlanId(planId);
        draft.setAuthorId(authorId);
        draft.setChapterNo(chapterNo);
        draft.setResult(content);
        taskMapper.insertDraft(draft);
        chapterPlanMapper.markDraft(planId, chapterNo);
    }

    @Override
    public List<String> programCheck(String content, Integer targetWords) {
        return ChapterTextCheckUtil.check(content, targetWords == null ? 0 : targetWords);
    }

    @Override
    public String getPref(Long authorId) {
        AiAuthorPref pref = prefMapper.selectByAuthorId(authorId);
        return pref == null ? null : pref.getPrefContent();
    }

    @Override
    public void savePref(Long authorId, String prefContent) {
        if (prefContent != null && !prefContent.isBlank()) {
            if (prefContent.length() > 4000) {
                throw new BusinessException(ResponseStatus.AI_PARAMS_INVALID);
            }
            //允许作者清除偏好时传空
            AiJsonUtil.parseJsonFromModel(prefContent);
        }
        AiAuthorPref pref = new AiAuthorPref();
        pref.setAuthorId(authorId);
        pref.setPrefContent(prefContent == null || prefContent.isBlank() ? "{}" : prefContent);
        prefMapper.upsert(pref);
    }

    // ------------------------------------------------------------------
    // 各类生成任务的实现（在 aiTaskExecutor 线程内执行）
    // ------------------------------------------------------------------

    private String genBookName(AiCreationPlan plan) {
        JsonNode params = readParams(plan);
        Map<String, String> vars = new HashMap<>();
        vars.put("genre", text(params, "genre"));
        vars.put("coreIdea", text(params, "coreIdea"));
        vars.put("protagonist", text(params, "protagonist"));
        vars.put("conflict", text(params, "conflict"));
        vars.put("targetReaders", text(params, "targetReaders"));
        String raw = callAi(PromptTemplateUtil.render("book_name", vars));
        JsonNode node = AiJsonUtil.parseJsonFromModel(raw);
        AiJsonUtil.validateNames(node);
        return node.toString();
    }

    private String genPlan(AiCreationPlan plan) {
        JsonNode params = readParams(plan);
        int totalChapters = params.path("totalChapters").asInt(60);
        Map<String, String> vars = new HashMap<>();
        vars.put("genre", text(params, "genre"));
        vars.put("bookName", text(params, "bookName"));
        vars.put("coreIdea", text(params, "coreIdea"));
        vars.put("protagonist", text(params, "protagonist"));
        vars.put("conflict", text(params, "conflict"));
        vars.put("worldview", text(params, "worldview"));
        vars.put("style", text(params, "style"));
        vars.put("theme", text(params, "theme"));
        vars.put("targetReaders", text(params, "targetReaders"));
        vars.put("totalChapters", String.valueOf(totalChapters));
        vars.put("chapterWords", String.valueOf(params.path("chapterWords").asInt(3000)));
        vars.put("extra", text(params, "extra"));
        vars.put("pref", AiJsonUtil.truncate(getPref(plan.getAuthorId()), 500));
        vars.put("planChapterEnd", String.valueOf(Math.min(totalChapters, MAX_FIRST_PLAN_CHAPTERS)));
        String raw = callAi(PromptTemplateUtil.render("creative_plan", vars));
        JsonNode node = AiJsonUtil.parseJsonFromModel(raw);
        AiJsonUtil.validatePlan(node);
        normalizeChapterNos(node.path("chapters"), 1);
        return node.toString();
    }

    private String genChapterPlanBatch(AiCreationPlan plan, AiCreationTask task) {
        JsonNode planNode = readPlanContent(plan);
        JsonNode params = readParams(plan);
        int totalChapters = params.path("totalChapters").asInt(60);
        int startNo = task.getChapterNo();
        int endNo = Math.min(startNo + CHAPTER_PLAN_BATCH_SIZE - 1, totalChapters);
        if (startNo > endNo) {
            throw new BusinessException(ResponseStatus.AI_PARAMS_INVALID);
        }

        //已有规划（方案JSON + 已拆分章节）压缩为标题清单供衔接参考
        StringBuilder existing = new StringBuilder();
        for (AiChapterPlan row : chapterPlanMapper.listByPlanId(plan.getId())) {
            if (row.getChapterNo() < startNo) {
                existing.append("第").append(row.getChapterNo()).append("章 ").append(row.getIndexName()).append('\n');
            }
        }
        Map<String, String> vars = new HashMap<>();
        vars.put("bookName", text(planNode, "bookName"));
        vars.put("genre", text(params, "genre"));
        vars.put("intro", AiJsonUtil.truncate(text(planNode, "intro"), 1000));
        vars.put("mainOutline", AiJsonUtil.truncate(planNode.path("mainOutline").toString(), 1500));
        vars.put("volumes", AiJsonUtil.truncate(planNode.path("volumes").toString(), 1000));
        vars.put("characters", AiJsonUtil.truncate(planNode.path("characters").toString(), 2000));
        vars.put("foreshadows", AiJsonUtil.truncate(planNode.path("foreshadows").toString(), 1000));
        vars.put("chapterWords", String.valueOf(params.path("chapterWords").asInt(3000)));
        vars.put("existingChapters", existing.isEmpty() ? "无" : AiJsonUtil.truncate(existing.toString(), 3000));
        vars.put("startNo", String.valueOf(startNo));
        vars.put("endNo", String.valueOf(endNo));
        vars.put("batchCount", String.valueOf(endNo - startNo + 1));
        String raw = callAi(PromptTemplateUtil.render("chapter_plan_batch", vars));
        JsonNode node = AiJsonUtil.parseJsonFromModel(raw);
        AiJsonUtil.validateChapters(node);
        normalizeChapterNos(node.path("chapters"), startNo);
        return node.toString();
    }

    private String genChapterContent(AiCreationPlan plan, AiCreationTask task) {
        AiChapterPlan row = chapterPlanMapper.selectByPlanAndNo(plan.getId(), task.getChapterNo());
        if (row == null) {
            throw new BusinessException(ResponseStatus.AI_CHAPTER_PLAN_NOT_EXISTS);
        }
        Map<String, String> vars = buildChapterVars(plan, row);
        String raw = AiJsonUtil.stripCodeFence(callAi(PromptTemplateUtil.render("chapter_content", vars)));
        if (raw.isBlank() || raw.length() > 200000) {
            throw new BusinessException(ResponseStatus.AI_GEN_FAILED);
        }
        chapterPlanMapper.markDraft(plan.getId(), task.getChapterNo());
        return raw;
    }

    /**
     * 构建单章创作上下文：结构化设定 + 前文摘要 + 上章结尾 + 当前章节规划，控制规模
     */
    private Map<String, String> buildChapterVars(AiCreationPlan plan, AiChapterPlan row) {
        JsonNode planNode = readPlanContent(plan);
        JsonNode params = readParams(plan);
        Map<String, String> vars = new HashMap<>();
        vars.put("genre", text(params, "genre"));
        vars.put("intro", AiJsonUtil.truncate(text(planNode, "intro"), 800));
        vars.put("worldview", AiJsonUtil.truncate(text(planNode, "worldview"), 2000));
        vars.put("characters", AiJsonUtil.truncate(planNode.path("characters").toString(), 2500));
        vars.put("style", text(params, "style"));
        vars.put("extra", text(params, "extra"));
        vars.put("pref", AiJsonUtil.truncate(getPref(plan.getAuthorId()), 500));
        vars.put("mainOutline", AiJsonUtil.truncate(planNode.path("mainOutline").toString(), 1500));
        vars.put("volume", AiJsonUtil.truncate(planNode.path("volumes").toString(), 1200));
        vars.put("foreshadows", buildPendingForeshadows(planNode, row.getChapterNo()));
        vars.put("recentSummaries", buildRecentSummaries(plan, row.getChapterNo()));
        vars.put("prevTail", buildPrevTail(plan, row.getChapterNo()));
        vars.put("chapterNo", String.valueOf(row.getChapterNo()));
        vars.put("indexName", row.getIndexName());
        vars.put("chapterOutline", AiJsonUtil.truncate(
                row.getChapterOutline() == null ? "" : row.getChapterOutline(), 1200));
        vars.put("chapterWords", String.valueOf(params.path("chapterWords").asInt(3000)));
        return vars;
    }

    private String buildPendingForeshadows(JsonNode planNode, int chapterNo) {
        JsonNode foreshadows = planNode.path("foreshadows");
        if (!foreshadows.isArray() || foreshadows.isEmpty()) {
            return "无";
        }
        ArrayNode pending = MAPPER.createArrayNode();
        for (JsonNode f : foreshadows) {
            if (f.path("payoffChapter").asInt(0) >= chapterNo && pending.size() < 8) {
                pending.add(f);
            }
        }
        return pending.isEmpty() ? "无（均已按计划处理或已越过回收章节）"
                : AiJsonUtil.truncate(pending.toString(), 1200);
    }

    private String buildRecentSummaries(AiCreationPlan plan, int chapterNo) {
        List<AiChapterPlan> rows = chapterPlanMapper.listByPlanId(plan.getId());
        List<AiChapterPlan> before = rows.stream()
                .filter(r -> r.getChapterNo() < chapterNo)
                .toList();
        StringBuilder sb = new StringBuilder();
        //已有规划章节：取最近5条摘要
        List<AiChapterPlan> withSummary = before.stream()
                .filter(r -> r.getSummary() != null && !r.getSummary().isBlank())
                .toList();
        for (AiChapterPlan r : withSummary.subList(Math.max(0, withSummary.size() - 5), withSummary.size())) {
            sb.append("第").append(r.getChapterNo()).append("章 ").append(r.getIndexName())
                    .append("：").append(r.getSummary()).append('\n');
        }
        //绑定现有小说且规划尚未推进到已写章节时，提示续写背景
        if (before.isEmpty() && plan.getBookId() != null) {
            List<BookIndex> indexes = bookService.queryIndexList(plan.getBookId(), null, 1, null);
            if (indexes != null && !indexes.isEmpty()) {
                sb.append("注意：本书已有").append(indexes.size())
                        .append("章既有正文，本次为续写创作，请从现有内容结尾自然衔接，不得重复或推翻已写情节。\n");
            }
        }
        return sb.isEmpty() ? "无（本书开篇）" : AiJsonUtil.truncate(sb.toString(), 2000);
    }

    private String buildPrevTail(AiCreationPlan plan, int chapterNo) {
        Long indexId = null;
        //优先取规划内最近一个已保存章节
        Optional<AiChapterPlan> prevSaved = chapterPlanMapper.listByPlanId(plan.getId()).stream()
                .filter(r -> r.getChapterNo() < chapterNo && r.getStatus() == CHAPTER_SAVED && r.getIndexId() != null)
                .max(Comparator.comparingInt(AiChapterPlan::getChapterNo));
        if (prevSaved.isPresent()) {
            indexId = prevSaved.get().getIndexId();
        } else if (plan.getBookId() != null) {
            //续写场景：取现有小说最新章节
            Book book = bookService.queryBookDetail(plan.getBookId());
            if (book != null) {
                indexId = book.getLastIndexId();
            }
        }
        if (indexId == null) {
            return "无（本书开篇章节）";
        }
        String content = unescapeContent(bookService.queryIndexContent(indexId, plan.getAuthorId()));
        if (content.isBlank()) {
            return "无";
        }
        int start = Math.max(0, content.length() - 500);
        return AiJsonUtil.truncate(content.substring(start), 500);
    }

    private String genCheckRevise(AiCreationPlan plan, AiCreationTask task) {
        AiChapterPlan row = chapterPlanMapper.selectByPlanAndNo(plan.getId(), task.getChapterNo());
        if (row == null) {
            throw new BusinessException(ResponseStatus.AI_CHAPTER_PLAN_NOT_EXISTS);
        }
        AiCreationTask draftTask = latestDraftTask(plan.getId(), task.getChapterNo());
        if (draftTask == null) {
            throw new BusinessException(ResponseStatus.AI_DRAFT_NOT_EXISTS);
        }
        String content = extractDraftContent(draftTask);
        JsonNode params = readParams(plan);
        int targetWords = params.path("chapterWords").asInt(3000);
        Map<String, String> vars = buildChapterVars(plan, row);

        boolean passed = false;
        JsonNode issues = MAPPER.createArrayNode();
        int round = 0;
        for (round = 1; round <= MAX_REVISE_ROUNDS; round++) {
            List<String> progProblems = ChapterTextCheckUtil.check(content, targetWords);
            JsonNode checkNode = AiJsonUtil.parseJsonFromModel(callAi(
                    renderWithContent("chapter_check", vars, content)));
            AiJsonUtil.validateCheck(checkNode);
            issues = checkNode.path("issues");
            passed = checkNode.path("passed").asBoolean(false) && progProblems.isEmpty();
            if (passed || round == MAX_REVISE_ROUNDS) {
                //最后一轮检查未通过时保留当前版本交作者处理，不再无限修订
                break;
            }
            String mergedIssues = mergeIssues(issues, progProblems);
            vars.put("issues", mergedIssues);
            String revised = AiJsonUtil.stripCodeFence(
                    callAi(renderWithContent("chapter_revise", vars, content)));
            if (revised.isBlank() || revised.length() > 200000) {
                throw new BusinessException(ResponseStatus.AI_GEN_FAILED);
            }
            content = revised;
        }
        chapterPlanMapper.markDraft(plan.getId(), task.getChapterNo());
        ObjectNode result = MAPPER.createObjectNode();
        result.put("passed", passed);
        result.put("rounds", round);
        result.set("issues", issues.deepCopy());
        result.put("content", content);
        return result.toString();
    }

    private String mergeIssues(JsonNode aiIssues, List<String> progProblems) {
        StringBuilder sb = new StringBuilder();
        for (JsonNode issue : aiIssues) {
            sb.append("[").append(text(issue, "type")).append("] ")
                    .append(text(issue, "description")).append(" 建议：")
                    .append(text(issue, "suggestion")).append('\n');
        }
        for (String p : progProblems) {
            sb.append("[程序校验] ").append(p).append('\n');
        }
        return AiJsonUtil.truncate(sb.toString(), 3000);
    }

    private String renderWithContent(String template, Map<String, String> vars, String content) {
        Map<String, String> copy = new HashMap<>(vars);
        copy.put("content", AiJsonUtil.truncate(content, 30000));
        return PromptTemplateUtil.render(template, copy);
    }

    /**
     * 从草稿任务结果提取正文：正文任务为纯文本，校验修订任务为JSON的content字段
     */
    private String extractDraftContent(AiCreationTask draftTask) {
        String raw = draftTask.getResult();
        if (raw == null || raw.isBlank()) {
            throw new BusinessException(ResponseStatus.AI_DRAFT_NOT_EXISTS);
        }
        if (draftTask.getTaskType() == TYPE_CHECK_REVISE) {
            return AiJsonUtil.parseJsonFromModel(raw).path("content").asText("");
        }
        return raw;
    }

    private String genChapterSummary(AiCreationPlan plan, AiCreationTask task) {
        AiChapterPlan row = chapterPlanMapper.selectByPlanAndNo(plan.getId(), task.getChapterNo());
        if (row == null || row.getStatus() != CHAPTER_SAVED || row.getIndexId() == null) {
            throw new BusinessException(ResponseStatus.AI_CHAPTER_PLAN_NOT_EXISTS);
        }
        String content = unescapeContent(bookService.queryIndexContent(row.getIndexId(), plan.getAuthorId()));
        if (content.isBlank()) {
            throw new BusinessException(ResponseStatus.AI_GEN_FAILED);
        }
        String summary;
        try {
            Map<String, String> vars = new HashMap<>();
            vars.put("indexName", row.getIndexName());
            vars.put("content", AiJsonUtil.truncate(content, 8000));
            summary = AiJsonUtil.stripCodeFence(callAi(PromptTemplateUtil.render("chapter_summary", vars)));
            summary = AiJsonUtil.truncate(summary, 500);
        } catch (Exception e) {
            //摘要失败降级为程序截取，不影响已保存正文
            log.warn("AI章节摘要失败，降级程序截取 planId={}, chapterNo={}", plan.getId(), task.getChapterNo());
            summary = ChapterTextCheckUtil.fallbackSummary(content);
        }
        chapterPlanMapper.updateSummary(plan.getId(), task.getChapterNo(), summary);
        return summary;
    }

    // ------------------------------------------------------------------
    // 公共辅助
    // ------------------------------------------------------------------

    /**
     * 流式聚合调用模型：与现有AI功能同一客户端与配置，分段响应规避长文本读超时
     */
    private String callAi(String prompt) {
        List<String> chunks;
        try {
            chunks = chatClient.prompt().user(prompt).stream().content()
                    .collectList().block(AI_TIMEOUT);
        } catch (IllegalStateException e) {
            //block(Duration)超时，模型响应挂起或过长，转为业务异常让任务明确失败可重试
            throw new BusinessException(ResponseStatus.AI_GEN_TIMEOUT);
        }
        String text = chunks == null ? "" : String.join("", chunks).trim();
        if (text.isEmpty()) {
            throw new BusinessException(ResponseStatus.AI_GEN_FAILED);
        }
        return text;
    }

    private AiCreationPlan loadOwnedPlan(Long authorId, Long planId) {
        AiCreationPlan plan = planMapper.selectByIdAndAuthor(planId, authorId);
        if (plan == null) {
            throw new BusinessException(ResponseStatus.AI_PLAN_NOT_EXISTS);
        }
        return plan;
    }

    private JsonNode readParams(AiCreationPlan plan) {
        if (plan.getCreativeParams() == null || plan.getCreativeParams().isBlank()) {
            return MAPPER.createObjectNode();
        }
        try {
            return MAPPER.readTree(plan.getCreativeParams());
        } catch (Exception e) {
            throw new BusinessException(ResponseStatus.AI_PARAMS_INVALID);
        }
    }

    private JsonNode readPlanContent(AiCreationPlan plan) {
        if (plan.getPlanContent() == null || plan.getPlanContent().isBlank()) {
            throw new BusinessException(ResponseStatus.AI_PLAN_INVALID);
        }
        try {
            return MAPPER.readTree(plan.getPlanContent());
        } catch (Exception e) {
            throw new BusinessException(ResponseStatus.AI_PLAN_INVALID);
        }
    }

    private String text(JsonNode node, String field) {
        return node.path(field).asText("");
    }

    /**
     * 与现有发布流程一致的正文格式转换
     */
    private String convertText(String text) {
        return text.replaceAll("\\n", "<br>").replaceAll("\\s", "&nbsp;");
    }

    /**
     * 存储格式还原为纯文本（供模型阅读与摘要）
     */
    private String unescapeContent(String content) {
        return content == null ? "" : content.replace("<br>", "\n").replace("&nbsp;", " ");
    }
}

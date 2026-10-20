package com.java2nb.novel.controller;

import com.java2nb.novel.core.bean.UserDetails;
import com.java2nb.novel.core.enums.ResponseStatus;
import com.java2nb.novel.entity.AiCreationPlan;
import com.java2nb.novel.entity.AiCreationTask;
import com.java2nb.novel.entity.Author;
import com.java2nb.novel.entity.Book;
import com.java2nb.novel.service.AiAssistantService;
import com.java2nb.novel.service.AuthorService;
import com.java2nb.novel.vo.AiPlanProgressVO;
import io.github.xxyopen.model.resp.RestResult;
import io.github.xxyopen.web.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * AI 长篇小说创作助手接口（作者后台）
 * 所有接口均校验登录作者身份，方案/任务/章节操作在Service层按author_id做数据隔离
 *
 * @author 11797
 */
@RequestMapping("author/aiAssistant")
@RestController
@Slf4j
@RequiredArgsConstructor
public class AiAssistantController extends BaseController {

    private final AiAssistantService aiAssistantService;

    private final AuthorService authorService;

    /**
     * 初始化或恢复创作方案（bookId=绑定现有小说；不传=新建创作）
     */
    @PostMapping("initPlan")
    public RestResult<AiCreationPlan> initPlan(@RequestParam(value = "planId", required = false) Long planId,
        @RequestParam(value = "bookId", required = false) Long bookId, HttpServletRequest request) {
        Author author = checkAuthor(request);
        return RestResult.ok(aiAssistantService.initPlan(author.getId(), planId, bookId));
    }

    /**
     * 保存创作问答参数
     */
    @PostMapping("saveParams")
    public RestResult<Void> saveParams(@RequestParam("planId") Long planId,
        @RequestParam("creativeParams") String creativeParams, HttpServletRequest request) {
        Author author = checkAuthor(request);
        aiAssistantService.saveParams(author.getId(), planId, creativeParams);
        return RestResult.ok();
    }

    /**
     * 提交异步生成任务（1书名 2创作方案 3章节正文 4校验修订 6章节规划增量）
     */
    @PostMapping("startTask")
    public RestResult<Long> startTask(@RequestParam("planId") Long planId, @RequestParam("taskType") Byte taskType,
        @RequestParam(value = "chapterNo", required = false) Integer chapterNo, HttpServletRequest request) {
        Author author = checkAuthor(request);
        return RestResult.ok(aiAssistantService.startTask(author.getId(), planId, taskType, chapterNo));
    }

    /**
     * 查询任务状态与结果（前端轮询）
     */
    @GetMapping("task/{taskId}")
    public RestResult<AiCreationTask> getTask(@PathVariable("taskId") Long taskId, HttpServletRequest request) {
        Author author = checkAuthor(request);
        return RestResult.ok(aiAssistantService.getTask(author.getId(), taskId));
    }

    /**
     * 查询创作进度（方案+章节计划+任务状态），用于页面加载恢复
     */
    @GetMapping("progress")
    public RestResult<AiPlanProgressVO> progress(@RequestParam("planId") Long planId, HttpServletRequest request) {
        Author author = checkAuthor(request);
        return RestResult.ok(aiAssistantService.getProgress(author.getId(), planId));
    }

    /**
     * 保存（编辑后的）创作方案草稿
     */
    @PostMapping("savePlanContent")
    public RestResult<Void> savePlanContent(@RequestParam("planId") Long planId,
        @RequestParam("planContent") String planContent, HttpServletRequest request) {
        Author author = checkAuthor(request);
        aiAssistantService.savePlanContent(author.getId(), planId, planContent);
        return RestResult.ok();
    }

    /**
     * 确认创作方案（新建小说时同步创建小说记录并拆分章节规划）
     */
    @PostMapping("confirmPlan")
    public RestResult<Void> confirmPlan(@RequestParam("planId") Long planId,
        @RequestParam(value = "planContent", required = false) String planContent,
        @RequestParam(value = "bookName", required = false) String bookName,
        @RequestParam(value = "workDirection", required = false) Byte workDirection,
        @RequestParam(value = "catId", required = false) Integer catId,
        @RequestParam(value = "catName", required = false) String catName, HttpServletRequest request) {
        Author author = checkAuthor(request);
        Book bookInfo = new Book();
        bookInfo.setBookName(bookName);
        bookInfo.setWorkDirection(workDirection);
        bookInfo.setCatId(catId);
        bookInfo.setCatName(catName);
        aiAssistantService.confirmPlan(author.getId(), author.getPenName(), planId, planContent, bookInfo);
        return RestResult.ok();
    }

    /**
     * 修改单个章节规划（仅未保存章节）
     */
    @PostMapping("updateChapterPlan")
    public RestResult<Void> updateChapterPlan(@RequestParam("planId") Long planId,
        @RequestParam("chapterNo") Integer chapterNo, @RequestParam("indexName") String indexName,
        @RequestParam(value = "chapterOutline", required = false) String chapterOutline,
        HttpServletRequest request) {
        Author author = checkAuthor(request);
        aiAssistantService.updateChapterPlan(author.getId(), planId, chapterNo, indexName, chapterOutline);
        return RestResult.ok();
    }

    /**
     * 保存作者编辑后的章节草稿（未正式保存）
     */
    @PostMapping("saveDraft")
    public RestResult<Void> saveDraft(@RequestParam("planId") Long planId, @RequestParam("chapterNo") Integer chapterNo,
        @RequestParam("content") String content, HttpServletRequest request) {
        Author author = checkAuthor(request);
        aiAssistantService.saveDraft(author.getId(), planId, chapterNo, content);
        return RestResult.ok();
    }

    /**
     * 作者确认后保存章节正文到现有章节系统
     */
    @PostMapping("saveChapter")
    public RestResult<Void> saveChapter(@RequestParam("planId") Long planId, @RequestParam("chapterNo") Integer chapterNo,
        @RequestParam("indexName") String indexName, @RequestParam("content") String content,
        @RequestParam(value = "isVip", required = false) Byte isVip, HttpServletRequest request) {
        Author author = checkAuthor(request);
        aiAssistantService.saveChapter(author.getId(), planId, chapterNo, indexName, content, isVip);
        return RestResult.ok();
    }

    /**
     * 程序化文本校验（字数与重复检测）
     */
    @PostMapping("programCheck")
    public RestResult<List<String>> programCheck(@RequestParam("content") String content,
        @RequestParam(value = "targetWords", required = false) Integer targetWords, HttpServletRequest request) {
        checkAuthor(request);
        return RestResult.ok(aiAssistantService.programCheck(content, targetWords));
    }

    /**
     * 查询全局创作偏好
     */
    @GetMapping("pref")
    public RestResult<String> getPref(HttpServletRequest request) {
        Author author = checkAuthor(request);
        return RestResult.ok(aiAssistantService.getPref(author.getId()));
    }

    /**
     * 保存或清除全局创作偏好
     */
    @PostMapping("savePref")
    public RestResult<Void> savePref(@RequestParam(value = "prefContent", required = false) String prefContent,
        HttpServletRequest request) {
        Author author = checkAuthor(request);
        aiAssistantService.savePref(author.getId(), prefContent);
        return RestResult.ok();
    }

    /**
     * 与 AuthorController 一致的作者身份校验：未登录或作者状态异常时拒绝访问
     */
    private Author checkAuthor(HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(request);
        if (userDetails == null) {
            throw new BusinessException(ResponseStatus.NO_LOGIN);
        }
        Author author = authorService.queryAuthor(userDetails.getId());
        if (author == null || author.getStatus() == 1) {
            throw new BusinessException(ResponseStatus.AUTHOR_STATUS_FORBIDDEN);
        }
        return author;
    }

}

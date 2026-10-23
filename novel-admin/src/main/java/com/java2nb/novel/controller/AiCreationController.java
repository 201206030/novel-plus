package com.java2nb.novel.controller;

import com.java2nb.novel.entity.AiChapterPlan;
import com.java2nb.novel.service.AiCreationService;
import com.java2nb.novel.vo.AdminAiPlanVO;
import com.java2nb.novel.vo.AdminAiPrefVO;
import com.java2nb.novel.vo.AdminAiTaskVO;
import io.github.xxyopen.model.page.PageBean;
import io.github.xxyopen.model.resp.RestResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * AI创作模块管理Controller：创作方案/章节规划/生成任务/作者偏好
 *
 * @author Administrator
 */
@RestController
@RequestMapping("ai")
@RequiredArgsConstructor
public class AiCreationController {

    private final AiCreationService aiCreationService;

    @GetMapping("plan/list")
    public RestResult<PageBean<AdminAiPlanVO>> planList(@RequestParam(required = false) String keyword,
        @RequestParam(required = false) Byte stage,
        @RequestParam(value = "curr", defaultValue = "1") int page,
        @RequestParam(value = "limit", defaultValue = "10") int pageSize) {
        return RestResult.ok(aiCreationService.planList(keyword, stage, page, pageSize));
    }

    @PostMapping("plan/remove")
    public RestResult<Void> planRemove(@RequestParam Long id) {
        aiCreationService.planRemove(id);
        return RestResult.ok();
    }

    @GetMapping("chapterPlan/list")
    public RestResult<PageBean<AiChapterPlan>> chapterPlanList(@RequestParam Long planId,
        @RequestParam(required = false) Byte status,
        @RequestParam(value = "curr", defaultValue = "1") int page,
        @RequestParam(value = "limit", defaultValue = "10") int pageSize) {
        return RestResult.ok(aiCreationService.chapterPlanList(planId, status, page, pageSize));
    }

    @PostMapping("chapterPlan/remove")
    public RestResult<Void> chapterPlanRemove(@RequestParam Long id) {
        aiCreationService.chapterPlanRemove(id);
        return RestResult.ok();
    }

    @GetMapping("task/list")
    public RestResult<PageBean<AdminAiTaskVO>> taskList(@RequestParam(required = false) Long planId,
        @RequestParam(required = false) Byte status,
        @RequestParam(required = false) Byte taskType,
        @RequestParam(value = "curr", defaultValue = "1") int page,
        @RequestParam(value = "limit", defaultValue = "10") int pageSize) {
        return RestResult.ok(aiCreationService.taskList(planId, status, taskType, page, pageSize));
    }

    @PostMapping("task/failStuck")
    public RestResult<Void> taskFailStuck(@RequestParam Long id) {
        aiCreationService.taskFailStuck(id);
        return RestResult.ok();
    }

    @PostMapping("task/remove")
    public RestResult<Void> taskRemove(@RequestParam Long id) {
        aiCreationService.taskRemove(id);
        return RestResult.ok();
    }

    @GetMapping("pref/list")
    public RestResult<PageBean<AdminAiPrefVO>> prefList(@RequestParam(required = false) String keyword,
        @RequestParam(value = "curr", defaultValue = "1") int page,
        @RequestParam(value = "limit", defaultValue = "10") int pageSize) {
        return RestResult.ok(aiCreationService.prefList(keyword, page, pageSize));
    }

    @PostMapping("pref/remove")
    public RestResult<Void> prefRemove(@RequestParam Long id) {
        aiCreationService.prefRemove(id);
        return RestResult.ok();
    }

}

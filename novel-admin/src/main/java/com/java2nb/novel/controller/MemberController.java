package com.java2nb.novel.controller;

import com.java2nb.novel.entity.User;
import com.java2nb.novel.entity.UserFeedback;
import com.java2nb.novel.service.MemberService;
import com.java2nb.novel.vo.AdminUserFeedbackVO;
import io.github.xxyopen.model.page.PageBean;
import io.github.xxyopen.model.resp.RestResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 会员管理Controller：会员列表/会员反馈
 *
 * @author Administrator
 */
@RestController
@RequestMapping("member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("user/list")
    public RestResult<PageBean<User>> userList(@RequestParam(required = false) String keyword,
        @RequestParam(value = "curr", defaultValue = "1") int page,
        @RequestParam(value = "limit", defaultValue = "10") int pageSize) {
        return RestResult.ok(memberService.userList(keyword, page, pageSize));
    }

    @GetMapping("user/get")
    public RestResult<User> userGet(@RequestParam Long id) {
        return RestResult.ok(memberService.userGet(id));
    }

    @PostMapping("user/save")
    public RestResult<Void> userSave(User user) {
        memberService.userSave(user);
        return RestResult.ok();
    }

    @PostMapping("user/update")
    public RestResult<Void> userUpdate(User user) {
        memberService.userUpdate(user);
        return RestResult.ok();
    }

    @PostMapping("user/remove")
    public RestResult<Void> userRemove(@RequestParam Long id) {
        memberService.userRemove(id);
        return RestResult.ok();
    }

    @PostMapping("user/batchRemove")
    public RestResult<Void> userBatchRemove(@RequestParam String ids) {
        memberService.userBatchRemove(ids);
        return RestResult.ok();
    }

    @PostMapping("user/ban")
    public RestResult<Void> userBan(@RequestParam Long id, @RequestParam Byte status) {
        memberService.userBan(id, status);
        return RestResult.ok();
    }

    @PostMapping("user/adjustBalance")
    public RestResult<Long> userAdjustBalance(@RequestParam Long id, @RequestParam Long amount) {
        return RestResult.ok(memberService.userAdjustBalance(id, amount));
    }

    @GetMapping("userFeedback/list")
    public RestResult<PageBean<AdminUserFeedbackVO>> feedbackList(@RequestParam(required = false) String keyword,
        @RequestParam(value = "curr", defaultValue = "1") int page,
        @RequestParam(value = "limit", defaultValue = "10") int pageSize) {
        return RestResult.ok(memberService.feedbackList(keyword, page, pageSize));
    }

    @GetMapping("userFeedback/get")
    public RestResult<UserFeedback> feedbackGet(@RequestParam Long id) {
        return RestResult.ok(memberService.feedbackGet(id));
    }

    @PostMapping("userFeedback/save")
    public RestResult<Void> feedbackSave(UserFeedback feedback) {
        memberService.feedbackSave(feedback);
        return RestResult.ok();
    }

    @PostMapping("userFeedback/update")
    public RestResult<Void> feedbackUpdate(UserFeedback feedback) {
        memberService.feedbackUpdate(feedback);
        return RestResult.ok();
    }

    @PostMapping("userFeedback/remove")
    public RestResult<Void> feedbackRemove(@RequestParam Long id) {
        memberService.feedbackRemove(id);
        return RestResult.ok();
    }

    @PostMapping("userFeedback/batchRemove")
    public RestResult<Void> feedbackBatchRemove(@RequestParam String ids) {
        memberService.feedbackBatchRemove(ids);
        return RestResult.ok();
    }

}

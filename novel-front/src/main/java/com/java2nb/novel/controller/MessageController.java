package com.java2nb.novel.controller;

import com.java2nb.novel.core.bean.UserDetails;
import com.java2nb.novel.core.enums.ResponseStatus;
import com.java2nb.novel.service.UserMessageService;
import io.github.xxyopen.model.resp.RestResult;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户消息（评论/段评被回复通知）
 *
 * @author 11797
 */
@RestController
@RequestMapping("user/message")
@RequiredArgsConstructor
@Slf4j
public class MessageController extends BaseController {

    private final UserMessageService userMessageService;

    /**
     * 分页查询我的消息，msgType 可选过滤：1书评回复 2段评回复
     */
    @GetMapping("listByPage")
    public RestResult<?> listByPage(@RequestParam(value = "curr", defaultValue = "1") int page,
        @RequestParam(value = "limit", defaultValue = "10") int pageSize,
        @RequestParam(value = "msgType", required = false) Byte msgType, HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(request);
        if (userDetails == null) {
            return RestResult.fail(ResponseStatus.NO_LOGIN);
        }
        return RestResult.ok(userMessageService.listMyMessageByPage(userDetails.getId(), msgType, page, pageSize));
    }

    /**
     * 未读消息数（个人中心角标用）
     */
    @GetMapping("unreadCount")
    public RestResult<?> unreadCount(HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(request);
        if (userDetails == null) {
            return RestResult.fail(ResponseStatus.NO_LOGIN);
        }
        return RestResult.ok(userMessageService.countUnread(userDetails.getId()));
    }

    /**
     * 单条标记已读
     */
    @PostMapping("read")
    public RestResult<Void> read(@RequestParam("messageId") Long messageId, HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(request);
        if (userDetails == null) {
            return RestResult.fail(ResponseStatus.NO_LOGIN);
        }
        userMessageService.markRead(userDetails.getId(), messageId);
        return RestResult.ok();
    }

    /**
     * 全部标记已读
     */
    @PostMapping("readAll")
    public RestResult<Void> readAll(HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(request);
        if (userDetails == null) {
            return RestResult.fail(ResponseStatus.NO_LOGIN);
        }
        userMessageService.markAllRead(userDetails.getId());
        return RestResult.ok();
    }
}

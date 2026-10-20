package com.java2nb.novel.controller;

import com.java2nb.novel.entity.UserMessage;
import com.java2nb.novel.service.MessageService;
import com.java2nb.novel.vo.AdminUserMessageVO;
import io.github.xxyopen.model.page.PageBean;
import io.github.xxyopen.model.resp.RestResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 消息管理Controller
 *
 * @author Administrator
 */
@RestController
@RequestMapping("message")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping("userMessage/list")
    public RestResult<PageBean<AdminUserMessageVO>> list(@RequestParam(required = false) Long userId,
        @RequestParam(required = false) Byte msgType,
        @RequestParam(required = false) Byte readFlag,
        @RequestParam(value = "curr", defaultValue = "1") int page,
        @RequestParam(value = "limit", defaultValue = "10") int pageSize) {
        return RestResult.ok(messageService.list(userId, msgType, readFlag, page, pageSize));
    }

    @GetMapping("userMessage/get")
    public RestResult<UserMessage> get(@RequestParam Long id) {
        return RestResult.ok(messageService.get(id));
    }

    @PostMapping("userMessage/send")
    public RestResult<Void> send(UserMessage userMessage) {
        messageService.send(userMessage);
        return RestResult.ok();
    }

    @PostMapping("userMessage/remove")
    public RestResult<Void> remove(@RequestParam Long id) {
        messageService.remove(id);
        return RestResult.ok();
    }

    @PostMapping("userMessage/batchRemove")
    public RestResult<Void> batchRemove(@RequestParam String ids) {
        messageService.batchRemove(ids);
        return RestResult.ok();
    }

}

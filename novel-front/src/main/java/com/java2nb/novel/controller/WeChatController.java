package com.java2nb.novel.controller;

import com.java2nb.novel.core.except.AesException;
import com.java2nb.novel.core.utils.WXPublicUtils;
import com.java2nb.novel.dto.WxMessageDto;
import com.java2nb.novel.service.WeChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zhu
 * @date 2023/5/9
 * @description
 */
@RestController
@RequestMapping("/wechat")
@RequiredArgsConstructor
@Slf4j
public class WeChatController {

    @Autowired
    private WeChatService weChatService;

    @GetMapping("/")
    @ResponseBody
    public String weCheck(HttpServletRequest request) throws AesException {
        return weChatService.weCheck(request);
    }

    @PostMapping("/")
    @ResponseBody
    public String getMessage(HttpServletRequest request,@RequestBody WxMessageDto dto) {
        return weChatService.getMessage(request, dto);
    }
}

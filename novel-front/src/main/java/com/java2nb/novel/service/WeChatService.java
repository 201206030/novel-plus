package com.java2nb.novel.service;

import com.java2nb.novel.core.except.AesException;
import com.java2nb.novel.dto.WxMessageDto;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhu
 * @date 2023/5/9
 * @description
 */
public interface WeChatService {
    /**
     *
     * @param request
     * @return
     * @throws AesException
     */
    String weCheck(HttpServletRequest request) throws AesException;

    /**
     *
     * @param request
     * @param dto
     * @return
     */
    String getMessage(HttpServletRequest request, WxMessageDto dto);
}

package com.java2nb.novel.core.advice;

import io.github.xxyopen.model.resp.RestResult;
import io.github.xxyopen.model.resp.SysResultCode;
import io.github.xxyopen.web.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CommonExceptionHandler {

    public CommonExceptionHandler() {
    }

    @ExceptionHandler({BindException.class})
    public RestResult<Void> handlerBindException(BindException e) {
        log.error(e.getMessage(), e);
        return RestResult.fail(SysResultCode.PARAM_ERROR);
    }

    @ExceptionHandler({BusinessException.class})
    public RestResult<Void> handlerBusinessException(BusinessException e) {
        log.error(e.getMessage(), e);
        return RestResult.fail(e.getResultCode());
    }

    @ExceptionHandler({Exception.class})
    public RestResult<Void> handlerException(Exception e) {
        log.error(e.getMessage(), e);
        return RestResult.error();
    }
}
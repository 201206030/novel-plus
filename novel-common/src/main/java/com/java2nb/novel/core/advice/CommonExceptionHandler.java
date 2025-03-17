package com.java2nb.novel.core.advice;

import io.github.xxyopen.model.resp.RestResult;
import io.github.xxyopen.model.resp.SysResultCode;
import io.github.xxyopen.web.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CommonExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(io.github.xxyopen.web.exception.CommonExceptionHandler.class);

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
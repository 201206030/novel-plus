package com.java2nb.novel.core.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 页面异常处理器
 *
 * @author 11797
 */
@Slf4j
@ControllerAdvice(basePackages = "com.java2nb.novel.page")
public class PageExceptionHandler {


    /**
     * 处理所有异常
     */
    @ExceptionHandler(Exception.class)
    public String handlerException(Exception e) {
        log.error(e.getMessage(), e);
        //跳转页面过程中出现异常时统一跳转到404页面
        return "404";
    }
}

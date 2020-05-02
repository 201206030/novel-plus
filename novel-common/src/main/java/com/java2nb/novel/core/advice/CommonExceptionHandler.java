package com.java2nb.novel.core.advice;

import com.java2nb.novel.core.bean.ResultBean;
import com.java2nb.novel.core.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 通用的异常处理器
 *
 * @author 11797*/
@Slf4j
@ControllerAdvice
@ResponseBody
public class CommonExceptionHandler {

    /**
     * 处理业务异常
     * */
    @ExceptionHandler(BusinessException.class)
    public ResultBean handlerBusinessException(BusinessException e){
        log.error(e.getMessage(),e);
        return ResultBean.fail(e.getResStatus());
    }


    /**
     * 处理系统异常
     * */
    @ExceptionHandler(Exception.class)
    public ResultBean handlerException(Exception e){
        log.error(e.getMessage(),e);
        return ResultBean.error();
    }
}

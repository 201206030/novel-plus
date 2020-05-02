package com.java2nb.novel.core.exception;

import com.java2nb.novel.core.enums.ResponseStatus;
import lombok.Data;

/**
 * 自定义业务异常，用于处理用户请求时，业务错误时抛出
 */
@Data
public class BusinessException extends RuntimeException {

    private ResponseStatus resStatus;

    public BusinessException(ResponseStatus resStatus) {
        //不调用父类Throwable的fillInStackTrace()方法生成栈追踪信息，提高应用性能
        //构造器之间的调用必须在第一行
        super(resStatus.getMsg(), null, false, false);
        this.resStatus = resStatus;
    }


}

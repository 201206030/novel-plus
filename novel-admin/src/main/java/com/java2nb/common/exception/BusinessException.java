package com.java2nb.common.exception;

import lombok.Data;

/**
 * 自定义业务异常
 */
@Data
public class BusinessException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
    private String msg;
    private int code;
    
    public BusinessException(int code,String msg) {
		//不调用父类Throwable的fillInStackTrace()方法生成栈追踪信息，提高应用性能
		//构造器之间的调用必须在第一行
		super(msg, null, false, false);
		this.code = code;
		this.msg = msg;
	}
	

	
}

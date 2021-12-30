package com.java2nb.novel.core.bean;


import com.java2nb.novel.core.enums.ResponseStatus;
import lombok.Data;

import java.io.Serializable;

/**
 * 自定义响应结构
 *
 * @param <T>
 * @author 11797
 */
@Data
public class ResultBean<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private int code = ResponseStatus.OK.getCode();

    /**
     * 响应消息
     */
    private String msg = ResponseStatus.OK.getMsg();
    /**
     * 响应中的数据
     */
    private T data;

    private ResultBean() {

    }

    private ResultBean(ResponseStatus responseStatus) {
        this.code = responseStatus.getCode();
        this.msg = responseStatus.getMsg();
    }

    private ResultBean(T data) {
        this.data = data;
    }


    /**
     * 业务处理成功,无数据返回
     */
    public static ResultBean<Void> ok() {
        return new ResultBean<>();
    }

    /**
     * 业务处理成功，有数据返回
     */
    public static <T> ResultBean<T> ok(T data) {
        return new ResultBean<>(data);
    }

    /**
     * 业务处理失败
     */
    public static ResultBean<Void> fail(ResponseStatus responseStatus) {
        return new ResultBean<>(responseStatus);
    }


    /**
     * 系统错误
     */
    public static ResultBean<Void> error() {
        return new ResultBean<>(ResponseStatus.ERROR);
    }
}


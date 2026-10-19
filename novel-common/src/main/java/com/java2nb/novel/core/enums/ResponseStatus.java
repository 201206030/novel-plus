package com.java2nb.novel.core.enums;

import io.github.xxyopen.model.resp.IResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author 11797
 */

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ResponseStatus implements IResultCode {


    /**
     * 用户相关错误
     * */
   NO_LOGIN(1001, "未登录或登录失效！"),
    VEL_CODE_ERROR(1002, "验证码错误！"),
    USERNAME_EXIST(1003,"该手机号已注册！"),
    USERNAME_PASS_ERROR(1004,"手机号或密码错误！"),
    TWO_PASSWORD_DIFF(1005, "两次输入的新密码不匹配!"),
    OLD_PASSWORD_ERROR(1006, "旧密码不匹配!"),
    USER_NO_BALANCE(1007, "用户余额不足"),

    /**
     * 评论相关错误
     * */
    HAS_COMMENTS(3001, "已评价过该书籍！"),
    PARA_COMMENT_CONTENT_INVALID(3002, "段落评论不能为空且不能超过200个字！"),
    PARA_COMMENT_PARAM_INVALID(3003, "段落评论参数有误！"),

    /**
     * 作者相关错误
     * */
    INVITE_CODE_INVALID(4001, "邀请码无效！"),
    AUTHOR_STATUS_FORBIDDEN(4002, "作者状态异常，暂不能管理小说！")
    , BOOKNAME_EXISTS(4003,"已发布过同名小说！"),

    /**
     * 小说相关错误
     */
    BOOK_EXISTS(5001,"该小说已存在"),

    /**
     * 书单相关错误
     * */
    LIST_NOT_EXISTS(6001, "书单不存在或已删除！"),
    NOT_MY_LIST(6002, "只能操作自己创建的书单！"),
    BOOK_IN_LIST(6003, "该书籍已在书单中！"),
    LIST_NAME_EXISTS(6004, "已创建过同名书单！"),
    LIST_NAME_INVALID(6005, "书单名不能为空且不能超过50个字！"),
    LIST_DESC_TOO_LONG(6006, "书单简介不能超过300个字！"),
    BOOK_NOT_EXISTS(6007, "书籍不存在！"),

    /**
     * 搜索引擎相关错误
     * */
    ES_SEARCH_FAIL(9001,"搜索引擎查询错误！"),

    /**
     * 文件相关错误
     * */
    FILE_DIR_MAKE_FAIL(10001,"目录创建失败"),
    FILE_NOT_IMAGE(10002,"请上传图片类型的文件"),
    FILE_SIZE_LIMIT(10003,"文件大小超出限制"),

    /**
     * 其他通用错误
     * */
    PASSWORD_ERROR(88001,"密码错误！");

    private int code;
    private String msg;


}

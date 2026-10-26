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
    USER_STATUS_FORBIDDEN(1008, "账号已被封禁，请联系管理员！"),

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
    PEN_NAME_EXISTS(4004,"笔名已被使用，请更换一个！"),
    AUTHOR_INFO_INVALID(4005,"资料填写有误，请检查笔名/手机号/邮箱格式！"),

    /**
     * 小说相关错误
     */
    BOOK_EXISTS(5001,"该小说已存在"),
    INDEX_NOT_EXISTS(5002,"该章节不存在！"),
    NEWS_NOT_EXISTS(5003,"该新闻不存在！"),
    INDEX_PUBLISH_TIME_INVALID(5004,"定时发布时间需晚于当前时间！"),
    INDEX_ALREADY_PUBLISHED(5005,"章节已发布，不能再撤回或改期！"),

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
     * AI创作助手相关错误
     * */
    AI_PLAN_NOT_EXISTS(7001, "创作方案不存在或无权访问！"),
    AI_TASK_RUNNING(7002, "该创作任务正在执行中，请勿重复提交！"),
    AI_GEN_FAILED(7003, "AI生成失败，请重试！"),
    AI_PLAN_INVALID(7004, "创作方案内容格式有误！"),
    AI_PARAMS_INVALID(7005, "创作参数不合法！"),
    AI_CHAPTER_SAVED(7006, "该章节已保存，请前往章节管理修改！"),
    AI_PLAN_NOT_CONFIRMED(7007, "请先确认创作方案后再创作章节！"),
    AI_CHAPTER_PLAN_NOT_EXISTS(7008, "该章节不在章节规划中，请先检查创作方案！"),
    AI_DRAFT_NOT_EXISTS(7009, "暂无章节草稿，请先生成正文！"),
    AI_GEN_TIMEOUT(7010, "AI生成超时，请重试或适当调低单章目标字数！"),

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
     * 第三方规则导入相关错误
     * */
    BOOK_SOURCE_EMPTY(11001, "导入内容不能为空！"),
    BOOK_SOURCE_PARSE_FAIL(11002, "规则JSON解析失败，请确认符合所选规则类型格式！"),
    RULE_SOURCE_TYPE_UNSUPPORTED(11003, "不支持的规则类型！"),
    CRAWL_SOURCE_NOT_EXISTS(11004, "爬虫源不存在或已被删除！"),
    CRAWL_SOURCE_RUNNING(11005, "爬虫源正在运行中，请先停止后再删除！"),
    BOOK_SOURCE_TOO_MANY(11006, "规则条数过多，单次预览最多支持5000条，请拆分后再导入！"),

    /**
     * 其他通用错误
     * */
    PASSWORD_ERROR(88001,"密码错误！");

    private int code;
    private String msg;


}

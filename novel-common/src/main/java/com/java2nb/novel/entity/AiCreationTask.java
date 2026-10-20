package com.java2nb.novel.entity;

import lombok.Data;

import java.util.Date;

/**
 * AI创作生成任务
 *
 * @author 11797
 */
@Data
public class AiCreationTask {

    private Long id;

    /**
     * 创作方案ID
     */
    private Long planId;

    /**
     * 作者ID（权限隔离）
     */
    private Long authorId;

    /**
     * 任务类型：1书名建议 2创作方案 3章节正文 4章节校验修订 5章节摘要 6章节规划增量
     */
    private Byte taskType;

    /**
     * 目标章节序号（非章节任务为0）
     */
    private Integer chapterNo;

    /**
     * 状态：0执行中 1成功 2失败
     */
    private Byte status;

    /**
     * 执行中标记（执行中=1，完成置NULL，配合唯一索引防止同一目标重复提交）
     */
    private Byte runningFlag;

    /**
     * 生成结果（结构化任务为JSON，正文任务为纯文本草稿）
     */
    private String result;

    /**
     * 最近一次失败原因
     */
    private String errorMsg;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}

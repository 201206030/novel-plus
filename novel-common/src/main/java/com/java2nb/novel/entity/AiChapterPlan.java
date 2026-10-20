package com.java2nb.novel.entity;

import lombok.Data;

import java.util.Date;

/**
 * AI章节计划
 *
 * @author 11797
 */
@Data
public class AiChapterPlan {

    private Long id;

    /**
     * 创作方案ID
     */
    private Long planId;

    /**
     * 章节序号（从1开始）
     */
    private Integer chapterNo;

    /**
     * 章节标题
     */
    private String indexName;

    /**
     * 章节规划JSON（主要事件/冲突/人物变化/主线关系/结尾悬念）
     */
    private String chapterOutline;

    /**
     * 状态：0待创作 1草稿已生成未保存 2已保存为正式章节 3保存中（事务内瞬时态）
     */
    private Byte status;

    /**
     * 已保存的正式章节ID（book_index.id）
     */
    private Long indexId;

    /**
     * 已保存章节字数
     */
    private Integer wordCount;

    /**
     * 已完成章节摘要（创作记忆）
     */
    private String summary;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}

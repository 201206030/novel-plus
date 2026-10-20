package com.java2nb.novel.entity;

import lombok.Data;

import java.util.Date;

/**
 * AI创作方案
 *
 * @author 11797
 */
@Data
public class AiCreationPlan {

    private Long id;

    /**
     * 作者ID
     */
    private Long authorId;

    /**
     * 关联小说ID（新建小说确认方案后回填）
     */
    private Long bookId;

    /**
     * 创作阶段：1创作问答中 2方案草稿 3方案已确认 4创作完成
     */
    private Byte stage;

    /**
     * 创作问答参数JSON
     */
    private String creativeParams;

    /**
     * 创作方案JSON（书名/卖点/简介/世界观/人物档案/总纲/分卷大纲/伏笔/章节规划）
     */
    private String planContent;

    /**
     * 大纲版本号（每次确认方案递增）
     */
    private Integer outlineVersion;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}

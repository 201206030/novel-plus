package com.java2nb.novel.vo;

import lombok.Data;

/**
 * 阅读3.0书源确认导入项
 *
 * @author xiongxiaoyang
 */
@Data
public class BookSourceImportConfirmVO {

    /**
     * 书源名称
     */
    private String sourceName;

    /**
     * 预览接口返回的转换后采集规则JSON，原样回传
     */
    private String crawlRule;

    /**
     * 该书源是否存在可选的发现分类（由预览条目携带），存在时必须完成至少一个分类映射
     */
    private Boolean hasExploreOptions;

    /**
     * 规则类型（如YUEDU3），入库时据此写规则来源列，空值按阅读3.0处理
     */
    private String sourceType;

}

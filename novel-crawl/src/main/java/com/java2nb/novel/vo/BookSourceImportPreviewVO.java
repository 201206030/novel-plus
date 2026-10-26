package com.java2nb.novel.vo;

import com.java2nb.novel.core.booksource.BookSourceConvertResult;
import lombok.Data;

import java.util.List;

/**
 * 阅读3.0书源导入预览VO
 *
 * @author xiongxiaoyang
 */
@Data
public class BookSourceImportPreviewVO {

    /**
     * 书源名称
     */
    private String sourceName;

    /**
     * 书源站点地址
     */
    private String sourceUrl;

    /**
     * 导入状态：OK完整可导入，PARTIAL可导入但有信息损失，FAILED不可导入
     */
    private String status;

    /**
     * 是否可导入
     */
    private Boolean importable;

    /**
     * 信息损失警告
     */
    private List<String> warnings;

    /**
     * 不可导入的原因
     */
    private List<String> errors;

    /**
     * 转换后的采集规则JSON（FAILED时为null），确认导入时原样回传
     */
    private String crawlRule;

    /**
     * 发现分类条目列表，供前端手动映射到novel-plus分类槽catId1-7
     */
    private List<BookSourceConvertResult.ExploreOption> exploreOptions;

}

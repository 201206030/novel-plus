package com.java2nb.novel.core.booksource;

import com.java2nb.novel.core.crawl.RuleBean;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 阅读3.0书源导入转换结果
 *
 * @author xiongxiaoyang
 */
@Data
public class BookSourceConvertResult {

    /**
     * 导入可行性状态
     */
    public enum Status {
        /**
         * 完整可导入
         */
        OK,
        /**
         * 可导入但有信息损失（部分规则被丢弃或近似转换）
         */
        PARTIAL,
        /**
         * 不可导入（核心规则缺失或无法转换）
         */
        FAILED
    }

    private String sourceName;

    private String sourceUrl;

    private Status status;

    /**
     * 转换过程中的信息损失警告
     */
    private final List<String> warnings = new ArrayList<>();

    /**
     * 不可导入的原因（仅FAILED时有值）
     */
    private final List<String> errors = new ArrayList<>();

    /**
     * 转换后的采集规则，FAILED时为null
     */
    private RuleBean ruleBean;

    /**
     * 采集规则JSON文本（供前端展示与确认导入时回传），FAILED时为null
     */
    private String crawlRule;

    /**
     * 解析出的发现（探索）分类条目，导入时由用户手动映射到novel-plus分类槽
     */
    private final List<ExploreOption> exploreOptions = new ArrayList<>();

    public boolean isImportable() {
        return status != Status.FAILED;
    }

    /**
     * 发现分类条目：阅读书源侧的分类名称与已补全的分类列表页URL（含{page}占位）
     */
    public static class ExploreOption {

        private final String title;

        private final String url;

        public ExploreOption(String title, String url) {
            this.title = title;
            this.url = url;
        }

        public String getTitle() {
            return title;
        }

        public String getUrl() {
            return url;
        }
    }

}
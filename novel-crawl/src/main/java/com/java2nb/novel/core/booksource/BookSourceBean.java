package com.java2nb.novel.core.booksource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Map;

/**
 * 阅读3.0（LEGUMI）书源JSON的数据模型，只建模导入转换需要的字段，其余字段忽略
 *
 * @author xiongxiaoyang
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookSourceBean {

    /**
     * 书源名称
     */
    private String bookSourceName;

    /**
     * 书源站点地址，如 https://www.sudugu.co
     */
    private String bookSourceUrl;

    /**
     * 书源分组
     */
    private String bookSourceGroup;

    /**
     * 书源注释
     */
    private String bookSourceComment;

    /**
     * 发现（探索）页配置，"分类名::相对路径"按行分隔，或JSON数组格式
     */
    private String exploreUrl;

    /**
     * 搜索URL规则，novel-plus无在线搜索能力，仅用于探测站点编码
     */
    private String searchUrl;

    /**
     * 自定义请求头
     */
    private String header;

    /**
     * 登录页地址
     */
    private String loginUrl;

    /**
     * 是否启用Cookie
     */
    private Boolean enabledCookieJar;

    /**
     * 书源是否启用
     */
    private Boolean enabled;

    /**
     * 详情页规则：name/author/coverUrl/intro/kind/wordCount/tocUrl等
     */
    private Map<String, String> ruleBookInfo;

    /**
     * 搜索页规则：bookList/bookUrl/name/author等
     */
    private Map<String, String> ruleSearch;

    /**
     * 发现页规则：bookList/bookUrl/name/author等
     */
    private Map<String, String> ruleExplore;

    /**
     * 目录页规则：chapterList/chapterName/chapterUrl/nextTocUrl等
     */
    private Map<String, String> ruleToc;

    /**
     * 正文页规则：content/replaceRegex/nextContentUrl等
     */
    private Map<String, String> ruleContent;

}

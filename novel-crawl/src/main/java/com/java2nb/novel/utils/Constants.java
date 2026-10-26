package com.java2nb.novel.utils;

/**
 * @author Administrator
 */
public class Constants {

    /**
     * 本地图片保存前缀
     */
    public static final String LOCAL_PIC_PREFIX = "/localPic/";

    /**
     * 爬虫源默认规则来源（手工创建的规则）
     */
    public static final String DEFAULT_SOURCE_FROM = "小说精品屋";

    /**
     * 访问量默认值
     */
    public static final Long VISIT_COUNT_DEFAULT = 100L;

    /**
     * 爬取小说http请求中无效的内容长度
     */
    public static final int INVALID_HTML_LENGTH = 1500;

    /**
     * 爬取小说http请求失败重试次数
     */
    public static final Integer HTTP_FAIL_RETRY_COUNT = 3;

    /**
     * 爬虫默认编码
     */
    public static final String CRAWL_DEFAULT_CHARSET = "UTF-8";

    /**
     * 目录分页采集的最大页数上限（防下一页链接成环）
     */
    public static final int MAX_INDEX_PAGE = 50;

    /**
     * 单章正文分页拼接的最大页数上限（防下一页链接成环）
     */
    public static final int MAX_CONTENT_PAGE = 20;
}

package com.java2nb.novel.utils;

/**
 * @author Administrator
 */
public class Constants {

    /**
     * 本地图片保存前缀
     * */
    public static final String LOCAL_PIC_PREFIX = "/localPic/";

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
    public static final Integer HTTP_FAIL_RETRY_COUNT = 5;
}

package com.java2nb.novel.core.crawl;

import lombok.Data;

import java.util.Map;

/**
 * 爬虫解析规则bean
 * @author Administrator
 */
@Data
public class RuleBean {

    /**
     * 小说更新列表url
     * */
    private String updateBookListUrl;

    /**
     * 分类列表页URL规则
     * */
    private String bookListUrl;

    private Map<String,String> catIdRule;

    private Map<String,Byte> bookStatusRule;

    private String bookIdPatten;
    private String pagePatten;
    private String totalPagePatten;
    private String bookDetailUrl;
    private String bookNamePatten;
    private String authorNamePatten;
    private String picUrlPatten;
    private String statusPatten;
    private String scorePatten;
    private String visitCountPatten;
    private String descStart;
    private String descEnd;
    private String upadateTimePatten;
    private String upadateTimeFormatPatten;
    private String bookIndexUrl;
    private String indexIdPatten;
    private String indexNamePatten;
    private String bookContentUrl;
    private String contentStart;
    private String contentEnd;


    private String picUrlPrefix;

    private String bookIndexStart;


}

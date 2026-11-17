package com.java2nb.novel.service;

import java.util.Map;

/**
 * AI 辅助生成爬虫规则
 *
 * @author xiongxiaoyang
 */
public interface AiRuleService {

    /**
     * 根据示例页面 URL 生成爬虫规则
     *
     * @param listUrl    列表页示例 URL（可空）
     * @param detailUrl  详情页示例 URL（可空）
     * @param indexUrl   目录页示例 URL（可空）
     * @param contentUrl 正文页示例 URL（可空）
     * @param parseType  解析方式 REGEX/CSS/XPATH/JSONPATH
     * @param charset    网站编码
     * @param clientIp   调用方 IP，用于频率限制
     * @return 规则字段名 -> 规则值，供前端回填表单
     */
    Map<String, String> generateRules(String listUrl, String detailUrl, String indexUrl, String contentUrl,
                                      String parseType, String charset, String clientIp);
}

package com.java2nb.novel.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java2nb.novel.core.booksource.BookSourceBean;
import com.java2nb.novel.core.booksource.BookSourceConvertResult;
import com.java2nb.novel.core.booksource.BookSourceRuleConverter;
import com.java2nb.novel.core.crawl.RuleBean;
import com.java2nb.novel.core.enums.ResponseStatus;
import com.java2nb.novel.entity.CrawlSource;
import com.java2nb.novel.mapper.CrawlSourceDynamicSqlSupport;
import com.java2nb.novel.mapper.CrawlSourceMapper;
import com.java2nb.novel.vo.BookSourceImportConfirmVO;
import com.java2nb.novel.vo.BookSourceImportPreviewVO;
import io.github.xxyopen.web.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

/**
 * 第三方规则导入服务：预览转换结果与确认入库。
 * 目前支持的规则类型：阅读3.0书源（YUEDU3），其他类型由导入策略扩展
 *
 * @author xiongxiaoyang
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BookSourceImportService {

    /**
     * 规则类型：阅读3.0书源
     */
    public static final String SOURCE_TYPE_YUEDU3 = "YUEDU3";

    /**
     * 单次预览允许的最大规则条数，超出直接拒绝，避免同步转换与超大响应导致请求超时
     */
    private static final int MAX_PREVIEW_COUNT = 5000;

    private final CrawlSourceMapper crawlSourceMapper;

    private final BookSourceRuleConverter converter = new BookSourceRuleConverter();

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 按规则类型解析导入内容并逐条转换，返回预览结果列表。
     * sourceType为空时默认阅读3.0书源；目前仅支持YUEDU3，其他类型抛不支持异常
     */
    public List<BookSourceImportPreviewVO> preview(String bookSourceJson, String sourceType) {
        String type = StringUtils.defaultIfBlank(sourceType, SOURCE_TYPE_YUEDU3);
        if (!SOURCE_TYPE_YUEDU3.equals(type)) {
            throw new BusinessException(ResponseStatus.RULE_SOURCE_TYPE_UNSUPPORTED);
        }
        if (StringUtils.isBlank(bookSourceJson)) {
            throw new BusinessException(ResponseStatus.BOOK_SOURCE_EMPTY);
        }
        List<BookSourceBean> sources = parseSources(bookSourceJson);
        if (sources.isEmpty()) {
            throw new BusinessException(ResponseStatus.BOOK_SOURCE_PARSE_FAIL);
        }
        if (sources.size() > MAX_PREVIEW_COUNT) {
            //限制单次转换规模，防止超大书源文件拖垮预览接口
            throw new BusinessException(ResponseStatus.BOOK_SOURCE_TOO_MANY);
        }
        List<BookSourceImportPreviewVO> previews = new ArrayList<>(sources.size());
        for (BookSourceBean source : sources) {
            BookSourceConvertResult result = converter.convert(source);
            previews.add(toPreviewVO(result));
        }
        return previews;
    }

    /**
     * 确认导入勾选项，写入crawl_source并默认停用。
     * 返回成功数、跳过（同名/非法规则/未映射分类）明细
     */
    public ImportSummary confirm(List<BookSourceImportConfirmVO> items) {
        ImportSummary summary = new ImportSummary();
        if (items == null || items.isEmpty()) {
            return summary;
        }
        Set<String> batchNames = new HashSet<>();
        Date now = new Date();
        for (BookSourceImportConfirmVO item : items) {
            String sourceName = StringUtils.trimToNull(item.getSourceName());
            if (sourceName == null || StringUtils.isBlank(item.getCrawlRule())) {
                summary.addSkip(sourceName == null ? "(未命名规则)" : sourceName, "名称或规则为空");
                continue;
            }
            //校验规则可解析，防止前端传入非法JSON
            RuleBean ruleBean;
            try {
                ruleBean = OBJECT_MAPPER.readValue(item.getCrawlRule(), RuleBean.class);
            } catch (Exception e) {
                summary.addSkip(sourceName, "采集规则JSON非法");
                continue;
            }
            if (StringUtils.isBlank(ruleBean.getBookDetailUrl())
                || StringUtils.isBlank(ruleBean.getContentPatten())
                || StringUtils.isBlank(ruleBean.getIndexIdPatten())) {
                summary.addSkip(sourceName, "采集规则缺少必填字段");
                continue;
            }
            //有发现分类可选的书源必须完成至少一个分类映射，否则导入后无法批量采集
            if (Boolean.TRUE.equals(item.getHasExploreOptions())
                && (ruleBean.getCatIdRule() == null || ruleBean.getCatIdRule().isEmpty())) {
                summary.addSkip(sourceName, "未选择分类映射");
                continue;
            }
            //同名批次内去重
            if (!batchNames.add(sourceName)) {
                summary.addSkip(sourceName, "本次导入内名称重复");
                continue;
            }
            //与库内已有书源名去重
            long exists = crawlSourceMapper.count(c -> c
                .where(CrawlSourceDynamicSqlSupport.sourceName, isEqualTo(sourceName)));
            if (exists > 0) {
                summary.addSkip(sourceName, "已存在同名爬虫源");
                continue;
            }
            CrawlSource crawlSource = new CrawlSource();
            crawlSource.setSourceName(sourceName);
            //规则来源按导入类型记录（如阅读3.0），区别于手工创建的默认来源
            crawlSource.setSourceFrom(sourceDisplayName(item.getSourceType()));
            crawlSource.setCrawlRule(item.getCrawlRule());
            //默认停用，由用户在列表页手动开启
            crawlSource.setSourceStatus((byte) 0);
            crawlSource.setCreateTime(now);
            crawlSource.setUpdateTime(now);
            crawlSourceMapper.insertSelective(crawlSource);
            summary.addSuccess(sourceName);
        }
        return summary;
    }

    /**
     * 规则来源展示名，入库写入source_from列；新类型接入时在此补充对应名称，未知类型暂记类型码
     */
    private String sourceDisplayName(String sourceType) {
        String type = StringUtils.defaultIfBlank(sourceType, SOURCE_TYPE_YUEDU3);
        if (SOURCE_TYPE_YUEDU3.equals(type)) {
            return "阅读3.0";
        }
        return type;
    }

    /**
     * 解析阅读3.0书源JSON，兼容数组与单对象两种顶层结构
     */
    private List<BookSourceBean> parseSources(String bookSourceJson) {
        String trimmed = bookSourceJson.trim();
        try {
            if (trimmed.startsWith("[")) {
                return OBJECT_MAPPER.readValue(trimmed,
                    OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, BookSourceBean.class));
            }
            List<BookSourceBean> list = new ArrayList<>(1);
            list.add(OBJECT_MAPPER.readValue(trimmed, BookSourceBean.class));
            return list;
        } catch (Exception e) {
            log.error("书源JSON解析失败", e);
            throw new BusinessException(ResponseStatus.BOOK_SOURCE_PARSE_FAIL);
        }
    }

    private BookSourceImportPreviewVO toPreviewVO(BookSourceConvertResult result) {
        BookSourceImportPreviewVO vo = new BookSourceImportPreviewVO();
        vo.setSourceName(result.getSourceName());
        vo.setSourceUrl(result.getSourceUrl());
        vo.setStatus(result.getStatus().name());
        vo.setImportable(result.isImportable());
        vo.setWarnings(result.getWarnings());
        vo.setErrors(result.getErrors());
        vo.setExploreOptions(result.getExploreOptions());
        vo.setCrawlRule(toRuleJson(result.getRuleBean()));
        return vo;
    }

    /**
     * 转换后规则Bean序列化为入库用的crawl_rule JSON（与现有规则格式一致，忽略空字段）
     */
    @SneakyThrows
    private String toRuleJson(RuleBean ruleBean) {
        if (ruleBean == null) {
            return null;
        }
        return OBJECT_MAPPER.writeValueAsString(ruleBean);
    }

    /**
     * 导入结果汇总
     */
    public static class ImportSummary {

        private final List<String> successNames = new ArrayList<>();

        private final List<SkipItem> skipItems = new ArrayList<>();

        public void addSuccess(String sourceName) {
            successNames.add(sourceName);
        }

        public void addSkip(String sourceName, String reason) {
            skipItems.add(new SkipItem(sourceName, reason));
        }

        public List<String> getSuccessNames() {
            return successNames;
        }

        public List<SkipItem> getSkipItems() {
            return skipItems;
        }

        public int getSuccessCount() {
            return successNames.size();
        }
    }

    /**
     * 跳过项
     */
    public static class SkipItem {

        private final String sourceName;

        private final String reason;

        public SkipItem(String sourceName, String reason) {
            this.sourceName = sourceName;
            this.reason = reason;
        }

        public String getSourceName() {
            return sourceName;
        }

        public String getReason() {
            return reason;
        }
    }

}

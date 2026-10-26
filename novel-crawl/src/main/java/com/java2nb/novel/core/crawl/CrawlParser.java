package com.java2nb.novel.core.crawl;

import com.java2nb.novel.core.utils.RandomBookInfoUtil;
import com.java2nb.novel.core.utils.StringUtil;
import com.java2nb.novel.core.utils.UrlUtil;
import com.java2nb.novel.entity.Book;
import com.java2nb.novel.entity.BookContent;
import com.java2nb.novel.entity.BookIndex;
import com.java2nb.novel.entity.CrawlSingleTask;
import com.java2nb.novel.utils.Constants;
import com.java2nb.novel.utils.CrawlHttpClient;
import io.github.xxyopen.util.IdWorker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 爬虫解析器
 *
 * @author Administrator
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CrawlParser {

    private final IdWorker ID_WORKER = IdWorker.INSTANCE;

    private final CrawlHttpClient crawlHttpClient;

    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 爬虫源采集章节数量缓存key
     */
    private static final String CRAWL_SOURCE_CHAPTER_COUNT_CACHE_KEY = "crawlSource:chapterCount:";

    /**
     * 爬虫任务进度
     */
    private final Map<Long, Integer> crawlTaskProgress = new HashMap<>();

    /**
     * 获取爬虫任务进度
     */
    public Integer getCrawlTaskProgress(Long taskId) {
        return crawlTaskProgress.get(taskId);
    }

    /**
     * 移除爬虫任务进度
     */
    public void removeCrawlTaskProgress(Long taskId) {
        crawlTaskProgress.remove(taskId);
    }

    /**
     * 获取爬虫源采集的章节数量
     */
    public Long getCrawlSourceChapterCount(Integer sourceId) {
        return Optional.ofNullable(
            stringRedisTemplate.opsForValue().get(CRAWL_SOURCE_CHAPTER_COUNT_CACHE_KEY + sourceId)).map(v -> {
            try {
                return Long.parseLong(v);
            } catch (NumberFormatException e) {
                return 0L;
            }
        }).orElse(0L);
    }

    public void parseBook(RuleBean ruleBean, String bookId, CrawlBookHandler handler)
        throws InterruptedException {
        Book book = new Book();
        String bookDetailUrl = resolveUrl(ruleBean, ruleBean.getBookDetailUrl().replace("{bookId}", bookId));
        String bookDetailHtml = crawlHttpClient.get(bookDetailUrl, ruleBean.getCharset());
        if (bookDetailHtml != null) {
            RuleExtractor extractor = RuleExtractor.create(ruleBean.getParseType(), bookDetailHtml);
            String bookName = extractor.extractFirst(ruleBean.getBookNamePatten());
            boolean isFindBookName = bookName != null;
            if (isFindBookName) {
                //设置小说名
                book.setBookName(bookName);
                String authorName = extractor.extractFirst(ruleBean.getAuthorNamePatten());
                boolean isFindAuthorName = authorName != null;
                if (isFindAuthorName) {
                    //设置作者名
                    book.setAuthorName(authorName);
                    if (StringUtils.isNotBlank(ruleBean.getPicUrlPatten())) {
                        String picUrl = extractor.extractFirst(ruleBean.getPicUrlPatten());
                        boolean isFindPicUrl = picUrl != null;
                        if (isFindPicUrl) {
                            if (StringUtils.isNotBlank(picUrl) && StringUtils.isNotBlank(ruleBean.getPicUrlPrefix())) {
                                picUrl = ruleBean.getPicUrlPrefix() + picUrl;
                            } else {
                                picUrl = resolveUrl(ruleBean, picUrl);
                            }
                            //设置封面图片路径
                            book.setPicUrl(picUrl);
                        }
                    }
                    if (StringUtils.isNotBlank(ruleBean.getScorePatten())) {
                        String score = extractor.extractFirst(ruleBean.getScorePatten());
                        if (score != null) {
                            try {
                                //设置评分
                                book.setScore(Float.parseFloat(score.trim()));
                            } catch (NumberFormatException e) {
                                log.error("解析小说评分出错：{}", score);
                            }
                        }
                    }
                    if (StringUtils.isNotBlank(ruleBean.getVisitCountPatten())) {
                        String visitCount = extractor.extractFirst(ruleBean.getVisitCountPatten());
                        if (visitCount != null) {
                            try {
                                //设置访问次数
                                book.setVisitCount(Long.parseLong(visitCount.trim()));
                            } catch (NumberFormatException e) {
                                log.error("解析小说访问次数出错：{}", visitCount);
                            }
                        }
                    }

                    //提取简介：优先使用descPatten规则，否则按起止标记截取（兼容老规则）
                    String desc = null;
                    if (StringUtils.isNotBlank(ruleBean.getDescPatten())) {
                        desc = extractor.extractFirst(ruleBean.getDescPatten());
                    } else if (StringUtils.isNotBlank(ruleBean.getDescStart()) && StringUtils.isNotBlank(
                        ruleBean.getDescEnd()) && bookDetailHtml.contains(ruleBean.getDescStart()) && bookDetailHtml
                        .contains(ruleBean.getDescEnd())) {
                        desc = bookDetailHtml.substring(
                            bookDetailHtml.indexOf(ruleBean.getDescStart()) + ruleBean.getDescStart().length());
                        desc = desc.substring(0, desc.indexOf(ruleBean.getDescEnd()));
                    }
                    if (desc != null) {
                        //过滤掉简介中的特殊标签
                        desc = desc.replaceAll("<a[^<]+</a>", "")
                            .replaceAll("<font[^<]+</font>", "")
                            .replaceAll("<p>\\s*</p>", "")
                            .replaceAll("<p>", "")
                            .replaceAll("</p>", "<br/>");
                        // 小说简介过滤
                        String filterDesc = ruleBean.getFilterDesc();
                        if (StringUtils.isNotBlank(filterDesc)) {
                            String[] filterRules = filterDesc.replace("\r\n", "\n").split("\n");
                            for (String filterRule : filterRules) {
                                if (StringUtils.isNotBlank(filterRule)) {
                                    desc = desc.replaceAll(filterRule, "");
                                }
                            }
                        }
                        // 去除小说简介前后空格
                        desc = desc.trim();
                        // 去除小说简介末尾冗余的小说名
                        if (desc.endsWith(bookName)) {
                            desc = desc.substring(0, desc.length() - bookName.length());
                        }
                        //设置书籍简介
                        book.setBookDesc(desc);
                    }
                    if (StringUtils.isNotBlank(ruleBean.getStatusPatten())) {
                        String bookStatus = extractor.extractFirst(ruleBean.getStatusPatten());
                        if (bookStatus != null && ruleBean.getBookStatusRule().get(bookStatus) != null) {
                            //设置更新状态
                            book.setBookStatus(ruleBean.getBookStatusRule().get(bookStatus));
                        }
                    }

                    if (StringUtils.isNotBlank(ruleBean.getUpadateTimePatten()) && StringUtils.isNotBlank(
                        ruleBean.getUpadateTimeFormatPatten())) {
                        String updateTime = extractor.extractFirst(ruleBean.getUpadateTimePatten());
                        if (updateTime != null) {
                            //设置更新时间
                            try {
                                book.setLastIndexUpdateTime(
                                    new SimpleDateFormat(ruleBean.getUpadateTimeFormatPatten()).parse(updateTime));
                            } catch (ParseException e) {
                                log.error("解析最新章节更新时间出错", e);
                            }

                        }
                    }

                }
                if (book.getVisitCount() == null && book.getScore() != null) {
                    //随机根据评分生成访问次数
                    book.setVisitCount(RandomBookInfoUtil.getVisitCountByScore(book.getScore()));
                } else if (book.getVisitCount() != null && book.getScore() == null) {
                    //随机根据访问次数生成评分
                    book.setScore(RandomBookInfoUtil.getScoreByVisitCount(book.getVisitCount()));
                } else if (book.getVisitCount() == null) {
                    //都没有，设置成固定值
                    book.setVisitCount(Constants.VISIT_COUNT_DEFAULT);
                    book.setScore(6.5f);
                }
            }
        }
        handler.handle(book);
    }

    public boolean parseBookIndexAndContent(String sourceBookId, Book book, RuleBean ruleBean, Integer sourceId,
        Map<Integer, BookIndex> existBookIndexMap, CrawlBookChapterHandler handler, CrawlSingleTask task)
        throws InterruptedException {

        if (task != null) {
            // 开始采集
            crawlTaskProgress.put(task.getId(), 0);
        }

        Date currentDate = new Date();

        List<BookIndex> indexList = new ArrayList<>();
        List<BookContent> contentList = new ArrayList<>();
        //读取目录（支持“下一页”分页，按页序汇总章节）
        List<String[]> indexPairs = collectIndexPairs(ruleBean, sourceBookId);

        if (!indexPairs.isEmpty()) {
            int matchCount = indexPairs.size();

            //总字数
            int totalWordCount = book.getWordCount() == null ? 0 : book.getWordCount();

            for (int indexNum = 0; indexNum < matchCount; indexNum++) {

                BookIndex hasIndex = existBookIndexMap.get(indexNum);
                String indexName = indexPairs.get(indexNum)[0];

                if (hasIndex == null || !StringUtils.deleteWhitespace(hasIndex.getIndexName())
                    .equals(StringUtils.deleteWhitespace(indexName))) {

                    String sourceIndexId = indexPairs.get(indexNum)[1];
                    String bookContentUrl = ruleBean.getBookContentUrl();
                    int calStart = bookContentUrl.indexOf("{cal_");
                    if (calStart != -1) {
                        //内容页URL需要进行计算才能得到
                        String calStr = bookContentUrl.substring(calStart,
                            calStart + bookContentUrl.substring(calStart).indexOf("}"));
                        String[] calArr = calStr.split("_");
                        int calType = Integer.parseInt(calArr[1]);
                        if (calType == 1) {
                            ///{cal_1_1_3}_{bookId}/{indexId}.html
                            //第一种计算规则，去除第x个参数的最后y个字母
                            int x = Integer.parseInt(calArr[2]);
                            int y = Integer.parseInt(calArr[3]);
                            String calResult;
                            if (x == 1) {
                                calResult = sourceBookId.substring(0, sourceBookId.length() - y);
                            } else {
                                calResult = sourceIndexId.substring(0, sourceBookId.length() - y);
                            }

                            if (calResult.isEmpty()) {
                                calResult = "0";

                            }

                            bookContentUrl = bookContentUrl.replace(calStr + "}", calResult);
                        }

                    }

                    String contentUrl = resolveUrl(ruleBean, bookContentUrl.replace("{bookId}", sourceBookId)
                        .replace("{indexId}", sourceIndexId));

                    //查询章节内容
                    String contentHtml = crawlHttpClient.get(contentUrl, ruleBean.getCharset());
                    String content = (contentHtml == null || contentHtml.contains("正在手打中"))
                        ? null : extractContent(ruleBean, contentHtml);
                    if (content != null && StringUtils.isNotBlank(ruleBean.getNextContentUrlPatten())) {
                        //正文分页拼接
                        content = mergeNextContentPages(ruleBean, contentUrl, contentHtml, content);
                    }
                    if (content != null) {
                        // 小说内容过滤
                        String filterContent = ruleBean.getFilterContent();
                        if (StringUtils.isNotBlank(filterContent)) {
                            String[] filterRules = filterContent.replace("\r\n", "\n").split("\n");
                            for (String filterRule : filterRules) {
                                if (StringUtils.isNotBlank(filterRule)) {
                                    content = content.replaceAll(filterRule, "");
                                }
                            }
                        }
                        // 去除小说内容末尾的所有换行
                        content = removeTrailingBrTags(content);
                        //插入章节目录和章节内容
                        BookIndex bookIndex = new BookIndex();
                        bookIndex.setIndexName(indexName);
                        bookIndex.setIndexNum(indexNum);
                        int wordCount = StringUtil.getStrValidWordCount(content);
                        bookIndex.setWordCount(wordCount);
                        indexList.add(bookIndex);

                        BookContent bookContent = new BookContent();
                        bookContent.setContent(content);
                        contentList.add(bookContent);

                        if (hasIndex != null) {
                            //章节更新
                            bookIndex.setId(hasIndex.getId());
                            bookContent.setIndexId(hasIndex.getId());

                            //计算总字数
                            totalWordCount = (totalWordCount + wordCount - hasIndex.getWordCount());
                        } else {
                            //章节插入
                            //设置目录和章节内容
                            Long indexId = ID_WORKER.nextId();
                            bookIndex.setId(indexId);
                            bookIndex.setBookId(book.getId());

                            bookIndex.setCreateTime(currentDate);

                            bookContent.setIndexId(indexId);

                            //计算总字数
                            totalWordCount += wordCount;
                        }
                        bookIndex.setUpdateTime(currentDate);

                        if (task != null) {
                            // 更新单本任务采集进度
                            crawlTaskProgress.put(task.getId(), indexList.size());
                        }

                        // 更新爬虫源采集章节数量
                        stringRedisTemplate.opsForValue().increment(CRAWL_SOURCE_CHAPTER_COUNT_CACHE_KEY + sourceId);

                    }


                }
            }

            if (!indexList.isEmpty()) {
                //如果有爬到最新章节，则设置小说主表的最新章节信息
                //获取爬取到的最新章节
                BookIndex lastIndex = indexList.getLast();
                book.setLastIndexId(lastIndex.getId());
                book.setLastIndexName(lastIndex.getIndexName());
                book.setLastIndexUpdateTime(currentDate);

            }
            book.setWordCount(totalWordCount);
            book.setUpdateTime(currentDate);

            if (indexList.size() == contentList.size() && !indexList.isEmpty()) {

                handler.handle(new ChapterBean() {{
                    setBookIndexList(indexList);
                    setBookContentList(contentList);
                }});

                return true;

            }

        }

        handler.handle(new ChapterBean() {{
            setBookIndexList(new ArrayList<>(0));
            setBookContentList(new ArrayList<>(0));
        }});
        return false;

    }

    /**
     * 读取章节目录，支持按nextIndexUrlPatten“下一页”规则翻页，
     * 返回按页序汇总的[章节名, 源章节ID]对；已访问页去重与页数上限防止链接成环
     */
    private List<String[]> collectIndexPairs(RuleBean ruleBean, String sourceBookId) throws InterruptedException {
        List<String[]> pairs = new ArrayList<>();
        String pageUrl = resolveUrl(ruleBean, ruleBean.getBookIndexUrl().replace("{bookId}", sourceBookId));
        Set<String> visited = new HashSet<>();
        int pageIndex = 0;
        while (StringUtils.isNotBlank(pageUrl) && pageIndex < Constants.MAX_INDEX_PAGE && visited.add(pageUrl)) {
            pageIndex++;
            String indexListHtml = crawlHttpClient.get(pageUrl, ruleBean.getCharset());
            if (indexListHtml == null) {
                break;
            }
            if (pageIndex == 1 && StringUtils.isNotBlank(ruleBean.getBookIndexStart()) && indexListHtml
                .contains(ruleBean.getBookIndexStart())) {
                indexListHtml = indexListHtml.substring(
                    indexListHtml.indexOf(ruleBean.getBookIndexStart()) + ruleBean.getBookIndexStart().length());
            }

            RuleExtractor indexExtractor = RuleExtractor.create(ruleBean.getParseType(), indexListHtml);
            List<String> indexIds = indexExtractor.extractAll(ruleBean.getIndexIdPatten());
            List<String> indexNames = indexExtractor.extractAll(ruleBean.getIndexNamePatten());
            int matchCount = Math.min(indexIds.size(), indexNames.size());
            for (int i = 0; i < matchCount; i++) {
                pairs.add(new String[]{indexNames.get(i), indexIds.get(i)});
            }

            pageUrl = StringUtils.isBlank(ruleBean.getNextIndexUrlPatten()) ? null
                : extractNextPageUrl(indexExtractor, ruleBean.getNextIndexUrlPatten(), pageUrl);
        }
        return pairs;
    }

    /**
     * 按“下一页”规则从当前页面提取下一页地址，以当前页URL为基准补全为绝对地址；
     * 伪链接（javascript:/#）或补全后仍非绝对地址时返回null
     */
    private String extractNextPageUrl(RuleExtractor extractor, String rule, String currentPageUrl) {
        String next = extractor.extractFirst(rule);
        if (StringUtils.isBlank(next)) {
            return null;
        }
        next = next.trim();
        if (next.startsWith("javascript") || next.startsWith("#") || "null".equals(next)) {
            return null;
        }
        String absolute = UrlUtil.absolutize(currentPageUrl, next);
        return UrlUtil.isAbsolute(absolute) ? absolute : null;
    }

    /**
     * 正文分页拼接：按nextContentUrlPatten逐页抓取“下一页”正文并按页序追加，
     * 直至无下一页、取不到正文或达到页数上限
     */
    private String mergeNextContentPages(RuleBean ruleBean, String firstPageUrl, String firstPageHtml,
        String firstPageContent) throws InterruptedException {
        StringBuilder merged = new StringBuilder(firstPageContent);
        String pageUrl = firstPageUrl;
        String pageHtml = firstPageHtml;
        Set<String> visited = new HashSet<>();
        visited.add(firstPageUrl);
        int pageCount = 1;
        while (pageCount < Constants.MAX_CONTENT_PAGE) {
            String nextUrl = extractNextPageUrl(
                RuleExtractor.create(ruleBean.getParseType(), pageHtml), ruleBean.getNextContentUrlPatten(), pageUrl);
            if (nextUrl == null || !visited.add(nextUrl)) {
                break;
            }
            String nextHtml = crawlHttpClient.get(nextUrl, ruleBean.getCharset());
            if (nextHtml == null || nextHtml.contains("正在手打中")) {
                break;
            }
            String nextContent = extractContent(ruleBean, nextHtml);
            if (StringUtils.isBlank(nextContent)) {
                break;
            }
            merged.append("\n").append(nextContent);
            pageUrl = nextUrl;
            pageHtml = nextHtml;
            pageCount++;
        }
        return merged.toString();
    }

    /**
     * 拼接后的URL不是绝对地址时，用规则的urlPrefix（源站origin）补全，urlPrefix为空时原样返回兼容老规则
     */
    private String resolveUrl(RuleBean ruleBean, String url) {
        if (StringUtils.isBlank(ruleBean.getUrlPrefix())) {
            return url;
        }
        return UrlUtil.absolutize(ruleBean.getUrlPrefix(), url);
    }

    /**
     * 提取章节正文，优先使用contentPatten规则，否则按contentStart/contentEnd标记截取（兼容老的正则规则）
     */
    private String extractContent(RuleBean ruleBean, String contentHtml) {
        if (StringUtils.isNotBlank(ruleBean.getContentPatten())) {
            //正文规则可能按段落逐段匹配（如 .content p::text），需要把所有匹配段拼接起来
            List<String> parts = RuleExtractor.create(ruleBean.getParseType(), contentHtml)
                .extractAll(ruleBean.getContentPatten());
            return parts.isEmpty() ? null : String.join("\n", parts);
        }
        String contentStart = ruleBean.getContentStart();
        String contentEnd = ruleBean.getContentEnd();
        if (StringUtils.isBlank(contentStart) || StringUtils.isBlank(contentEnd)
            || !contentHtml.contains(contentStart)) {
            log.error("章节正文未匹配到起止标记：{} / {}", contentStart, contentEnd);
            return null;
        }
        String content = contentHtml.substring(contentHtml.indexOf(contentStart) + contentStart.length());
        int endIndex = content.indexOf(contentEnd);
        if (endIndex < 0) {
            log.error("章节正文未匹配到结束标记：{}", contentEnd);
            return null;
        }
        return content.substring(0, endIndex);
    }

    /**
     * 删除字符串末尾的所有 <br> 类似标签（允许各种空格）
     */
    public static String removeTrailingBrTags(String str) {
        return str.replaceAll("(?i)(?:\\s*<\\s*br\\s*/?\\s*>)++(?:\\s|\\u3000)*$", "");
    }

}

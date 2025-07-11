package com.java2nb.novel.core.crawl;

import com.java2nb.novel.core.utils.RandomBookInfoUtil;
import com.java2nb.novel.core.utils.StringUtil;
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
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public void parseBook(RuleBean ruleBean, String bookId, CrawlBookHandler handler)
        throws InterruptedException {
        Book book = new Book();
        String bookDetailUrl = ruleBean.getBookDetailUrl().replace("{bookId}", bookId);
        String bookDetailHtml = crawlHttpClient.get(bookDetailUrl, ruleBean.getCharset());
        if (bookDetailHtml != null) {
            Pattern bookNamePatten = PatternFactory.getPattern(ruleBean.getBookNamePatten());
            Matcher bookNameMatch = bookNamePatten.matcher(bookDetailHtml);
            boolean isFindBookName = bookNameMatch.find();
            if (isFindBookName) {
                String bookName = bookNameMatch.group(1);
                //设置小说名
                book.setBookName(bookName);
                Pattern authorNamePatten = PatternFactory.getPattern(ruleBean.getAuthorNamePatten());
                Matcher authorNameMatch = authorNamePatten.matcher(bookDetailHtml);
                boolean isFindAuthorName = authorNameMatch.find();
                if (isFindAuthorName) {
                    String authorName = authorNameMatch.group(1);
                    //设置作者名
                    book.setAuthorName(authorName);
                    if (StringUtils.isNotBlank(ruleBean.getPicUrlPatten())) {
                        Pattern picUrlPatten = PatternFactory.getPattern(ruleBean.getPicUrlPatten());
                        Matcher picUrlMatch = picUrlPatten.matcher(bookDetailHtml);
                        boolean isFindPicUrl = picUrlMatch.find();
                        if (isFindPicUrl) {
                            String picUrl = picUrlMatch.group(1);
                            if (StringUtils.isNotBlank(picUrl) && StringUtils.isNotBlank(ruleBean.getPicUrlPrefix())) {
                                picUrl = ruleBean.getPicUrlPrefix() + picUrl;
                            }
                            //设置封面图片路径
                            book.setPicUrl(picUrl);
                        }
                    }
                    if (StringUtils.isNotBlank(ruleBean.getScorePatten())) {
                        Pattern scorePatten = PatternFactory.getPattern(ruleBean.getScorePatten());
                        Matcher scoreMatch = scorePatten.matcher(bookDetailHtml);
                        boolean isFindScore = scoreMatch.find();
                        if (isFindScore) {
                            String score = scoreMatch.group(1);
                            //设置评分
                            book.setScore(Float.parseFloat(score));
                        }
                    }
                    if (StringUtils.isNotBlank(ruleBean.getVisitCountPatten())) {
                        Pattern visitCountPatten = PatternFactory.getPattern(ruleBean.getVisitCountPatten());
                        Matcher visitCountMatch = visitCountPatten.matcher(bookDetailHtml);
                        boolean isFindVisitCount = visitCountMatch.find();
                        if (isFindVisitCount) {
                            String visitCount = visitCountMatch.group(1);
                            //设置访问次数
                            book.setVisitCount(Long.parseLong(visitCount));
                        }
                    }

                    String desc = bookDetailHtml.substring(
                        bookDetailHtml.indexOf(ruleBean.getDescStart()) + ruleBean.getDescStart().length());
                    desc = desc.substring(0, desc.indexOf(ruleBean.getDescEnd()));
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
                    if (StringUtils.isNotBlank(ruleBean.getStatusPatten())) {
                        Pattern bookStatusPatten = PatternFactory.getPattern(ruleBean.getStatusPatten());
                        Matcher bookStatusMatch = bookStatusPatten.matcher(bookDetailHtml);
                        boolean isFindBookStatus = bookStatusMatch.find();
                        if (isFindBookStatus) {
                            String bookStatus = bookStatusMatch.group(1);
                            if (ruleBean.getBookStatusRule().get(bookStatus) != null) {
                                //设置更新状态
                                book.setBookStatus(ruleBean.getBookStatusRule().get(bookStatus));
                            }
                        }
                    }

                    if (StringUtils.isNotBlank(ruleBean.getUpadateTimePatten()) && StringUtils.isNotBlank(
                        ruleBean.getUpadateTimeFormatPatten())) {
                        Pattern updateTimePatten = PatternFactory.getPattern(ruleBean.getUpadateTimePatten());
                        Matcher updateTimeMatch = updateTimePatten.matcher(bookDetailHtml);
                        boolean isFindUpdateTime = updateTimeMatch.find();
                        if (isFindUpdateTime) {
                            String updateTime = updateTimeMatch.group(1);
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

    public boolean parseBookIndexAndContent(String sourceBookId, Book book, RuleBean ruleBean,
        Map<Integer, BookIndex> existBookIndexMap, CrawlBookChapterHandler handler, CrawlSingleTask task)
        throws InterruptedException {

        if (task != null) {
            // 开始采集
            crawlTaskProgress.put(task.getId(), 0);
        }

        Date currentDate = new Date();

        List<BookIndex> indexList = new ArrayList<>();
        List<BookContent> contentList = new ArrayList<>();
        //读取目录
        String indexListUrl = ruleBean.getBookIndexUrl().replace("{bookId}", sourceBookId);
        String indexListHtml = crawlHttpClient.get(indexListUrl, ruleBean.getCharset());

        if (indexListHtml != null) {
            if (StringUtils.isNotBlank(ruleBean.getBookIndexStart())) {
                indexListHtml = indexListHtml.substring(
                    indexListHtml.indexOf(ruleBean.getBookIndexStart()) + ruleBean.getBookIndexStart().length());
            }

            Pattern indexIdPatten = PatternFactory.getPattern(ruleBean.getIndexIdPatten());
            Matcher indexIdMatch = indexIdPatten.matcher(indexListHtml);

            Pattern indexNamePatten = PatternFactory.getPattern(ruleBean.getIndexNamePatten());
            Matcher indexNameMatch = indexNamePatten.matcher(indexListHtml);

            boolean isFindIndex = indexIdMatch.find() & indexNameMatch.find();

            int indexNum = 0;

            //总字数
            int totalWordCount = book.getWordCount() == null ? 0 : book.getWordCount();

            while (isFindIndex) {

                BookIndex hasIndex = existBookIndexMap.get(indexNum);
                String indexName = indexNameMatch.group(1);

                if (hasIndex == null || !StringUtils.deleteWhitespace(hasIndex.getIndexName())
                    .equals(StringUtils.deleteWhitespace(indexName))) {

                    String sourceIndexId = indexIdMatch.group(1);
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

                    String contentUrl = bookContentUrl.replace("{bookId}", sourceBookId)
                        .replace("{indexId}", sourceIndexId);

                    //查询章节内容
                    String contentHtml = crawlHttpClient.get(contentUrl, ruleBean.getCharset());
                    if (contentHtml != null && !contentHtml.contains("正在手打中")) {
                        String content = contentHtml.substring(
                            contentHtml.indexOf(ruleBean.getContentStart()) + ruleBean.getContentStart().length());
                        content = content.substring(0, content.indexOf(ruleBean.getContentEnd()));
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
                            // 更新采集进度
                            crawlTaskProgress.put(task.getId(), indexList.size());
                        }


                    }


                }
                indexNum++;
                isFindIndex = indexIdMatch.find() & indexNameMatch.find();
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
     * 删除字符串末尾的所有 <br> 类似标签（允许各种空格）
     */
    public static String removeTrailingBrTags(String str) {
        return str.replaceAll("(?i)(?:\\s*<\\s*br\\s*/?\\s*>)++(?:\\s|\\u3000)*$", "");
    }

}

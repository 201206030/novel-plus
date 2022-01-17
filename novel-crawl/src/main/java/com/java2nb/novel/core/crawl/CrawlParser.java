package com.java2nb.novel.core.crawl;

import com.java2nb.novel.core.utils.HttpUtil;
import com.java2nb.novel.core.utils.RandomBookInfoUtil;
import com.java2nb.novel.core.utils.RestTemplateUtil;
import com.java2nb.novel.core.utils.StringUtil;
import com.java2nb.novel.entity.Book;
import com.java2nb.novel.entity.BookContent;
import com.java2nb.novel.entity.BookIndex;
import com.java2nb.novel.utils.Constants;
import io.github.xxyopen.util.IdWorker;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

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
public class CrawlParser {

    private static final IdWorker idWorker = IdWorker.INSTANCE;

    private static final RestTemplate restTemplate = RestTemplateUtil.getInstance("utf-8");

    private static final ThreadLocal<Integer> retryCount = new ThreadLocal<>();

    @SneakyThrows
    public static void parseBook(RuleBean ruleBean, String bookId, CrawlBookHandler handler) {
        Book book = new Book();
        String bookDetailUrl = ruleBean.getBookDetailUrl().replace("{bookId}", bookId);
        String bookDetailHtml = getByHttpClientWithChrome(bookDetailUrl);
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

                    String desc = bookDetailHtml.substring(bookDetailHtml.indexOf(ruleBean.getDescStart()) + ruleBean.getDescStart().length());
                    desc = desc.substring(0, desc.indexOf(ruleBean.getDescEnd()));
                    //过滤掉简介中的特殊标签
                    desc = desc.replaceAll("<a[^<]+</a>", "")
                            .replaceAll("<font[^<]+</font>", "")
                            .replaceAll("<p>\\s*</p>", "")
                            .replaceAll("<p>", "")
                            .replaceAll("</p>", "<br/>");
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

                    if (StringUtils.isNotBlank(ruleBean.getUpadateTimePatten()) && StringUtils.isNotBlank(ruleBean.getUpadateTimeFormatPatten())) {
                        Pattern updateTimePatten = PatternFactory.getPattern(ruleBean.getUpadateTimePatten());
                        Matcher updateTimeMatch = updateTimePatten.matcher(bookDetailHtml);
                        boolean isFindUpdateTime = updateTimeMatch.find();
                        if (isFindUpdateTime) {
                            String updateTime = updateTimeMatch.group(1);
                            //设置更新时间
                            book.setLastIndexUpdateTime(new SimpleDateFormat(ruleBean.getUpadateTimeFormatPatten()).parse(updateTime));

                        }
                    }

                }
                if (book.getVisitCount() == null && book.getScore() != null) {
                    //随机根据评分生成访问次数
                    book.setVisitCount(RandomBookInfoUtil.getVisitCountByScore(book.getScore()));
                } else if (book.getVisitCount() != null && book.getScore() == null) {
                    //随机根据访问次数生成评分
                    book.setScore(RandomBookInfoUtil.getScoreByVisitCount(book.getVisitCount()));
                } else if (book.getVisitCount() == null && book.getScore() == null) {
                    //都没有，设置成固定值
                    book.setVisitCount(Constants.VISIT_COUNT_DEFAULT);
                    book.setScore(6.5f);
                }
            }
        }
        handler.handle(book);
    }

    public static boolean parseBookIndexAndContent(String sourceBookId, Book book, RuleBean ruleBean, Map<Integer, BookIndex> existBookIndexMap, CrawlBookChapterHandler handler) {

        Date currentDate = new Date();

        List<BookIndex> indexList = new ArrayList<>();
        List<BookContent> contentList = new ArrayList<>();
        //读取目录
        String indexListUrl = ruleBean.getBookIndexUrl().replace("{bookId}", sourceBookId);
        String indexListHtml = getByHttpClientWithChrome(indexListUrl);

        if (indexListHtml != null) {
            if (StringUtils.isNotBlank(ruleBean.getBookIndexStart())) {
                indexListHtml = indexListHtml.substring(indexListHtml.indexOf(ruleBean.getBookIndexStart()) + ruleBean.getBookIndexStart().length());
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

                if (hasIndex == null || !StringUtils.deleteWhitespace(hasIndex.getIndexName()).equals(StringUtils.deleteWhitespace(indexName))) {

                    String sourceIndexId = indexIdMatch.group(1);
                    String bookContentUrl = ruleBean.getBookContentUrl();
                    int calStart = bookContentUrl.indexOf("{cal_");
                    if (calStart != -1) {
                        //内容页URL需要进行计算才能得到
                        String calStr = bookContentUrl.substring(calStart, calStart + bookContentUrl.substring(calStart).indexOf("}"));
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

                            if (calResult.length() == 0) {
                                calResult = "0";

                            }

                            bookContentUrl = bookContentUrl.replace(calStr + "}", calResult);
                        }

                    }

                    String contentUrl = bookContentUrl.replace("{bookId}", sourceBookId).replace("{indexId}", sourceIndexId);

                    //查询章节内容
                    String contentHtml = getByHttpClientWithChrome(contentUrl);
                    if (contentHtml != null && !contentHtml.contains("正在手打中")) {
                        String content = contentHtml.substring(contentHtml.indexOf(ruleBean.getContentStart()) + ruleBean.getContentStart().length());
                        content = content.substring(0, content.indexOf(ruleBean.getContentEnd()));
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
                            Long indexId = idWorker.nextId();
                            bookIndex.setId(indexId);
                            bookIndex.setBookId(book.getId());

                            bookIndex.setCreateTime(currentDate);

                            bookContent.setIndexId(indexId);

                            //计算总字数
                            totalWordCount += wordCount;
                        }
                        bookIndex.setUpdateTime(currentDate);


                    }


                }
                indexNum++;
                isFindIndex = indexIdMatch.find() & indexNameMatch.find();
            }


            if (indexList.size() > 0) {
                //如果有爬到最新章节，则设置小说主表的最新章节信息
                //获取爬取到的最新章节
                BookIndex lastIndex = indexList.get(indexList.size() - 1);
                book.setLastIndexId(lastIndex.getId());
                book.setLastIndexName(lastIndex.getIndexName());
                book.setLastIndexUpdateTime(currentDate);

            }
            book.setWordCount(totalWordCount);
            book.setUpdateTime(currentDate);

            if (indexList.size() == contentList.size() && indexList.size() > 0) {

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


    private static String getByHttpClient(String url) {
        try {
            ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class);
            if (forEntity.getStatusCode() == HttpStatus.OK) {
                String body = forEntity.getBody();
                assert body != null;
                if (body.length() < Constants.INVALID_HTML_LENGTH) {
                    return processErrorHttpResult(url);
                }
                //成功获得html内容
                return body;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return processErrorHttpResult(url);

    }

    private static String getByHttpClientWithChrome(String url) {
        try {

            String body = HttpUtil.getByHttpClientWithChrome(url);
            if (body != null && body.length() < Constants.INVALID_HTML_LENGTH) {
                return processErrorHttpResult(url);
            }
            //成功获得html内容
            return body;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return processErrorHttpResult(url);

    }

    @SneakyThrows
    private static String processErrorHttpResult(String url) {
        Integer count = retryCount.get();
        if (count == null) {
            count = 0;
        }
        if (count < Constants.HTTP_FAIL_RETRY_COUNT) {
            Thread.sleep(new Random().nextInt(10 * 1000));
            retryCount.set(++count);
            return getByHttpClient(url);
        }
        return null;
    }


}

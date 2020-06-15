package com.java2nb.novel.core.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java2nb.novel.core.crawl.CrawlParser;
import com.java2nb.novel.core.crawl.RuleBean;
import com.java2nb.novel.entity.*;
import com.java2nb.novel.service.BookService;
import com.java2nb.novel.service.CrawlService;
import com.java2nb.novel.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
@WebListener
@Slf4j
@RequiredArgsConstructor
public class StarterListener implements ServletContextListener {

    private final BookService bookService;

    private final CrawlService crawlService;

    @Value("${crawl.update.thread}")
    private int updateThreadCount;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        for (int i = 0; i < updateThreadCount; i++) {
            new Thread(() -> {
                log.info("程序启动,开始执行自动更新线程。。。");
                while (true) {
                    try {
                        //1.查询最新目录更新时间在一个月之内的前100条需要更新的数据
                        Date currentDate = new Date();
                        Date startDate = DateUtils.addDays(currentDate, -30);
                        List<Book> bookList;
                        synchronized (this) {
                            bookList = bookService.queryNeedUpdateBook(startDate, 100);
                        }
                        for (Book needUpdateBook : bookList) {
                            try {
                                //查询爬虫源规则
                                CrawlSource source = crawlService.queryCrawlSource(needUpdateBook.getCrawlSourceId());
                                RuleBean ruleBean = new ObjectMapper().readValue(source.getCrawlRule(), RuleBean.class);
                                //解析小说基本信息
                                Book book = CrawlParser.parseBook(ruleBean, needUpdateBook.getCrawlBookId());
                                //这里只做老书更新
                                book.setId(needUpdateBook.getId());
                                if (needUpdateBook.getPicUrl() != null && needUpdateBook.getPicUrl().contains(Constants.LOCAL_PIC_PREFIX)) {
                                    //本地图片则不更新
                                    book.setPicUrl(null);
                                }
                                //查询已存在的章节
                                Map<Integer, BookIndex> existBookIndexMap = bookService.queryExistBookIndexMap(needUpdateBook.getId());
                                //解析章节目录
                                Map<Integer, List> indexAndContentList = CrawlParser.parseBookIndexAndContent(needUpdateBook.getCrawlBookId(), book, ruleBean, existBookIndexMap);
                                bookService.updateBookAndIndexAndContent(book, (List<BookIndex>) indexAndContentList.get(CrawlParser.BOOK_INDEX_LIST_KEY), (List<BookContent>) indexAndContentList.get(CrawlParser.BOOK_CONTENT_LIST_KEY), existBookIndexMap);
                            } catch (Exception e) {
                                log.error(e.getMessage(), e);
                            }

                        }

                        Thread.sleep(1000 * 60 * 10);
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }

                }
            }).start();


        }


        new Thread(() -> {
            log.info("程序启动,开始执行单本采集任务线程。。。");
            while (true) {
                CrawlSingleTask task = null;
                byte crawlStatus = 0;
                try {
                    //获取采集任务
                    task = crawlService.getCrawlSingleTask();

                    if (task != null) {
                        //查询爬虫规则
                        CrawlSource source = crawlService.queryCrawlSource(task.getSourceId());
                        RuleBean ruleBean = new ObjectMapper().readValue(source.getCrawlRule(), RuleBean.class);

                        if (crawlService.parseBookAndSave(task.getCatId(), ruleBean, task.getSourceId(), task.getSourceBookId())) {
                            //采集成功
                            crawlStatus = 1;
                        }

                    }

                    Thread.sleep(1000 * 60);

                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
                if (task != null) {
                    crawlService.updateCrawlSingleTask(task, crawlStatus);
                }

            }
        }).start();
    }
}

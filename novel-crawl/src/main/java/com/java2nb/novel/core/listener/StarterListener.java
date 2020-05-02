package com.java2nb.novel.core.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java2nb.novel.core.crawl.CrawlParser;
import com.java2nb.novel.core.crawl.RuleBean;
import com.java2nb.novel.entity.Book;
import com.java2nb.novel.entity.BookContent;
import com.java2nb.novel.entity.BookIndex;
import com.java2nb.novel.entity.CrawlSource;
import com.java2nb.novel.service.BookService;
import com.java2nb.novel.service.CrawlService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;

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

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("程序启动,开始执行自动更新线程。。。");
        new Thread(() -> {
            while (true) {
                try {
                    //1.查询最新目录更新时间在一个月之内的前100条需要更新的数据
                    Date currentDate = new Date();
                    Date startDate = DateUtils.addDays(currentDate, -30);
                    List<Book> bookList = bookService.queryNeedUpdateBook(startDate, 100);
                    for (Book needUpdateBook : bookList) {
                        try {
                            //查询爬虫源规则
                            CrawlSource source = crawlService.queryCrawlSource(needUpdateBook.getCrawlSourceId());
                            RuleBean ruleBean = new ObjectMapper().readValue(source.getCrawlRule(), RuleBean.class);
                            //解析小说基本信息
                            Book book = CrawlParser.parseBook(ruleBean, needUpdateBook.getCrawlBookId());
                            //这里只做老书更新
                            book.setCrawlLastTime(currentDate);
                            book.setId(needUpdateBook.getId());
                            book.setPicUrl(needUpdateBook.getPicUrl());
                            //查询已存在的章节
                            Map<Integer, BookIndex> existBookIndexMap = bookService.queryExistBookIndexMap(needUpdateBook.getId());
                            //解析章节目录
                            Map<Integer, List> indexAndContentList = CrawlParser.parseBookIndexAndContent(needUpdateBook.getCrawlBookId(),book, ruleBean, existBookIndexMap);
                            bookService.updateBookAndIndexAndContent(book, (List<BookIndex>) indexAndContentList.get(CrawlParser.BOOK_INDEX_LIST_KEY), (List<BookContent>) indexAndContentList.get(CrawlParser.BOOK_CONTENT_LIST_KEY));
                        }catch (Exception e){
                            log.error(e.getMessage(), e);
                            //解析异常中断，更新一下小说的最后解析时间
                            bookService.updateCrawlLastTime(needUpdateBook.getId());
                        }

                    }

                    Thread.sleep(1000 * 60 * 10);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }

            }
        }).start();
    }
}

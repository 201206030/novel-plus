package com.java2nb.novel.core.crawl;

import com.java2nb.novel.entity.Book;

/**
 * 爬虫小说处理器
 * */
public interface CrawlBookHandler {

    void handle(Book book);

}

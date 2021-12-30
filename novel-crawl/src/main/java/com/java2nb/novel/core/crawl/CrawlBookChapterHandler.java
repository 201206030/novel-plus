package com.java2nb.novel.core.crawl;

import com.java2nb.novel.entity.Book;

/**
 * 爬虫小说章节内容处理器
 * */
public interface CrawlBookChapterHandler {

    void handle(ChapterBean chapterBean);

}

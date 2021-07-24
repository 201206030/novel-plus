package com.java2nb.novel.core.crawl;

import com.java2nb.novel.entity.BookContent;
import com.java2nb.novel.entity.BookIndex;
import lombok.Data;

import java.util.List;

/**
 * 章节数据封装bean
 * @author Administrator
 */
@Data
public class ChapterBean {

    /**
     * 章节索引集合
     * */
    List<BookIndex> bookIndexList;

    /**
     * 章节内容集合
     * */
    List<BookContent> bookContentList;
}

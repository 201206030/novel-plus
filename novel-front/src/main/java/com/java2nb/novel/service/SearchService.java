package com.java2nb.novel.service;


import com.github.pagehelper.PageInfo;
import com.java2nb.novel.entity.Book;
import com.java2nb.novel.search.BookSP;

/**
 * @author 11797
 */
public interface SearchService {

    /**
     * 导入到es
     * @param book 小说数据
     */
    void importToEs(Book book);

    PageInfo searchBook(BookSP params, int page, int pageSize);
}

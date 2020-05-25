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

    /**
     * 搜索
     * @param params 搜索参数
     * @param page 当前页码
     * @param pageSize 每页大小
     * @return 分页信息
     */
    PageInfo searchBook(BookSP params, int page, int pageSize);
}

package com.java2nb.novel.service;

import com.java2nb.novel.vo.BookListVO;
import io.github.xxyopen.model.page.PageBean;

/**
 * 用户书单服务
 *
 * @author 11797
 */
public interface UserBookListService {

    /**
     * 创建书单
     *
     * @return 书单ID
     */
    Long createBookList(Long userId, String listName, String description);

    /**
     * 更新书单名称与简介
     */
    void updateBookList(Long userId, Long listId, String listName, String description);

    /**
     * 删除书单（软删除）
     */
    void deleteBookList(Long userId, Long listId);

    /**
     * 分页查询我的书单
     */
    PageBean<BookListVO> listMinePage(Long userId, int page, int pageSize);

    /**
     * 分页查询广场书单
     */
    PageBean<BookListVO> listPlazaPage(int page, int pageSize);

    /**
     * 查询书单详情（含条目）
     *
     * @param userId 当前登录用户ID，未登录传null
     */
    BookListVO detail(Long listId, Long userId);

    /**
     * 将书籍加入书单
     */
    void addBook(Long userId, Long listId, Long bookId, String remark);

    /**
     * 从书单移除书籍
     */
    void removeBook(Long userId, Long listId, Long bookId);
}

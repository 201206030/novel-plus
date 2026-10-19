package com.java2nb.novel.mapper;

import com.java2nb.novel.entity.UserBookList;
import com.java2nb.novel.entity.UserBookListItem;
import com.java2nb.novel.vo.BookListItemVO;
import com.java2nb.novel.vo.BookListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户书单Mapper
 *
 * @author 11797
 */
@Mapper
public interface FrontUserBookListMapper {

    /**
     * 新增书单
     */
    int insertBookList(UserBookList bookList);

    /**
     * 更新书单名称与简介
     */
    int updateBookList(UserBookList bookList);

    /**
     * 软删除书单
     */
    int softDeleteById(@Param("id") Long id);

    /**
     * 书籍数量增减（delta为±1）
     */
    int addBookCount(@Param("id") Long id, @Param("delta") int delta);

    /**
     * 查询书单（含创建人信息，不过滤status）
     */
    BookListVO getById(@Param("id") Long id);

    /**
     * 查询用户书单数量（排除已删除，用于书名查重）
     */
    int countByName(@Param("userId") Long userId, @Param("listName") String listName, @Param("excludeId") Long excludeId);

    /**
     * 分页查询我的书单
     */
    List<BookListVO> listMine(@Param("userId") Long userId);

    /**
     * 分页查询广场书单
     */
    List<BookListVO> listPlaza();

    /**
     * 新增书单条目
     */
    int insertItem(UserBookListItem item);

    /**
     * 删除书单条目
     */
    int deleteItem(@Param("listId") Long listId, @Param("bookId") Long bookId);

    /**
     * 判断条目是否已存在
     */
    int existsItem(@Param("listId") Long listId, @Param("bookId") Long bookId);

    /**
     * 查询书单条目列表（关联书籍信息）
     */
    List<BookListItemVO> listItems(@Param("listId") Long listId);
}

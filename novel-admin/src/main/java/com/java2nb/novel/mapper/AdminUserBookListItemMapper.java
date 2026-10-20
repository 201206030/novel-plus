package com.java2nb.novel.mapper;

import com.java2nb.novel.entity.UserBookListItem;
import com.java2nb.novel.vo.AdminUserBookListItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 书单明细Mapper
 *
 * @author Administrator
 */
@Mapper
public interface AdminUserBookListItemMapper {

    /**
     * 查询书单下的明细列表（含书名）
     *
     * @param listId 书单ID
     * @return 明细列表
     */
    List<AdminUserBookListItemVO> listByListId(@Param("listId") Long listId);

    /**
     * 查询书单下指定书籍的明细
     *
     * @param listId 书单ID
     * @param bookId 书籍ID
     * @return 明细
     */
    UserBookListItem getByListIdAndBookId(@Param("listId") Long listId, @Param("bookId") Long bookId);

    /**
     * 根据ID查询书单明细
     *
     * @param id 明细ID
     * @return 明细
     */
    UserBookListItem getById(@Param("id") Long id);

    /**
     * 新增书单明细
     *
     * @param item 书单明细
     * @return 影响行数
     */
    int insert(UserBookListItem item);

    /**
     * 修改书单明细推荐语
     *
     * @param item 书单明细
     * @return 影响行数
     */
    int update(UserBookListItem item);

    /**
     * 删除书单明细
     *
     * @param id 明细ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 删除书单下全部明细
     *
     * @param listId 书单ID
     * @return 影响行数
     */
    int deleteByListId(@Param("listId") Long listId);

}

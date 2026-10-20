package com.java2nb.novel.mapper;

import com.java2nb.novel.entity.UserBookList;
import com.java2nb.novel.vo.AdminUserBookListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 书单管理Mapper
 *
 * @author Administrator
 */
@Mapper
public interface AdminUserBookListMapper {

    /**
     * 分页查询书单列表（含创建用户昵称）
     *
     * @param listName 书单名（模糊匹配，可空）
     * @param userId   创建用户ID（可空）
     * @return 书单列表
     */
    List<AdminUserBookListVO> listAll(@Param("listName") String listName, @Param("userId") Long userId);

    /**
     * 根据ID查询书单
     *
     * @param id 书单ID
     * @return 书单
     */
    UserBookList getById(@Param("id") Long id);

    /**
     * 新增书单
     *
     * @param userBookList 书单
     * @return 影响行数
     */
    int insert(UserBookList userBookList);

    /**
     * 修改书单
     *
     * @param userBookList 书单
     * @return 影响行数
     */
    int update(UserBookList userBookList);

    /**
     * 逻辑删除书单（状态置为0）
     *
     * @param id 书单ID
     * @return 影响行数
     */
    int softDelete(@Param("id") Long id);

    /**
     * 批量逻辑删除书单
     *
     * @param ids 书单ID集合
     * @return 影响行数
     */
    int batchSoftDelete(@Param("ids") List<Long> ids);

    /**
     * 刷新书单书籍数量冗余字段
     *
     * @param listId 书单ID
     * @return 影响行数
     */
    int refreshBookCount(@Param("listId") Long listId);

}

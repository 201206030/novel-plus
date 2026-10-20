package com.java2nb.novel.mapper;

import com.java2nb.novel.vo.AdminBookCommentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 小说评论管理查询Mapper（列表联表查询，增删改复用公共BookCommentMapper）
 *
 * @author Administrator
 */
@Mapper
public interface AdminBookCommentQueryMapper {

    /**
     * 分页查询评论列表（含书名/评论用户昵称）
     *
     * @param bookId  书籍ID（可空）
     * @param keyword 评论内容关键字（可空）
     * @return 评论列表
     */
    List<AdminBookCommentVO> listAll(@Param("bookId") Long bookId, @Param("keyword") String keyword);

    /**
     * 根据ID查询评论详情
     *
     * @param id 评论ID
     * @return 评论详情
     */
    AdminBookCommentVO getById(@Param("id") Long id);

}

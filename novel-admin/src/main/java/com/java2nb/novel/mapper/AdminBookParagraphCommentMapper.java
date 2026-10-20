package com.java2nb.novel.mapper;

import com.java2nb.novel.vo.AdminBookParagraphCommentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 段评管理Mapper
 *
 * @author Administrator
 */
@Mapper
public interface AdminBookParagraphCommentMapper {

    /**
     * 分页查询段评列表（含书名/章节名/评论用户昵称）
     *
     * @param bookId  书籍ID（可空）
     * @param keyword 评论内容关键字（可空）
     * @return 段评列表
     */
    List<AdminBookParagraphCommentVO> listAll(@Param("bookId") Long bookId, @Param("keyword") String keyword);

    /**
     * 根据ID查询段评详情
     *
     * @param id 段评ID
     * @return 段评详情
     */
    AdminBookParagraphCommentVO getById(@Param("id") Long id);

    /**
     * 修改段评内容
     *
     * @param id            段评ID
     * @param commentContent 评论内容
     * @return 影响行数
     */
    int updateContent(@Param("id") Long id, @Param("commentContent") String commentContent);

    /**
     * 删除段评（连带删除其下回复）
     *
     * @param id 段评ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 批量删除段评（连带删除其下回复）
     *
     * @param ids 段评ID集合
     * @return 影响行数
     */
    int batchDeleteByIds(@Param("ids") List<Long> ids);

}

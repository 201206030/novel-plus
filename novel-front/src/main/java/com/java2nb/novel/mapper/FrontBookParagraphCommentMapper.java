package com.java2nb.novel.mapper;

import com.java2nb.novel.entity.BookParagraphComment;
import com.java2nb.novel.vo.BookParagraphCommentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 书籍段落评论Mapper
 *
 * @author 11797
 */
@Mapper
public interface FrontBookParagraphCommentMapper {

    /**
     * 新增段落评论/回复
     */
    int insert(BookParagraphComment comment);

    /**
     * 查询某段落全部评论（含回复，按时间正序）
     */
    List<BookParagraphCommentVO> listByParagraph(@Param("bookIndexId") Long bookIndexId,
        @Param("paragraphIdx") Integer paragraphIdx);

    /**
     * 按主键查询（含评论人信息）
     */
    BookParagraphCommentVO getById(@Param("id") Long id);

    /**
     * 查询用户发布的全部段评（含回复，带书名/封面/章节名，按时间倒序）
     */
    List<BookParagraphCommentVO> listMyParagraphComment(@Param("userId") Long userId);

    /**
     * 按作品查询顶层段评（作家后台评论管理用，带评论人/书名/章节名）
     */
    List<BookParagraphCommentVO> listTopByBook(@Param("bookId") Long bookId);

    /**
     * 按顶层段评ID批量查询回复（作家后台评论管理用）
     */
    List<BookParagraphCommentVO> listRepliesByRootIds(@Param("rootIds") List<Long> rootIds);

    /**
     * 顶层评论回复数+1
     */
    int addReplyCount(@Param("id") Long id);

    /**
     * 按段落分组统计章节内各段落评论数（含回复）
     */
    List<Map<String, Object>> countGroupByParagraph(@Param("bookIndexId") Long bookIndexId);
}

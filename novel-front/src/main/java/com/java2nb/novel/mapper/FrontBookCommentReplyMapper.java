package com.java2nb.novel.mapper;

import com.java2nb.novel.vo.BookCommentReplyVO;
import com.java2nb.novel.vo.BookCommentVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Administrator
 */
public interface FrontBookCommentReplyMapper extends BookCommentReplyMapper {

    List<BookCommentReplyVO> listCommentReplyByPage(@Param("userId") Long userId, @Param("commentId") Long commentId);

}

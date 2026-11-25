package com.java2nb.novel.service;

import com.java2nb.novel.entity.Book;
import com.java2nb.novel.vo.BookCommentReplyVO;
import com.java2nb.novel.vo.BookCommentVO;
import com.java2nb.novel.vo.BookParagraphCommentVO;
import io.github.xxyopen.model.page.PageBean;

import java.util.List;

/**
 * 作家后台作品评论管理服务
 *
 * @author 11797
 */
public interface AuthorCommentService {

    /**
     * 查询当前作者名下作品（评论管理页筛选下拉框用）
     */
    List<Book> listMyBooks(Long authorId);

    /**
     * 校验作品归属，不属于当前作者则抛出无权管理异常
     */
    Book checkBookOwned(Long bookId, Long authorId);

    /**
     * 分页查询某条书评的已有回复
     */
    PageBean<BookCommentReplyVO> listBookReplies(Long authorId, Long commentId, int page, int pageSize);

    /**
     * 分页查询作品书评
     */
    PageBean<BookCommentVO> listBookComments(Long authorId, Long bookId, int page, int pageSize);

    /**
     * 分页查询作品段评（顶层评论 + 回复）
     */
    PageBean<BookParagraphCommentVO> listParagraphComments(Long authorId, Long bookId, int page, int pageSize);

    /**
     * 回复书评
     */
    void replyBookComment(Long userId, Long authorId, Long commentId, String content);

    /**
     * 回复段评
     */
    void replyParagraphComment(Long userId, Long authorId, Long rootId, String content,
        Long replyUserId, String replyUserName);
}

package com.java2nb.novel.controller;

import com.java2nb.novel.core.bean.UserDetails;
import com.java2nb.novel.core.enums.ResponseStatus;
import com.java2nb.novel.entity.Author;
import com.java2nb.novel.entity.Book;
import com.java2nb.novel.service.AuthorCommentService;
import com.java2nb.novel.service.AuthorService;
import com.java2nb.novel.vo.BookCommentReplyVO;
import com.java2nb.novel.vo.BookCommentVO;
import com.java2nb.novel.vo.BookParagraphCommentVO;
import io.github.xxyopen.model.page.PageBean;
import io.github.xxyopen.model.resp.RestResult;
import io.github.xxyopen.web.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 作家后台作品评论/段评管理接口
 *
 * <p>所有查询和回复均校验作品归属当前作者，且仅提供查看与回复能力，不提供修改/删除。</p>
 *
 * @author 11797
 */
@RequestMapping("author/comment")
@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthorCommentController extends BaseController {

    private final AuthorCommentService authorCommentService;

    private final AuthorService authorService;

    /**
     * 当前作者名下作品列表（评论管理页筛选下拉框用）
     */
    @GetMapping("bookList")
    public RestResult<List<Book>> bookList(HttpServletRequest request) {
        Author author = checkAuthor(request);
        return RestResult.ok(authorCommentService.listMyBooks(author.getId()));
    }

    /**
     * 某条书评的已有回复（作家回复前查看上下文用）
     */
    @GetMapping("replyList")
    public RestResult<PageBean<BookCommentReplyVO>> replyList(@RequestParam("commentId") Long commentId,
        @RequestParam(value = "curr", defaultValue = "1") int page,
        @RequestParam(value = "limit", defaultValue = "20") int pageSize, HttpServletRequest request) {
        Author author = checkAuthor(request);
        return RestResult.ok(authorCommentService.listBookReplies(author.getId(), commentId, page, pageSize));
    }

    /**
     * 作品书评分页列表
     */
    @GetMapping("bookPage")
    public RestResult<PageBean<BookCommentVO>> bookPage(@RequestParam("bookId") Long bookId,
        @RequestParam(value = "curr", defaultValue = "1") int page,
        @RequestParam(value = "limit", defaultValue = "10") int pageSize, HttpServletRequest request) {
        Author author = checkAuthor(request);
        return RestResult.ok(authorCommentService.listBookComments(author.getId(), bookId, page, pageSize));
    }

    /**
     * 作品段评分页列表（含当前页顶层评论的回复）
     */
    @GetMapping("paragraphPage")
    public RestResult<PageBean<BookParagraphCommentVO>> paragraphPage(@RequestParam("bookId") Long bookId,
        @RequestParam(value = "curr", defaultValue = "1") int page,
        @RequestParam(value = "limit", defaultValue = "10") int pageSize, HttpServletRequest request) {
        Author author = checkAuthor(request);
        return RestResult.ok(authorCommentService.listParagraphComments(author.getId(), bookId, page, pageSize));
    }

    /**
     * 回复书评（读者端会收到被回复通知；作家端不提供修改/删除）
     */
    @PostMapping("replyBook")
    public RestResult<Void> replyBook(@RequestParam("commentId") Long commentId,
        @RequestParam("content") String content, HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(request);
        if (userDetails == null) {
            throw new BusinessException(ResponseStatus.NO_LOGIN);
        }
        Author author = checkAuthor(request);
        authorCommentService.replyBookComment(userDetails.getId(), author.getId(), commentId, content);
        return RestResult.ok();
    }

    /**
     * 回复段评（读者端会收到被回复通知；作家端不提供修改/删除）
     */
    @PostMapping("replyParagraph")
    public RestResult<Void> replyParagraph(@RequestParam("rootId") Long rootId,
        @RequestParam("content") String content,
        @RequestParam(value = "replyUserId", required = false) Long replyUserId,
        @RequestParam(value = "replyUserName", required = false) String replyUserName,
        HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(request);
        if (userDetails == null) {
            throw new BusinessException(ResponseStatus.NO_LOGIN);
        }
        Author author = checkAuthor(request);
        authorCommentService.replyParagraphComment(userDetails.getId(), author.getId(), rootId, content,
            replyUserId, replyUserName);
        return RestResult.ok();
    }

    private Author checkAuthor(HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(request);
        if (userDetails == null) {
            throw new BusinessException(ResponseStatus.NO_LOGIN);
        }
        Author author = authorService.queryAuthor(userDetails.getId());
        if (author == null || author.getStatus() == 1) {
            throw new BusinessException(ResponseStatus.AUTHOR_STATUS_FORBIDDEN);
        }
        return author;
    }
}

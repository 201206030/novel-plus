package com.java2nb.novel.controller;

import com.java2nb.novel.entity.BookComment;
import com.java2nb.novel.service.CommentService;
import com.java2nb.novel.vo.AdminBookCommentVO;
import com.java2nb.novel.vo.AdminBookParagraphCommentVO;
import io.github.xxyopen.model.page.PageBean;
import io.github.xxyopen.model.resp.RestResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 评论管理Controller：小说评论/段评管理
 *
 * @author Administrator
 */
@RestController
@RequestMapping("comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("bookComment/list")
    public RestResult<PageBean<AdminBookCommentVO>> commentList(@RequestParam(required = false) Long bookId,
        @RequestParam(required = false) String keyword,
        @RequestParam(value = "curr", defaultValue = "1") int page,
        @RequestParam(value = "limit", defaultValue = "10") int pageSize) {
        return RestResult.ok(commentService.commentList(bookId, keyword, page, pageSize));
    }

    @GetMapping("bookComment/get")
    public RestResult<AdminBookCommentVO> commentGet(@RequestParam Long id) {
        return RestResult.ok(commentService.commentGet(id));
    }

    @PostMapping("bookComment/update")
    public RestResult<Void> commentUpdate(BookComment bookComment) {
        commentService.commentUpdate(bookComment);
        return RestResult.ok();
    }

    @PostMapping("bookComment/remove")
    public RestResult<Void> commentRemove(@RequestParam Long id) {
        commentService.commentRemove(id);
        return RestResult.ok();
    }

    @PostMapping("bookComment/batchRemove")
    public RestResult<Void> commentBatchRemove(@RequestParam String ids) {
        commentService.commentBatchRemove(ids);
        return RestResult.ok();
    }

    @GetMapping("paragraph/list")
    public RestResult<PageBean<AdminBookParagraphCommentVO>> paragraphList(@RequestParam(required = false) Long bookId,
        @RequestParam(required = false) String keyword,
        @RequestParam(value = "curr", defaultValue = "1") int page,
        @RequestParam(value = "limit", defaultValue = "10") int pageSize) {
        return RestResult.ok(commentService.paragraphList(bookId, keyword, page, pageSize));
    }

    @GetMapping("paragraph/get")
    public RestResult<AdminBookParagraphCommentVO> paragraphGet(@RequestParam Long id) {
        return RestResult.ok(commentService.paragraphGet(id));
    }

    @PostMapping("paragraph/update")
    public RestResult<Void> paragraphUpdate(@RequestParam Long id, @RequestParam String commentContent) {
        commentService.paragraphUpdate(id, commentContent);
        return RestResult.ok();
    }

    @PostMapping("paragraph/remove")
    public RestResult<Void> paragraphRemove(@RequestParam Long id) {
        commentService.paragraphRemove(id);
        return RestResult.ok();
    }

    @PostMapping("paragraph/batchRemove")
    public RestResult<Void> paragraphBatchRemove(@RequestParam String ids) {
        commentService.paragraphBatchRemove(ids);
        return RestResult.ok();
    }

}

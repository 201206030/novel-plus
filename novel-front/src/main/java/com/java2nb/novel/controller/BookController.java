package com.java2nb.novel.controller;

import com.java2nb.novel.core.bean.UserDetails;
import com.java2nb.novel.core.enums.ResponseStatus;
import com.java2nb.novel.core.utils.IpUtil;
import com.java2nb.novel.entity.*;
import com.java2nb.novel.service.BookContentService;
import com.java2nb.novel.service.BookParagraphCommentService;
import com.java2nb.novel.service.BookService;
import com.java2nb.novel.service.IpLocationService;
import com.java2nb.novel.service.LikeService;
import com.java2nb.novel.service.UserService;
import com.java2nb.novel.vo.*;
import io.github.xxyopen.model.page.PageBean;
import io.github.xxyopen.model.page.builder.pagehelper.PageBuilder;
import io.github.xxyopen.model.resp.RestResult;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 11797
 */
@RequestMapping("book")
@RestController
@Slf4j
@RequiredArgsConstructor
public class BookController extends BaseController {

    private final BookService bookService;

    private final Map<String, BookContentService> bookContentServiceMap;

    private final IpLocationService ipLocationService;

    private final LikeService likeService;

    private final BookParagraphCommentService bookParagraphCommentService;

    private final UserService userService;

    /**
     * 查询首页小说设置列表数据
     */
    @GetMapping("listBookSetting")
    public RestResult<Map<String, List<BookSettingVO>>> listBookSetting() {
        return RestResult.ok(bookService.listBookSettingVO());
    }

    /**
     * 查询首页点击榜单数据
     */
    @GetMapping("listClickRank")
    public RestResult<List<Book>> listClickRank() {
        return RestResult.ok(bookService.listClickRank());
    }

    /**
     * 查询首页新书榜单数据
     */
    @GetMapping("listNewRank")
    public RestResult<List<Book>> listNewRank() {
        return RestResult.ok(bookService.listNewRank());
    }

    /**
     * 查询首页更新榜单数据
     */
    @GetMapping("listUpdateRank")
    public RestResult<List<BookVO>> listUpdateRank() {
        return RestResult.ok(bookService.listUpdateRank());
    }

    /**
     * 查询小说分类列表
     */
    @GetMapping("listBookCategory")
    public RestResult<List<BookCategory>> listBookCategory() {
        return RestResult.ok(bookService.listBookCategory());
    }

    /**
     * 分页搜索
     */
    @GetMapping("searchByPage")
    public RestResult<?> searchByPage(@Validated BookSpVO bookSP, @RequestParam(value = "curr", defaultValue = "1") int page,
        @RequestParam(value = "limit", defaultValue = "20") int pageSize) {
        return RestResult.ok(bookService.searchByPage(bookSP, page, pageSize));
    }

    /**
     * 查询小说详情信息
     */
    @GetMapping("queryBookDetail/{id}")
    public RestResult<Book> queryBookDetail(@PathVariable("id") Long id) {
        return RestResult.ok(bookService.queryOnlineBookDetail(id));
    }


    /**
     * 查询小说排行信息
     */
    @GetMapping("listRank")
    public RestResult<List<Book>> listRank(@RequestParam(value = "type", defaultValue = "0") Byte type,
        @RequestParam(value = "limit", defaultValue = "30") Integer limit) {
        return RestResult.ok(bookService.listRank(type, limit));
    }

    /**
     * 增加点击次数
     */
    @PostMapping("addVisitCount")
    public RestResult<Void> addVisitCount(Long bookId) {
        bookService.addVisitCount(bookId, 1);
        return RestResult.ok();
    }

    /**
     * 查询章节相关信息
     */
    @GetMapping("queryBookIndexAbout")
    public RestResult<Map<String, Object>> queryBookIndexAbout(Long bookId, Long lastBookIndexId) {
        Map<String, Object> data = new HashMap<>(2);
        data.put("bookIndexCount", bookService.queryIndexCount(bookId));
        BookIndex bookIndex = bookService.queryBookIndex(lastBookIndexId);
        String lastBookContent = bookContentServiceMap.get(bookIndex.getStorageType())
            .queryBookContent(bookId, lastBookIndexId).getContent();
        if (lastBookContent.length() > 42) {
            lastBookContent = lastBookContent.substring(0, 42);
        }
        data.put("lastBookContent", lastBookContent);
        return RestResult.ok(data);
    }

    /**
     * 根据分类id查询同类推荐书籍
     */
    @GetMapping("listRecBookByCatId")
    public RestResult<List<Book>> listRecBookByCatId(Integer catId) {
        return RestResult.ok(bookService.listRecBookByCatId(catId));
    }

    /**
     * 读者端查询章节正文（供跨端客户端使用）
     */
    @GetMapping("queryBookIndexContent/{indexId}")
    public RestResult<?> queryBookIndexContent(@PathVariable("indexId") Long indexId,
        HttpServletRequest request) {
        BookIndex bookIndex;
        try {
            // queryBookIndex 对不存在的章节会抛异常，统一转为业务错误码
            bookIndex = bookService.queryBookIndex(indexId);
        } catch (RuntimeException e) {
            bookIndex = null;
        }
        if (bookIndex == null) {
            return RestResult.fail(ResponseStatus.INDEX_NOT_EXISTS);
        }
        Long bookId = bookIndex.getBookId();
        // 已下架（或未上架）的小说不允许读者阅读章节正文
        bookService.queryOnlineBookDetail(bookId);
        // 判断是否需要购买（与页面端渲染逻辑保持一致）
        boolean needBuy = false;
        if (bookIndex.getIsVip() != null && bookIndex.getIsVip() == 1) {
            UserDetails user = getUserDetails(request);
            if (user == null) {
                // 未登录，需要购买
                needBuy = true;
            } else {
                // 判断用户是否购买过该目录
                needBuy = !userService.queryIsBuyBookIndex(user.getId(), indexId);
            }
        }
        Map<String, Object> data = new HashMap<>(10);
        data.put("bookId", bookId);
        data.put("indexId", indexId);
        data.put("indexNum", bookIndex.getIndexNum());
        data.put("indexName", bookIndex.getIndexName());
        data.put("isVip", bookIndex.getIsVip());
        data.put("bookPrice", bookIndex.getBookPrice());
        data.put("preBookIndexId", bookService.queryPreBookIndexId(bookId, bookIndex.getIndexNum()));
        data.put("nextBookIndexId", bookService.queryNextBookIndexId(bookId, bookIndex.getIndexNum()));
        data.put("needBuy", needBuy);
        if (!needBuy) {
            BookContent bookContent = bookContentServiceMap.get(bookIndex.getStorageType())
                .queryBookContent(bookId, indexId);
            data.put("content", bookContent == null ? null : bookContent.getContent());
        }
        return RestResult.ok(data);
    }


    /**
     * 分页查询书籍评论列表
     */
    @GetMapping("listCommentByPage")
    public RestResult<PageBean<BookCommentVO>> listCommentByPage(@RequestParam("bookId") Long bookId,
        @RequestParam(value = "curr", defaultValue = "1") int page,
        @RequestParam(value = "limit", defaultValue = "5") int pageSize) {
        return RestResult.ok(bookService.listCommentByPage(null, bookId, page, pageSize));
    }

    /**
     * 分页查询评论回复列表
     */
    @GetMapping("listCommentReplyByPage")
    public RestResult<PageBean<BookCommentReplyVO>> listCommentReplyByPage(@RequestParam("commentId") Long commentId,
        @RequestParam(value = "curr", defaultValue = "1") int page,
        @RequestParam(value = "limit", defaultValue = "5") int pageSize) {
        return RestResult.ok(bookService.listCommentReplyByPage(null, commentId, page, pageSize));
    }

    /**
     * 新增评价
     */
    @PostMapping("addBookComment")
    public RestResult<?> addBookComment(BookComment comment, HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(request);
        if (userDetails == null) {
            return RestResult.fail(ResponseStatus.NO_LOGIN);
        }
        comment.setLocation(ipLocationService.getLocation(IpUtil.getRealIp(request)));
        bookService.addBookComment(userDetails.getId(), comment);
        return RestResult.ok();
    }

    /**
     * 评价点赞/取消点赞
     */
    @PostMapping("toggleCommentLike")
    public RestResult<?> toggleCommentLike(Long commentId, HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(request);
        if (userDetails == null) {
            return RestResult.fail(ResponseStatus.NO_LOGIN);
        }
        return RestResult.ok(likeService.toggleCommentLike(commentId, userDetails.getId()));
    }

    /**
     * 评价点踩/取消点踩
     */
    @PostMapping("toggleCommentUnLike")
    public RestResult<?> toggleCommentUnLike(Long commentId, HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(request);
        if (userDetails == null) {
            return RestResult.fail(ResponseStatus.NO_LOGIN);
        }
        return RestResult.ok(likeService.toggleCommentUnLike(commentId, userDetails.getId()));
    }

    /**
     * 查询章节内各段落评论数（key：段落序号，value：评论数）
     */
    @GetMapping("listParagraphCommentCounts")
    public RestResult<Map<Integer, Integer>> listParagraphCommentCounts(@RequestParam("bookIndexId") Long bookIndexId) {
        return RestResult.ok(bookParagraphCommentService.listParagraphCommentCounts(bookIndexId));
    }

    /**
     * 查询某段落评论列表
     */
    @GetMapping("listParagraphComment")
    public RestResult<List<BookParagraphCommentVO>> listParagraphComment(@RequestParam("bookIndexId") Long bookIndexId,
        @RequestParam("paragraphIdx") Integer paragraphIdx) {
        return RestResult.ok(bookParagraphCommentService.listParagraphComment(bookIndexId, paragraphIdx));
    }

    /**
     * 新增段落评论
     */
    @PostMapping("addParagraphComment")
    public RestResult<Void> addParagraphComment(BookParagraphComment comment, HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(request);
        if (userDetails == null) {
            return RestResult.fail(ResponseStatus.NO_LOGIN);
        }
        comment.setLocation(ipLocationService.getLocation(IpUtil.getRealIp(request)));
        bookParagraphCommentService.addParagraphComment(userDetails.getId(), comment);
        return RestResult.ok();
    }

    /**
     * 新增回复
     */
    @PostMapping("addCommentReply")
    public RestResult<?> addCommentReply(BookCommentReply commentReply, HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(request);
        if (userDetails == null) {
            return RestResult.fail(ResponseStatus.NO_LOGIN);
        }
        commentReply.setLocation(ipLocationService.getLocation(IpUtil.getRealIp(request)));
        bookService.addBookCommentReply(userDetails.getId(), commentReply);
        return RestResult.ok();
    }

    /**
     * 回复点赞/取消点赞
     */
    @PostMapping("toggleReplyLike")
    public RestResult<?> toggleReplyLike(Long replyId, HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(request);
        if (userDetails == null) {
            return RestResult.fail(ResponseStatus.NO_LOGIN);
        }
        return RestResult.ok(likeService.toggleReplyLike(replyId, userDetails.getId()));
    }

    /**
     * 回复点赞/取消点赞
     */
    @PostMapping("toggleReplyUnLike")
    public RestResult<?> toggleReplyUnLike(Long replyId, HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(request);
        if (userDetails == null) {
            return RestResult.fail(ResponseStatus.NO_LOGIN);
        }
        return RestResult.ok(likeService.toggleReplyUnLike(replyId, userDetails.getId()));
    }

    /**
     * 根据小说ID查询小说前十条最新更新目录集合
     */
    @GetMapping("queryNewIndexList")
    public RestResult<List<BookIndex>> queryNewIndexList(Long bookId) {
        return RestResult.ok(bookService.queryIndexList(bookId, "index_num desc", 1, 10));
    }

    /**
     * 目录页
     */
    @GetMapping("/queryIndexList")
    public RestResult<PageBean<BookIndex>> indexList(Long bookId,
        @RequestParam(value = "curr", defaultValue = "1") int page,
        @RequestParam(value = "limit", defaultValue = "5") int pageSize,
        @RequestParam(value = "orderBy", defaultValue = "index_num desc") String orderBy) {
        return RestResult.ok(PageBuilder.build(bookService.queryIndexList(bookId, orderBy, page, pageSize)));
    }


}

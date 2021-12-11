package com.java2nb.novel.controller;

import com.java2nb.novel.core.bean.UserDetails;
import com.java2nb.novel.core.enums.ResponseStatus;
import com.java2nb.novel.entity.Book;
import com.java2nb.novel.entity.BookCategory;
import com.java2nb.novel.entity.BookComment;
import com.java2nb.novel.entity.BookIndex;
import com.java2nb.novel.service.BookContentService;
import com.java2nb.novel.vo.BookCommentVO;
import com.java2nb.novel.vo.BookSettingVO;
import com.java2nb.novel.vo.BookSpVO;
import com.java2nb.novel.service.BookService;
import com.java2nb.novel.vo.BookVO;
import io.github.xxyopen.model.page.PageBean;
import io.github.xxyopen.model.page.builder.pagehelper.PageBuilder;
import io.github.xxyopen.model.resp.RestResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    private final RabbitTemplate rabbitTemplate;

    private final Map<String, BookContentService> bookContentServiceMap;

    @Value("${spring.rabbitmq.enable}")
    private Integer enableMq;


    /**
     * 查询首页小说设置列表数据
     */
    @GetMapping("listBookSetting")
    public RestResult<Map<Byte, List<BookSettingVO>>> listBookSetting() {
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
    public RestResult<?> searchByPage(BookSpVO bookSP, @RequestParam(value = "curr", defaultValue = "1") int page, @RequestParam(value = "limit", defaultValue = "20") int pageSize) {
        return RestResult.ok(bookService.searchByPage(bookSP, page, pageSize));
    }

    /**
     * 查询小说详情信息
     */
    @GetMapping("queryBookDetail/{id}")
    public RestResult<Book> queryBookDetail(@PathVariable("id") Long id) {
        return RestResult.ok(bookService.queryBookDetail(id));
    }


    /**
     * 查询小说排行信息
     */
    @GetMapping("listRank")
    public RestResult<List<Book>> listRank(@RequestParam(value = "type", defaultValue = "0") Byte type, @RequestParam(value = "limit", defaultValue = "30") Integer limit) {
        return RestResult.ok(bookService.listRank(type, limit));
    }

    /**
     * 增加点击次数
     */
    @PostMapping("addVisitCount")
    public RestResult<Void> addVisitCount(Long bookId) {
        if (enableMq == 1) {
            rabbitTemplate.convertAndSend("ADD-BOOK-VISIT-EXCHANGE", null, bookId);
        } else {
            bookService.addVisitCount(bookId, 1);
        }
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
        String lastBookContent = bookContentServiceMap.get(bookIndex.getStorageType()).queryBookContent(bookId,lastBookIndexId).getContent();
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
     * 分页查询书籍评论列表
     */
    @GetMapping("listCommentByPage")
    public RestResult<PageBean<BookCommentVO>> listCommentByPage(@RequestParam("bookId") Long bookId, @RequestParam(value = "curr", defaultValue = "1") int page, @RequestParam(value = "limit", defaultValue = "5") int pageSize) {
        return RestResult.ok(bookService.listCommentByPage(null, bookId, page, pageSize));
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
        bookService.addBookComment(userDetails.getId(), comment);
        return RestResult.ok();
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
    public RestResult<PageBean<BookIndex>> indexList(Long bookId, @RequestParam(value = "curr", defaultValue = "1") int page, @RequestParam(value = "limit", defaultValue = "5") int pageSize, @RequestParam(value = "orderBy", defaultValue = "index_num desc") String orderBy) {
        return RestResult.ok(PageBuilder.build(bookService.queryIndexList(bookId, orderBy, page, pageSize)));
    }


}

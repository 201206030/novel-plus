package com.java2nb.novel.controller;

import com.java2nb.novel.core.bean.UserDetails;
import com.java2nb.novel.core.enums.ResponseStatus;
import com.java2nb.novel.service.UserBookListService;
import com.java2nb.novel.vo.BookListVO;
import io.github.xxyopen.model.page.PageBean;
import io.github.xxyopen.model.resp.RestResult;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户书单
 *
 * @author 11797
 */
@RequestMapping("bookList")
@RestController
@Slf4j
@RequiredArgsConstructor
public class UserBookListController extends BaseController {

    private final UserBookListService userBookListService;

    /**
     * 分页查询书单广场
     */
    @GetMapping("plaza")
    public RestResult<PageBean<BookListVO>> plaza(@RequestParam(value = "curr", defaultValue = "1") int page,
        @RequestParam(value = "limit", defaultValue = "10") int pageSize) {
        return RestResult.ok(userBookListService.listPlazaPage(page, pageSize));
    }

    /**
     * 分页查询我的书单
     */
    @GetMapping("mine")
    public RestResult<?> mine(@RequestParam(value = "curr", defaultValue = "1") int page,
        @RequestParam(value = "limit", defaultValue = "20") int pageSize, HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(request);
        if (userDetails == null) {
            return RestResult.fail(ResponseStatus.NO_LOGIN);
        }
        return RestResult.ok(userBookListService.listMinePage(userDetails.getId(), page, pageSize));
    }

    /**
     * 查询书单详情（任何登录状态可看）
     */
    @GetMapping("detail")
    public RestResult<BookListVO> detail(@RequestParam("listId") Long listId, HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(request);
        Long userId = userDetails == null ? null : userDetails.getId();
        return RestResult.ok(userBookListService.detail(listId, userId));
    }

    /**
     * 创建书单
     *
     * @return 书单ID
     */
    @PostMapping("create")
    public RestResult<?> create(String listName, String description, HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(request);
        if (userDetails == null) {
            return RestResult.fail(ResponseStatus.NO_LOGIN);
        }
        return RestResult.ok(userBookListService.createBookList(userDetails.getId(), listName, description));
    }

    /**
     * 更新书单
     */
    @PostMapping("update")
    public RestResult<Void> update(Long listId, String listName, String description, HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(request);
        if (userDetails == null) {
            return RestResult.fail(ResponseStatus.NO_LOGIN);
        }
        userBookListService.updateBookList(userDetails.getId(), listId, listName, description);
        return RestResult.ok();
    }

    /**
     * 删除书单
     */
    @PostMapping("delete")
    public RestResult<Void> delete(Long listId, HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(request);
        if (userDetails == null) {
            return RestResult.fail(ResponseStatus.NO_LOGIN);
        }
        userBookListService.deleteBookList(userDetails.getId(), listId);
        return RestResult.ok();
    }

    /**
     * 将书籍加入书单
     */
    @PostMapping("addBook")
    public RestResult<Void> addBook(Long listId, Long bookId, String remark, HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(request);
        if (userDetails == null) {
            return RestResult.fail(ResponseStatus.NO_LOGIN);
        }
        userBookListService.addBook(userDetails.getId(), listId, bookId, remark);
        return RestResult.ok();
    }

    /**
     * 从书单移除书籍
     */
    @PostMapping("removeBook")
    public RestResult<Void> removeBook(Long listId, Long bookId, HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(request);
        if (userDetails == null) {
            return RestResult.fail(ResponseStatus.NO_LOGIN);
        }
        userBookListService.removeBook(userDetails.getId(), listId, bookId);
        return RestResult.ok();
    }
}

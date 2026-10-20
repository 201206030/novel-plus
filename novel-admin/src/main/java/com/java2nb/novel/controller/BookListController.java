package com.java2nb.novel.controller;

import com.java2nb.novel.entity.User;
import com.java2nb.novel.entity.UserBookList;
import com.java2nb.novel.service.BookListService;
import com.java2nb.novel.vo.AdminUserBookListItemVO;
import com.java2nb.novel.vo.AdminUserBookListVO;
import io.github.xxyopen.model.page.PageBean;
import io.github.xxyopen.model.resp.RestResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 书单管理Controller：书单列表/书单明细
 *
 * @author Administrator
 */
@RestController
@RequestMapping("bookList")
@RequiredArgsConstructor
public class BookListController {

    private final BookListService bookListService;

    @GetMapping("bookList/list")
    public RestResult<PageBean<AdminUserBookListVO>> list(@RequestParam(required = false) String keyword,
        @RequestParam(required = false) Long userId,
        @RequestParam(value = "curr", defaultValue = "1") int page,
        @RequestParam(value = "limit", defaultValue = "10") int pageSize) {
        return RestResult.ok(bookListService.list(keyword, userId, page, pageSize));
    }

    @GetMapping("bookList/get")
    public RestResult<UserBookList> get(@RequestParam Long id) {
        return RestResult.ok(bookListService.get(id));
    }

    @GetMapping("bookList/users")
    public RestResult<List<User>> userOptions(@RequestParam(required = false) String keyword) {
        return RestResult.ok(bookListService.userOptions(keyword));
    }

    @PostMapping("bookList/save")
    public RestResult<Void> save(UserBookList userBookList) {
        bookListService.save(userBookList);
        return RestResult.ok();
    }

    @PostMapping("bookList/update")
    public RestResult<Void> update(UserBookList userBookList) {
        bookListService.update(userBookList);
        return RestResult.ok();
    }

    @PostMapping("bookList/remove")
    public RestResult<Void> remove(@RequestParam Long id) {
        bookListService.remove(id);
        return RestResult.ok();
    }

    @PostMapping("bookList/batchRemove")
    public RestResult<Void> batchRemove(@RequestParam String ids) {
        bookListService.batchRemove(ids);
        return RestResult.ok();
    }

    @GetMapping("item/list")
    public RestResult<PageBean<AdminUserBookListItemVO>> itemList(@RequestParam Long listId,
        @RequestParam(value = "curr", defaultValue = "1") int page,
        @RequestParam(value = "limit", defaultValue = "10") int pageSize) {
        return RestResult.ok(bookListService.itemList(listId, page, pageSize));
    }

    @PostMapping("item/save")
    public RestResult<Void> itemAdd(@RequestParam Long listId, @RequestParam Long bookId,
        @RequestParam(required = false) String remark) {
        bookListService.itemAdd(listId, bookId, remark);
        return RestResult.ok();
    }

    @PostMapping("item/update")
    public RestResult<Void> itemUpdate(@RequestParam Long id, @RequestParam String remark) {
        bookListService.itemUpdate(id, remark);
        return RestResult.ok();
    }

    @PostMapping("item/remove")
    public RestResult<Void> itemRemove(@RequestParam Long id) {
        bookListService.itemRemove(id);
        return RestResult.ok();
    }

}

package com.java2nb.novel.controller;

import com.java2nb.novel.entity.Book;
import com.java2nb.novel.entity.BookContent;
import com.java2nb.novel.entity.BookIndex;
import com.java2nb.novel.service.BookService;
import io.github.xxyopen.model.page.PageBean;
import io.github.xxyopen.model.resp.RestResult;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 小说管理Controller：小说列表/章节管理/内容管理
 *
 * @author Administrator
 */
@RestController
@RequestMapping("book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("book/list")
    public RestResult<PageBean<Book>> bookList(@RequestParam(required = false) String keyword,
        @RequestParam(value = "curr", defaultValue = "1") int page,
        @RequestParam(value = "limit", defaultValue = "10") int pageSize) {
        return RestResult.ok(bookService.bookList(keyword, page, pageSize));
    }

    @GetMapping("book/get")
    public RestResult<Book> bookGet(@RequestParam Long id) {
        return RestResult.ok(bookService.bookGet(id));
    }

    @PostMapping("book/save")
    public RestResult<Void> bookSave(Book book) {
        bookService.bookSave(book);
        return RestResult.ok();
    }

    @PostMapping("book/update")
    public RestResult<Void> bookUpdate(Book book) {
        bookService.bookUpdate(book);
        return RestResult.ok();
    }

    /**
     * 小说上架/下架（status：0 下架，1 上架）
     */
    @PostMapping("book/status")
    public RestResult<Void> bookStatus(@RequestParam Long id, @RequestParam Byte status) {
        bookService.bookStatus(id, status);
        return RestResult.ok();
    }

    /**
     * 一键上架所有小说
     *
     * @return 本次上架的小说数量
     */
    @PostMapping("book/batchOnline")
    public RestResult<Integer> bookBatchOnline() {
        return RestResult.ok(bookService.bookOnlineAll());
    }

    @PostMapping("book/remove")
    public RestResult<Void> bookRemove(@RequestParam Long id) {
        bookService.bookRemove(id);
        return RestResult.ok();
    }

    @PostMapping("book/batchRemove")
    public RestResult<Void> bookBatchRemove(@RequestParam String ids) {
        bookService.bookBatchRemove(ids);
        return RestResult.ok();
    }

    /**
     * 下载小说全部正文（TXT）；直接写响应流，避免被全局 ResponseBodyAdvice 拦截序列化
     */
    @SneakyThrows
    @GetMapping("book/download")
    public void bookDownload(@RequestParam Long bookId, HttpServletResponse response) {
        Book book = bookService.bookGet(bookId);
        String text = bookService.bookDownload(bookId);
        String fileName = URLEncoder.encode(book.getBookName() + ".txt", StandardCharsets.UTF_8);
        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + fileName);
        response.getOutputStream().write(text.getBytes(StandardCharsets.UTF_8));
        response.getOutputStream().flush();
    }

    @GetMapping("bookIndex/list")
    public RestResult<PageBean<BookIndex>> indexList(@RequestParam Long bookId,
        @RequestParam(required = false) String keyword,
        @RequestParam(value = "curr", defaultValue = "1") int page,
        @RequestParam(value = "limit", defaultValue = "10") int pageSize) {
        return RestResult.ok(bookService.indexList(bookId, keyword, page, pageSize));
    }

    @GetMapping("bookIndex/get")
    public RestResult<BookIndex> indexGet(@RequestParam Long id) {
        return RestResult.ok(bookService.indexGet(id));
    }

    @PostMapping("bookIndex/save")
    public RestResult<Void> indexSave(BookIndex bookIndex) {
        bookService.indexSave(bookIndex);
        return RestResult.ok();
    }

    @PostMapping("bookIndex/update")
    public RestResult<Void> indexUpdate(BookIndex bookIndex) {
        bookService.indexUpdate(bookIndex);
        return RestResult.ok();
    }

    @PostMapping("bookIndex/remove")
    public RestResult<Void> indexRemove(@RequestParam Long id) {
        bookService.indexRemove(id);
        return RestResult.ok();
    }

    @PostMapping("bookIndex/batchRemove")
    public RestResult<Void> indexBatchRemove(@RequestParam String ids) {
        bookService.indexBatchRemove(ids);
        return RestResult.ok();
    }

    @GetMapping("bookContent/get")
    public RestResult<BookContent> contentGet(@RequestParam Long indexId) {
        return RestResult.ok(bookService.contentGet(indexId));
    }

    @PostMapping("bookContent/save")
    public RestResult<Void> contentSave(@RequestParam Long indexId, @RequestParam String content) {
        bookService.contentSave(indexId, content);
        return RestResult.ok();
    }

}

package com.java2nb.novel.controller;

import com.java2nb.novel.entity.Author;
import com.java2nb.novel.entity.AuthorCode;
import com.java2nb.novel.service.AuthorIncomeService;
import com.java2nb.novel.service.AuthorService;
import com.java2nb.novel.vo.AdminAuthorIncomeVO;
import io.github.xxyopen.model.page.PageBean;
import io.github.xxyopen.model.resp.RestResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 作家管理Controller：作者列表/邀请码管理
 *
 * @author Administrator
 */
@RestController
@RequestMapping("author")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    private final AuthorIncomeService authorIncomeService;

    @GetMapping("author/list")
    public RestResult<PageBean<Author>> authorList(@RequestParam(required = false) String keyword,
        @RequestParam(value = "curr", defaultValue = "1") int page,
        @RequestParam(value = "limit", defaultValue = "10") int pageSize) {
        return RestResult.ok(authorService.authorList(keyword, page, pageSize));
    }

    @GetMapping("author/get")
    public RestResult<Author> authorGet(@RequestParam Long id) {
        return RestResult.ok(authorService.authorGet(id));
    }

    @PostMapping("author/save")
    public RestResult<Void> authorSave(Author author) {
        authorService.authorSave(author);
        return RestResult.ok();
    }

    @PostMapping("author/update")
    public RestResult<Void> authorUpdate(Author author) {
        authorService.authorUpdate(author);
        return RestResult.ok();
    }

    @PostMapping("author/remove")
    public RestResult<Void> authorRemove(@RequestParam Long id) {
        authorService.authorRemove(id);
        return RestResult.ok();
    }

    @PostMapping("author/batchRemove")
    public RestResult<Void> authorBatchRemove(@RequestParam String ids) {
        authorService.authorBatchRemove(ids);
        return RestResult.ok();
    }

    @PostMapping("author/status")
    public RestResult<Void> authorStatus(@RequestParam Long id, @RequestParam Byte status) {
        authorService.authorStatus(id, status);
        return RestResult.ok();
    }

    @GetMapping("authorCode/list")
    public RestResult<PageBean<AuthorCode>> codeList(@RequestParam(required = false) String keyword,
        @RequestParam(value = "curr", defaultValue = "1") int page,
        @RequestParam(value = "limit", defaultValue = "10") int pageSize) {
        return RestResult.ok(authorService.codeList(keyword, page, pageSize));
    }

    @GetMapping("authorCode/get")
    public RestResult<AuthorCode> codeGet(@RequestParam Long id) {
        return RestResult.ok(authorService.codeGet(id));
    }

    @PostMapping("authorCode/save")
    public RestResult<Void> codeSave(AuthorCode authorCode) {
        authorService.codeSave(authorCode);
        return RestResult.ok();
    }

    @PostMapping("authorCode/update")
    public RestResult<Void> codeUpdate(AuthorCode authorCode) {
        authorService.codeUpdate(authorCode);
        return RestResult.ok();
    }

    @PostMapping("authorCode/remove")
    public RestResult<Void> codeRemove(@RequestParam Long id) {
        authorService.codeRemove(id);
        return RestResult.ok();
    }

    @PostMapping("authorCode/batchRemove")
    public RestResult<Void> codeBatchRemove(@RequestParam String ids) {
        authorService.codeBatchRemove(ids);
        return RestResult.ok();
    }

    @GetMapping("income/list")
    public RestResult<PageBean<AdminAuthorIncomeVO>> incomeList(@RequestParam(required = false) String keyword,
        @RequestParam(required = false) Byte payStatus,
        @RequestParam(required = false) String month,
        @RequestParam(value = "curr", defaultValue = "1") int page,
        @RequestParam(value = "limit", defaultValue = "10") int pageSize) {
        return RestResult.ok(authorIncomeService.incomeList(keyword, payStatus, month, page, pageSize));
    }

    @PostMapping("income/settle")
    public RestResult<Void> incomeSettle(@RequestParam Long id) {
        authorIncomeService.incomeSettle(id);
        return RestResult.ok();
    }

    @PostMapping("income/batchSettle")
    public RestResult<Integer> incomeBatchSettle(@RequestParam String ids) {
        return RestResult.ok(authorIncomeService.incomeBatchSettle(ids));
    }

    @PostMapping("income/settleAll")
    public RestResult<Integer> incomeSettleAll() {
        return RestResult.ok(authorIncomeService.incomeSettleAll());
    }

}

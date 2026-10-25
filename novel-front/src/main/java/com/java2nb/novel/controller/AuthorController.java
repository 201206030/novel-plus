package com.java2nb.novel.controller;

import io.github.xxyopen.model.page.PageBean;

import com.java2nb.novel.core.bean.UserDetails;
import com.java2nb.novel.core.enums.ResponseStatus;
import io.github.xxyopen.model.resp.RestResult;
import io.github.xxyopen.web.exception.BusinessException;
import com.java2nb.novel.entity.Author;
import com.java2nb.novel.entity.AuthorIncome;
import com.java2nb.novel.entity.AuthorIncomeDetail;
import com.java2nb.novel.entity.Book;
import com.java2nb.novel.service.AuthorService;
import com.java2nb.novel.service.BookService;
import com.java2nb.novel.vo.BookIncomeSummaryVO;
import com.java2nb.novel.vo.IncomeRuleVO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.Date;
import java.util.List;

/**
 * @author 11797
 */
@RequestMapping("author")
@RestController
@Slf4j
@RequiredArgsConstructor
public class AuthorController extends BaseController {

    private final AuthorService authorService;

    private final BookService bookService;

    private final ChatClient chatClient;

    private final OpenAiChatModel chatModel;

    /**
     * 校验笔名是否存在（资料页改笔名时可传 excludeAuthorId 排除自身）
     */
    @GetMapping("checkPenName")
    public RestResult<Boolean> checkPenName(String penName,
        @RequestParam(value = "excludeAuthorId", required = false) Long excludeAuthorId) {

        return RestResult.ok(authorService.checkPenName(penName, excludeAuthorId));
    }

    /**
     * 查询当前作家完整资料（供作者信息页回显）
     */
    @GetMapping("authorInfo")
    public RestResult<Author> authorInfo(HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(request);
        if (userDetails == null) {
            throw new BusinessException(ResponseStatus.NO_LOGIN);
        }
        return RestResult.ok(authorService.queryAuthorAll(userDetails.getId()));
    }

    /**
     * 更新当前作家资料（笔名/头像/简介/联系方式）
     */
    @PostMapping("updateAuthorInfo")
    public RestResult<Void> updateAuthorInfo(Author form, HttpServletRequest request) {
        Author current = checkAuthor(request);
        Long userId = getUserDetails(request).getId();

        String penName = StringUtils.trimToEmpty(form.getPenName());
        String telPhone = StringUtils.trimToEmpty(form.getTelPhone());
        String email = StringUtils.trimToEmpty(form.getEmail());
        // 服务端同步校验格式
        if (!penName.matches("^[\\u4e00-\\u9fa5a-zA-Z]{2,8}$")
            || !telPhone.matches("^1[3-9]\\d{9}$")
            || !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new BusinessException(ResponseStatus.AUTHOR_INFO_INVALID);
        }
        // 笔名发生变更时才查重，排除自身
        if (!penName.equals(current.getPenName())
            && Boolean.TRUE.equals(authorService.checkPenName(penName, current.getId()))) {
            throw new BusinessException(ResponseStatus.PEN_NAME_EXISTS);
        }
        form.setPenName(penName);
        authorService.updateAuthorInfo(userId, form);
        return RestResult.ok();
    }

    /**
     * 查询稿费分成规则（供收入明细页说明用）
     */
    @GetMapping("incomeRule")
    public RestResult<IncomeRuleVO> incomeRule() {
        return RestResult.ok(authorService.incomeRule());
    }

    /**
     * 按作品汇总订阅收入（收入明细页顶部卡片）
     */
    @GetMapping("bookIncomeSummary")
    public RestResult<List<BookIncomeSummaryVO>> bookIncomeSummary(
        @RequestParam(value = "bookId", defaultValue = "0") Long bookId,
        @RequestParam(value = "startTime", defaultValue = "2020-05-01") Date startTime,
        @RequestParam(value = "endTime", defaultValue = "2030-01-01") Date endTime,
        HttpServletRequest request) {

        return RestResult.ok(
            authorService.bookIncomeSummary(getUserDetails(request).getId(), bookId, startTime, endTime));
    }

    /**
     * 作家发布小说分页列表查询
     */
    @GetMapping("listBookByPage")
    public RestResult<PageBean<Book>> listBookByPage(@RequestParam(value = "curr", defaultValue = "1") int page,
        @RequestParam(value = "limit", defaultValue = "10") int pageSize, HttpServletRequest request) {

        return RestResult.ok(bookService.listBookPageByUserId(getUserDetails(request).getId(), page, pageSize));
    }

    /**
     * 发布小说
     */
    @PostMapping("addBook")
    public RestResult<Void> addBook(@RequestParam("bookDesc") String bookDesc, Book book, HttpServletRequest request) {

        Author author = checkAuthor(request);

        //bookDesc不能使用book对象来接收，否则会自动去掉前面的空格
        book.setBookDesc(bookDesc
            .replaceAll("\\n", "<br>")
            .replaceAll("\\s", "&nbsp;"));
        //发布小说
        bookService.addBook(book, author.getId(), author.getPenName());

        return RestResult.ok();
    }

    /**
     * 更新小说状态,上架或下架
     */
    @PostMapping("updateBookStatus")
    public RestResult<Void> updateBookStatus(Long bookId, Byte status, HttpServletRequest request) {
        Author author = checkAuthor(request);

        //更新小说状态,上架或下架
        bookService.updateBookStatus(bookId, status, author.getId());

        return RestResult.ok();
    }

    /**
     * 完结小说
     */
    @PostMapping("finishBook")
    public RestResult<Void> finishBook(Long bookId, HttpServletRequest request) {
        Author author = checkAuthor(request);

        //完结小说
        bookService.finishBook(bookId, author.getId());

        return RestResult.ok();
    }


    /**
     * 删除章节
     */
    @DeleteMapping("deleteIndex/{indexId}")
    public RestResult<Void> deleteIndex(@PathVariable("indexId") Long indexId, HttpServletRequest request) {

        Author author = checkAuthor(request);

        //删除章节
        bookService.deleteIndex(indexId, author.getId());

        return RestResult.ok();
    }


    /**
     * 发布章节内容
     */
    @PostMapping("addBookContent")
    public RestResult<Void> addBookContent(Long bookId, String indexName, String content, Byte isVip,
        HttpServletRequest request) {
        Author author = checkAuthor(request);

        content = content.replaceAll("\\n", "<br>")
            .replaceAll("\\s", "&nbsp;");
        //发布章节内容
        bookService.addBookContent(bookId, indexName, content, isVip, author.getId());

        return RestResult.ok();
    }

    /**
     * 查询章节内容
     */
    @GetMapping("queryIndexContent/{indexId}")
    public RestResult<String> queryIndexContent(@PathVariable("indexId") Long indexId, HttpServletRequest request) {

        Author author = checkAuthor(request);

        String content = bookService.queryIndexContent(indexId, author.getId());

        content = content.replaceAll("<br>", "\n")
            .replaceAll("&nbsp;", " ");

        return RestResult.ok(content);
    }

    /**
     * 更新章节内容
     */
    @PostMapping("updateBookContent")
    public RestResult<Void> updateBookContent(Long indexId, String indexName, String content,
        HttpServletRequest request) {
        Author author = checkAuthor(request);

        content = content.replaceAll("\\n", "<br>")
            .replaceAll("\\s", "&nbsp;");
        //更新章节内容
        bookService.updateBookContent(indexId, indexName, content, author.getId());

        return RestResult.ok();
    }

    /**
     * 修改小说封面
     */
    @PostMapping("updateBookPic")
    public RestResult<Void> updateBookPic(@RequestParam("bookId") Long bookId, @RequestParam("bookPic") String bookPic,
        HttpServletRequest request) {
        Author author = checkAuthor(request);
        bookService.updateBookPic(bookId, bookPic, author.getId());
        return RestResult.ok();
    }


    /**
     * 作家日收入统计数据分页列表查询
     */
    @GetMapping("listIncomeDailyByPage")
    public RestResult<PageBean<AuthorIncomeDetail>> listIncomeDailyByPage(
        @RequestParam(value = "curr", defaultValue = "1") int page,
        @RequestParam(value = "limit", defaultValue = "10") int pageSize,
        @RequestParam(value = "bookId", defaultValue = "0") Long bookId,
        @RequestParam(value = "startTime", defaultValue = "2020-05-01") Date startTime,
        @RequestParam(value = "endTime", defaultValue = "2030-01-01") Date endTime,
        HttpServletRequest request) {

        return RestResult.ok(
            authorService.listIncomeDailyByPage(page, pageSize, getUserDetails(request).getId(), bookId, startTime,
                endTime));
    }


    /**
     * 作家月收入统计数据分页列表查询
     */
    @GetMapping("listIncomeMonthByPage")
    public RestResult<PageBean<AuthorIncome>> listIncomeMonthByPage(
        @RequestParam(value = "curr", defaultValue = "1") int page,
        @RequestParam(value = "limit", defaultValue = "10") int pageSize,
        @RequestParam(value = "bookId", defaultValue = "0") Long bookId,
        HttpServletRequest request) {

        return RestResult.ok(
            authorService.listIncomeMonthByPage(page, pageSize, getUserDetails(request).getId(), bookId));
    }

    private Author checkAuthor(HttpServletRequest request) {

        UserDetails userDetails = getUserDetails(request);
        if (userDetails == null) {
            throw new BusinessException(ResponseStatus.NO_LOGIN);
        }

        //查询作家信息
        Author author = authorService.queryAuthor(userDetails.getId());

        //判断作者状态是否正常
        if (author.getStatus() == 1) {
            //封禁状态，不能发布小说
            throw new BusinessException(ResponseStatus.AUTHOR_STATUS_FORBIDDEN);
        }

        return author;


    }

    /**
     * 查询AI生成图片
     */
    @GetMapping("queryAiGenPic")
    public RestResult<String> queryAiGenPic(@RequestParam("bookId") Long bookId) {
        return RestResult.ok(bookService.queryAiGenPic(bookId));
    }

    /**
     * AI扩写
     */
    @PostMapping("ai/expand")
    public RestResult<String> expandText(@RequestParam("text") String text, @RequestParam("ratio") Double ratio) {
        String prompt = "请将以下文本扩写为原长度的" + ratio / 100 + "倍：" + text;
        return RestResult.ok(chatClient.prompt()
            .user(prompt)
            .call()
            .content());
    }

    /**
     * AI缩写
     */
    @PostMapping("ai/condense")
    public RestResult<String> condenseText(@RequestParam("text") String text, @RequestParam("ratio") Integer ratio) {
        String prompt = "请将以下文本缩写为原长度的" + 100 / ratio + "分之一：" + text;
        return RestResult.ok(chatClient.prompt()
            .user(prompt)
            .call()
            .content());
    }

    /**
     * AI续写
     */
    @PostMapping("ai/continue")
    public RestResult<String> continueText(@RequestParam("text") String text, @RequestParam("length") Integer length) {
        String prompt = "请续写以下文本，续写长度约为" + length + "字：" + text;
        return RestResult.ok(chatClient.prompt()
            .user(prompt)
            .call()
            .content());
    }

    /**
     * AI润色
     */
    @PostMapping("ai/polish")
    public RestResult<String> polishText(@RequestParam("text") String text) {
        String prompt = "请润色优化以下文本，保持原意：" + text;
        return RestResult.ok(chatClient.prompt()
            .user(prompt)
            .call()
            .content());
    }

    /**
     * AI扩写
     */
    @GetMapping(value = "ai/stream/expand", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamExpandText(@RequestParam("text") String text, @RequestParam("ratio") Double ratio) {
        String prompt = "请将以下文本扩写为原长度的" + ratio / 100 + "倍：" + text;
        return chatClient.prompt()
            .user(prompt)
            .stream()
            .content();
    }

    /**
     * AI缩写
     */
    @GetMapping(value = "ai/stream/condense", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamCondenseText(@RequestParam("text") String text, @RequestParam("ratio") Integer ratio) {
        String prompt = "请将以下文本缩写为原长度的" + 100 / ratio + "分之一：" + text;
        return chatClient.prompt()
            .user(prompt)
            .stream()
            .content();
    }

    /**
     * AI续写
     */
    @GetMapping(value = "ai/stream/continue", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamContinueText(@RequestParam("text") String text, @RequestParam("length") Integer length) {
        String prompt = "请续写以下文本，续写长度约为" + length + "字：" + text;
        return chatClient.prompt()
            .user(prompt)
            .stream()
            .content();
    }

    /**
     * AI润色
     */
    @GetMapping(value = "/ai/stream/polish", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamPolishText(@RequestParam("text") String text) {
        String prompt = "请润色优化以下文本，保持原意：" + text;
        return chatClient.prompt()
            .user(prompt)
            .stream()
            .content();
    }

}

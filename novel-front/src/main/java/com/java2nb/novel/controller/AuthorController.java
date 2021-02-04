package com.java2nb.novel.controller;

import com.github.pagehelper.PageInfo;
import com.java2nb.novel.core.bean.ResultBean;
import com.java2nb.novel.core.bean.UserDetails;
import com.java2nb.novel.core.enums.ResponseStatus;
import com.java2nb.novel.core.exception.BusinessException;
import com.java2nb.novel.core.utils.BeanUtil;
import com.java2nb.novel.entity.Author;
import com.java2nb.novel.entity.Book;
import com.java2nb.novel.service.AuthorService;
import com.java2nb.novel.service.BookService;
import com.java2nb.novel.service.FriendLinkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author 11797
 */
@RequestMapping("author")
@RestController
@Slf4j
@RequiredArgsConstructor
public class AuthorController extends BaseController{

    private final AuthorService authorService;

    private final BookService bookService;

    /**
     * 校验笔名是否存在
     * */
    @GetMapping("checkPenName")
    public ResultBean checkPenName(String penName){

        return ResultBean.ok(authorService.checkPenName(penName));
    }

    /**
     * 作家发布小说分页列表查询
     * */
    @GetMapping("listBookByPage")
    public ResultBean listBookByPage(@RequestParam(value = "curr", defaultValue = "1") int page, @RequestParam(value = "limit", defaultValue = "10") int pageSize ,HttpServletRequest request){

        return ResultBean.ok(bookService.listBookPageByUserId(getUserDetails(request).getId(),page,pageSize));
    }

    /**
     * 发布小说
     * */
    @PostMapping("addBook")
    public ResultBean addBook(@RequestParam("bookDesc") String bookDesc,Book book,HttpServletRequest request){

        Author author = checkAuthor(request);

        //bookDesc不能使用book对象来接收，否则会自动去掉前面的空格
        book.setBookDesc(bookDesc
                .replaceAll("\\n","<br>")
                .replaceAll("\\s","&nbsp;"));
        //发布小说
        bookService.addBook(book,author.getId(),author.getPenName());

        return ResultBean.ok();
    }

    /**
     * 更新小说状态,上架或下架
     * */
    @PostMapping("updateBookStatus")
    public ResultBean updateBookStatus(Long bookId,Byte status,HttpServletRequest request){
        Author author = checkAuthor(request);

        //更新小说状态,上架或下架
        bookService.updateBookStatus(bookId,status,author.getId());

        return ResultBean.ok();
    }



    /**
     * 删除章节
     */
    @DeleteMapping("deleteIndex/{indexId}")
    public ResultBean deleteIndex(@PathVariable("indexId") Long indexId,  HttpServletRequest request) {

        Author author = checkAuthor(request);

        //删除章节
        bookService.deleteIndex(indexId, author.getId());

        return ResultBean.ok();
    }

    /**
     * 更新章节名
     */
    @PostMapping("updateIndexName")
    public ResultBean updateIndexName(Long indexId,  String indexName, HttpServletRequest request) {

        Author author = checkAuthor(request);

        //更新章节名
        bookService.updateIndexName(indexId, indexName, author.getId());

        return ResultBean.ok();
    }




    /**
     * 发布章节内容
     */
    @PostMapping("addBookContent")
    public ResultBean addBookContent(Long bookId, String indexName, String content,Byte isVip, HttpServletRequest request) {
        Author author = checkAuthor(request);

        content = content.replaceAll("\\n", "<br>")
                .replaceAll("\\s", "&nbsp;");
        //发布章节内容
        bookService.addBookContent(bookId, indexName, content,isVip, author.getId());

        return ResultBean.ok();
    }

    /**
     * 查询章节内容
     */
    @GetMapping("queryIndexContent/{indexId}")
    public ResultBean queryIndexContent(@PathVariable("indexId") Long indexId,  HttpServletRequest request) {

        Author author = checkAuthor(request);

        String content = bookService.queryIndexContent(indexId, author.getId());

        content = content.replaceAll("<br>", "\n")
                .replaceAll("&nbsp;", " ");

        return ResultBean.ok(content);
    }

    /**
     * 更新章节内容
     */
    @PostMapping("updateBookContent")
    public ResultBean updateBookContent(Long indexId, String indexName, String content, HttpServletRequest request) {
        Author author = checkAuthor(request);

        content = content.replaceAll("\\n", "<br>")
                .replaceAll("\\s", "&nbsp;");
        //更新章节内容
        bookService.updateBookContent(indexId, indexName, content, author.getId());

        return ResultBean.ok();
    }

    /**
     * 作家日收入统计数据分页列表查询
     * */
    @GetMapping("listIncomeDailyByPage")
    public ResultBean listIncomeDailyByPage(@RequestParam(value = "curr", defaultValue = "1") int page,
                                            @RequestParam(value = "limit", defaultValue = "10") int pageSize ,
                                            @RequestParam(value = "bookId", defaultValue = "0") Long bookId,
                                            @RequestParam(value = "startTime",defaultValue = "2020-05-01") Date startTime,
                                            @RequestParam(value = "endTime",defaultValue = "2030-01-01") Date endTime,
                                            HttpServletRequest request){

        return ResultBean.ok(authorService.listIncomeDailyByPage(page,pageSize,getUserDetails(request).getId(),bookId,startTime,endTime));
    }


    /**
     * 作家月收入统计数据分页列表查询
     * */
    @GetMapping("listIncomeMonthByPage")
    public ResultBean listIncomeMonthByPage(@RequestParam(value = "curr", defaultValue = "1") int page,
                                            @RequestParam(value = "limit", defaultValue = "10") int pageSize ,
                                            @RequestParam(value = "bookId", defaultValue = "0") Long bookId,
                                            HttpServletRequest request){

        return ResultBean.ok(authorService.listIncomeMonthByPage(page,pageSize,getUserDetails(request).getId(),bookId));
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




}

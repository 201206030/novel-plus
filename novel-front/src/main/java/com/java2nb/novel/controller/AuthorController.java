package com.java2nb.novel.controller;

import com.github.pagehelper.PageInfo;
import com.java2nb.novel.core.bean.ResultBean;
import com.java2nb.novel.core.enums.ResponseStatus;
import com.java2nb.novel.core.utils.BeanUtil;
import com.java2nb.novel.entity.Author;
import com.java2nb.novel.entity.Book;
import com.java2nb.novel.service.AuthorService;
import com.java2nb.novel.service.BookService;
import com.java2nb.novel.service.FriendLinkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    @PostMapping("checkPenName")
    public ResultBean checkPenName(String penName){

        return ResultBean.ok(authorService.checkPenName(penName));
    }

    /**
     * 作家发布小说分页列表查询
     * */
    @PostMapping("listBookByPage")
    public ResultBean listBookByPage(@RequestParam(value = "curr", defaultValue = "1") int page, @RequestParam(value = "limit", defaultValue = "10") int pageSize ,HttpServletRequest request){

        return ResultBean.ok(new PageInfo<>(bookService.listBookPageByUserId(getUserDetails(request).getId(),page,pageSize)
        ));
    }

    /**
     * 发布小说
     * */
    @PostMapping("addBook")
    public ResultBean addBook(Book book,HttpServletRequest request){

        //查询作家信息
        Author author = authorService.queryAuthor(getUserDetails(request).getId());

        //判断作者状态是否正常
        if(author.getStatus()==1){
            //封禁状态，不能发布小说
            return ResultBean.fail(ResponseStatus.AUTHOR_STATUS_FORBIDDEN);

        }

        //发布小说
        bookService.addBook(book,author.getId(),author.getPenName());

        return ResultBean.ok();
    }

    /**
     * 更新小说状态,上架或下架
     * */
    @PostMapping("updateBookStatus")
    public ResultBean updateBookStatus(Long bookId,Byte status,HttpServletRequest request){
        //查询作家信息
        Author author = authorService.queryAuthor(getUserDetails(request).getId());

        //判断作者状态是否正常
        if(author.getStatus()==1){
            //封禁状态，不能发布小说
            return ResultBean.fail(ResponseStatus.AUTHOR_STATUS_FORBIDDEN);
        }

        //更新小说状态,上架或下架
        bookService.updateBookStatus(bookId,status,author.getId());

        return ResultBean.ok();
    }



    /**
     * 发布章节内容
     * */
    @PostMapping("addBookContent")
    public ResultBean addBookContent(Long bookId,String indexName,String content,HttpServletRequest request){
        //查询作家信息
        Author author = authorService.queryAuthor(getUserDetails(request).getId());

        //判断作者状态是否正常
        if(author.getStatus()==1){
            //封禁状态，不能发布小说
            return ResultBean.fail(ResponseStatus.AUTHOR_STATUS_FORBIDDEN);
        }

        //发布章节内容
        bookService.addBookContent(bookId,indexName,content,author.getId());

        return ResultBean.ok();
    }




}

package com.java2nb.novel.controller;

import com.java2nb.novel.core.bean.ResultBean;
import com.java2nb.novel.core.bean.UserDetails;
import com.java2nb.novel.core.utils.ThreadLocalUtil;
import com.java2nb.novel.entity.*;
import com.java2nb.novel.service.AuthorService;
import com.java2nb.novel.service.BookService;
import com.java2nb.novel.service.NewsService;
import com.java2nb.novel.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author 11797
 */
@Slf4j
@RequiredArgsConstructor
@Controller
public class PageController extends BaseController{

    private final BookService bookService;

    private final NewsService newsService;

    private final AuthorService authorService;

    private final UserService userService;


    @RequestMapping("{url}.html")
    public String module(@PathVariable("url") String url) {
        return url;
    }

    @RequestMapping("{module}/{url}.html")
    public String module2(@PathVariable("module") String module, @PathVariable("url") String url,HttpServletRequest request) {

        if(request.getRequestURI().startsWith("/author")) {
            //访问作者专区
            UserDetails user = getUserDetails(request);
            if (user == null) {
                //未登录
                return "redirect:/user/login.html?originUrl=" + request.getRequestURI();
            }

            boolean isAuthor = authorService.isAuthor(user.getId());
            if (!isAuthor) {
                return "redirect:/author/register.html" ;
            }
        }

        return module + "/" + url;
    }

    @RequestMapping("{module}/{classify}/{url}.html")
    public String module3(@PathVariable("module") String module, @PathVariable("classify") String classify, @PathVariable("url") String url) {
        return module + "/" + classify + "/" + url;
    }

    /**
     * 首页
     * */
    @RequestMapping(path = {"/", "/index", "/index.html"})
    public String index() {
        return ThreadLocalUtil.getTemplateDir()+"index";
    }

    /**
     * 作品页
     * */
    @RequestMapping("book/bookclass.html")
    public String bookClass() {
        return "book/bookclass";
    }

    /**
     * 排行页
     * */
    @RequestMapping("book/book_ranking.html")
    public String bookRank() {

        return ThreadLocalUtil.getTemplateDir()+"book/book_ranking";
    }


    /**
     * 详情页
     * */
    @RequestMapping("/book/{bookId}.html")
    public String bookDetail(@PathVariable("bookId") Long bookId, Model model) {
        Book book = bookService.queryBookDetail(bookId);
        model.addAttribute("book",book);
        if(book.getLastIndexId() != null) {
            //查询首章目录ID
            Long firstBookIndexId = bookService.queryFirstBookIndexId(bookId);
            model.addAttribute("firstBookIndexId", firstBookIndexId);
        }
        return ThreadLocalUtil.getTemplateDir()+"book/book_detail";
    }

    /**
     * 目录页
     * */
    @RequestMapping("/book/indexList-{bookId}.html")
    public String indexList(@PathVariable("bookId") Long bookId, Model model) {
        Book book = bookService.queryBookDetail(bookId);
        model.addAttribute("book",book);
        List<BookIndex> bookIndexList = bookService.queryIndexList(bookId,null,1,null);
        model.addAttribute("bookIndexList",bookIndexList);
        model.addAttribute("bookIndexCount",bookIndexList.size());
        return ThreadLocalUtil.getTemplateDir()+"book/book_index";
    }

    /**
     * 内容页
     * */
    @RequestMapping("/book/{bookId}/{bookIndexId}.html")
    public String indexList(@PathVariable("bookId") Long bookId,@PathVariable("bookIndexId") Long bookIndexId, HttpServletRequest request,Model model) {
        //查询书籍
        Book book = bookService.queryBookDetail(bookId);
        model.addAttribute("book",book);
        //查询目录
        BookIndex bookIndex = bookService.queryBookIndex(bookIndexId);
        model.addAttribute("bookIndex",bookIndex);
        //查询上一章节目录ID
        Long preBookIndexId = bookService.queryPreBookIndexId(bookId,bookIndex.getIndexNum());
        model.addAttribute("preBookIndexId",preBookIndexId);
        //查询下一章目录ID
        Long nextBookIndexId = bookService.queryNextBookIndexId(bookId,bookIndex.getIndexNum());
        model.addAttribute("nextBookIndexId",nextBookIndexId);
        //查询内容
        BookContent bookContent = bookService.queryBookContent(bookIndex.getId());
        model.addAttribute("bookContent",bookContent);

        boolean needBuy = false;
        //判断该目录是否收费
        if(bookIndex.getIsVip()!=null && bookIndex.getIsVip() == 1 ){
            UserDetails user = getUserDetails(request);
            if(user == null){
                //未登录
                return "redirect:/user/login.html?originUrl="+request.getRequestURI();
            }
            //收费，判断用户是否购买过该目录
            boolean isBuy = userService.queryIsBuyBookIndex(user.getId(),bookIndexId);
            if(!isBuy){
                //没有购买过，需要购买
                bookContent.setContent(null);
                needBuy = true;
            }
        }
        model.addAttribute("needBuy",needBuy);
        return ThreadLocalUtil.getTemplateDir()+"book/book_content";
    }

    /**
     * 评论页面
     * */
    @RequestMapping("/book/comment-{bookId}.html")
    public String commentList(@PathVariable("bookId") Long bookId, Model model) {
        //查询书籍
        Book book = bookService.queryBookDetail(bookId);
        model.addAttribute("book",book);
        return "book/book_comment";
    }

    /**
     * 新闻内容页面
     * */
    @RequestMapping("/about/newsInfo-{newsId}.html")
    public String newsInfo(@PathVariable("newsId") Long newsId, Model model) {
        //查询新闻
        News news = newsService.queryNewsInfo(newsId);
        model.addAttribute("news",news);
        return "about/news_info";
    }


    /**
     * 作者注册页面
     * */
    @RequestMapping("author/register.html")
    public String authorRegister(Author author, HttpServletRequest request, Model model){
        UserDetails user = getUserDetails(request);
        if(user == null){
            //未登录
            return "redirect:/user/login.html?originUrl=/author/register.html";
        }

        if(StringUtils.isNotBlank(author.getInviteCode())) {
            //提交作者注册信息
            String errorInfo = authorService.register(user.getId(), author);
            if(StringUtils.isBlank(errorInfo)){
                //注册成功
                return "redirect:/author/index.html";
            }
            model.addAttribute("LabErr",errorInfo);
            model.addAttribute("author",author);
        }
        return "author/register";
    }


}

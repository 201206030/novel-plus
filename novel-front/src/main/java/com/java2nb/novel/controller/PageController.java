package com.java2nb.novel.controller;

import com.java2nb.novel.core.utils.TemplateUtil;
import com.java2nb.novel.entity.Book;
import com.java2nb.novel.entity.BookContent;
import com.java2nb.novel.entity.BookIndex;
import com.java2nb.novel.entity.News;
import com.java2nb.novel.service.BookService;
import com.java2nb.novel.service.NewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author 11797
 */
@Slf4j
@RequiredArgsConstructor
@Controller
public class PageController{

    private final BookService bookService;

    private final NewsService newsService;


    @RequestMapping("{url}.html")
    public String module(@PathVariable("url") String url) {
        return url;
    }

    @RequestMapping("{module}/{url}.html")
    public String module2(@PathVariable("module") String module, @PathVariable("url") String url) {
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
        return TemplateUtil.getTemplateDir()+"index";
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

        return TemplateUtil.getTemplateDir()+"book/book_ranking";
    }


    /**
     * 详情页
     * */
    @RequestMapping("/book/{bookId}.html")
    public String bookDetail(@PathVariable("bookId") Long bookId, Model model) {
        Book book = bookService.queryBookDetail(bookId);
        model.addAttribute("book",book);
        //查询首章目录ID
        Long firstBookIndexId = bookService.queryFirstBookIndexId(bookId);
        model.addAttribute("firstBookIndexId",firstBookIndexId);
        return TemplateUtil.getTemplateDir()+"book/book_detail";
    }

    /**
     * 目录页
     * */
    @RequestMapping("/book/indexList-{bookId}.html")
    public String indexList(@PathVariable("bookId") Long bookId, Model model) {
        Book book = bookService.queryBookDetail(bookId);
        model.addAttribute("book",book);
        List<BookIndex> bookIndexList = bookService.queryIndexList(bookId,null,null);
        model.addAttribute("bookIndexList",bookIndexList);
        model.addAttribute("bookIndexCount",bookIndexList.size());
        return TemplateUtil.getTemplateDir()+"book/book_index";
    }

    /**
     * 内容页
     * */
    @RequestMapping("/book/{bookId}/{bookIndexId}.html")
    public String indexList(@PathVariable("bookId") Long bookId,@PathVariable("bookIndexId") Long bookIndexId, Model model) {
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
        return TemplateUtil.getTemplateDir()+"book/book_content";
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

}

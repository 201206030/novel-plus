package com.java2nb.novel.page;

import com.java2nb.novel.controller.BaseController;
import com.java2nb.novel.core.bean.UserDetails;
import com.java2nb.novel.core.utils.ThreadLocalUtil;
import com.java2nb.novel.entity.*;
import com.java2nb.novel.service.*;
import com.java2nb.novel.vo.BookCommentVO;
import com.java2nb.novel.vo.BookSettingVO;
import io.github.xxyopen.model.page.PageBean;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author 11797
 */
@Slf4j
@RequiredArgsConstructor
@Controller
public class PageController extends BaseController {

    @Value("${content.save.path}")
    private String fileSavePath;

    private final BookService bookService;

    private final NewsService newsService;

    private final AuthorService authorService;

    private final UserService userService;

    private final ThreadPoolExecutor threadPoolExecutor;

    private final Map<String, BookContentService> bookContentServiceMap;

    @RequestMapping("{url}.html")
    public String module(@PathVariable("url") String url) {
        return url;
    }

    @RequestMapping("{module}/{url}.html")
    public String module2(@PathVariable("module") String module, @PathVariable("url") String url, HttpServletRequest request) {

        if (request.getRequestURI().startsWith("/author")) {
            //访问作者专区
            UserDetails user = getUserDetails(request);
            if (user == null) {
                //未登录
                return "redirect:/user/login.html?originUrl=" + request.getRequestURI();
            }

            boolean isAuthor = authorService.isAuthor(user.getId());
            if (!isAuthor) {
                return "redirect:/author/register.html";
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
     */
    @SneakyThrows
    @RequestMapping(path = {"/", "/index", "/index.html"})
    public String index(Model model) {
        //加载小说首页小说基本信息线程
        CompletableFuture<Map<Byte, List<BookSettingVO>>> bookCompletableFuture = CompletableFuture.supplyAsync(bookService::listBookSettingVO, threadPoolExecutor);
        //加载首页新闻线程
        CompletableFuture<List<News>> newsCompletableFuture = CompletableFuture.supplyAsync(newsService::listIndexNews, threadPoolExecutor);
        model.addAttribute("bookMap", bookCompletableFuture.get());
        model.addAttribute("newsList", newsCompletableFuture.get());
        return ThreadLocalUtil.getTemplateDir() + "index";
    }

    /**
     * 作品页
     */
    @RequestMapping("book/bookclass.html")
    public String bookClass() {
        return "book/bookclass";
    }

    /**
     * 排行页
     */
    @RequestMapping("book/book_ranking.html")
    public String bookRank() {

        return ThreadLocalUtil.getTemplateDir() + "book/book_ranking";
    }


    /**
     * 详情页
     */
    @SneakyThrows
    @RequestMapping("/book/{bookId}.html")
    public String bookDetail(@PathVariable("bookId") Long bookId, Model model) {
        //加载小说基本信息线程
        CompletableFuture<Book> bookCompletableFuture = CompletableFuture.supplyAsync(() -> {
            //查询书籍
            Book book = bookService.queryBookDetail(bookId);
            log.debug("加载小说基本信息线程结束");
            return book;
        }, threadPoolExecutor);
        //加载小说评论列表线程
        CompletableFuture<PageBean<BookCommentVO>> bookCommentPageBeanCompletableFuture = CompletableFuture.supplyAsync(() -> {
            PageBean<BookCommentVO> bookCommentVOPageBean = bookService.listCommentByPage(null, bookId, 1, 5);
            log.debug("加载小说评论列表线程结束");
            return bookCommentVOPageBean;
        }, threadPoolExecutor);
        //加载小说首章信息线程，该线程在加载小说基本信息线程执行完毕后才执行
        CompletableFuture<Long> firstBookIndexIdCompletableFuture = bookCompletableFuture.thenApplyAsync((book) -> {
            if (book.getLastIndexId() != null) {
                //查询首章目录ID
                Long firstBookIndexId = bookService.queryFirstBookIndexId(bookId);
                log.debug("加载小说基本信息线程结束");
                return firstBookIndexId;
            }
            return null;
        }, threadPoolExecutor);
        //加载随机推荐小说线程，该线程在加载小说基本信息线程执行完毕后才执行
        CompletableFuture<List<Book>> recBookCompletableFuture = bookCompletableFuture.thenApplyAsync((book) -> {
            List<Book> books = bookService.listRecBookByCatId(book.getCatId());
            log.debug("加载随机推荐小说线程结束");
            return books;
        }, threadPoolExecutor);


        model.addAttribute("book", bookCompletableFuture.get());
        model.addAttribute("firstBookIndexId", firstBookIndexIdCompletableFuture.get());
        model.addAttribute("recBooks", recBookCompletableFuture.get());
        model.addAttribute("bookCommentPageBean", bookCommentPageBeanCompletableFuture.get());

        return ThreadLocalUtil.getTemplateDir() + "book/book_detail";
    }

    /**
     * 目录页
     */
    @SneakyThrows
    @RequestMapping("/book/indexList-{bookId}.html")
    public String indexList(@PathVariable("bookId") Long bookId, Model model) {
        Book book = bookService.queryBookDetail(bookId);
        model.addAttribute("book", book);
        List<BookIndex> bookIndexList = bookService.queryIndexList(bookId, null, 1, null);
        model.addAttribute("bookIndexList", bookIndexList);
        model.addAttribute("bookIndexCount", bookIndexList.size());
        return ThreadLocalUtil.getTemplateDir() + "book/book_index";
    }

    /**
     * 内容页
     */
    @SneakyThrows
    @RequestMapping("/book/{bookId}/{bookIndexId}.html")
    public String bookContent(@PathVariable("bookId") Long bookId, @PathVariable("bookIndexId") Long bookIndexId, HttpServletRequest request, Model model) {
        //加载小说基本信息线程
        CompletableFuture<Book> bookCompletableFuture = CompletableFuture.supplyAsync(() -> {
            //查询书籍
            Book book = bookService.queryBookDetail(bookId);
            log.debug("加载小说基本信息线程结束");
            return book;
        }, threadPoolExecutor);

        //加载小说章节信息线程
        CompletableFuture<BookIndex> bookIndexCompletableFuture = CompletableFuture.supplyAsync(() -> {
            //查询目录
            BookIndex bookIndex = bookService.queryBookIndex(bookIndexId);
            log.debug("加载小说章节信息线程结束");
            return bookIndex;
        }, threadPoolExecutor);

        //加载小说上一章节信息线程，该线程在加载小说章节信息线程执行完毕后才执行
        CompletableFuture<Long> preBookIndexIdCompletableFuture = bookIndexCompletableFuture.thenApplyAsync((bookIndex) -> {
            //查询上一章节目录ID
            Long preBookIndexId = bookService.queryPreBookIndexId(bookId, bookIndex.getIndexNum());
            log.debug("加载小说上一章节信息线程结束");
            return preBookIndexId;
        }, threadPoolExecutor);

        //加载小说下一章节信息线程，该线程在加载小说章节信息线程执行完毕后才执行
        CompletableFuture<Long> nextBookIndexIdCompletableFuture = bookIndexCompletableFuture.thenApplyAsync((bookIndex) -> {
            //查询下一章目录ID
            Long nextBookIndexId = bookService.queryNextBookIndexId(bookId, bookIndex.getIndexNum());
            log.debug("加载小说下一章节信息线程结束");
            return nextBookIndexId;
        }, threadPoolExecutor);

        //加载小说内容信息线程，该线程在加载小说章节信息线程执行完毕后才执行
        CompletableFuture<BookContent> bookContentCompletableFuture = bookIndexCompletableFuture.thenApplyAsync((bookIndex) -> {
            //查询内容
            BookContent bookContent = bookContentServiceMap.get(bookIndex.getStorageType()).queryBookContent(bookId, bookIndexId);
            log.debug("加载小说内容信息线程结束");
            return bookContent;
        }, threadPoolExecutor);


        //判断用户是否需要购买线程，该线程在加载小说章节信息线程执行完毕后才执行
        CompletableFuture<Boolean> needBuyCompletableFuture = bookIndexCompletableFuture.thenApplyAsync((bookIndex) -> {
            //判断该目录是否收费
            if (bookIndex.getIsVip() != null && bookIndex.getIsVip() == 1) {
                //收费
                UserDetails user = getUserDetails(request);
                if (user == null) {
                    //未登录，需要购买
                    return true;
                }
                //判断用户是否购买过该目录
                boolean isBuy = userService.queryIsBuyBookIndex(user.getId(), bookIndexId);
                if (!isBuy) {
                    //没有购买过，需要购买
                    return true;
                }
            }

            log.debug("判断用户是否需要购买线程结束");
            return false;

        }, threadPoolExecutor);


        model.addAttribute("book", bookCompletableFuture.get());
        model.addAttribute("bookIndex", bookIndexCompletableFuture.get());
        model.addAttribute("preBookIndexId", preBookIndexIdCompletableFuture.get());
        model.addAttribute("nextBookIndexId", nextBookIndexIdCompletableFuture.get());
        model.addAttribute("bookContent", bookContentCompletableFuture.get());
        model.addAttribute("needBuy", needBuyCompletableFuture.get());

        return ThreadLocalUtil.getTemplateDir() + "book/book_content";
    }

    /**
     * 评论页面
     */
    @RequestMapping("/book/comment-{bookId}.html")
    public String commentList(@PathVariable("bookId") Long bookId, Model model) {
        //查询书籍
        Book book = bookService.queryBookDetail(bookId);
        model.addAttribute("book", book);
        return "book/book_comment";
    }

    /**
     * 新闻内容页面
     */
    @RequestMapping("/about/newsInfo-{newsId}.html")
    public String newsInfo(@PathVariable("newsId") Long newsId, Model model) {
        //查询新闻
        News news = newsService.queryNewsInfo(newsId);
        model.addAttribute("news", news);
        return "about/news_info";
    }


    /**
     * 作者注册页面
     */
    @RequestMapping("author/register.html")
    public String authorRegister(Author author, HttpServletRequest request, Model model) {
        UserDetails user = getUserDetails(request);
        if (user == null) {
            //未登录
            return "redirect:/user/login.html?originUrl=/author/register.html";
        }

        if (StringUtils.isNotBlank(author.getInviteCode())) {
            //提交作者注册信息
            String errorInfo = authorService.register(user.getId(), author);
            if (StringUtils.isBlank(errorInfo)) {
                //注册成功
                return "redirect:/author/index.html";
            }
            model.addAttribute("LabErr", errorInfo);
            model.addAttribute("author", author);
        }
        return "author/register";
    }


}

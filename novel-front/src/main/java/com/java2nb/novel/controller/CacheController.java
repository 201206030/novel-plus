package com.java2nb.novel.controller;

import com.java2nb.novel.core.bean.ResultBean;
import com.java2nb.novel.core.cache.CacheKey;
import com.java2nb.novel.core.cache.CacheService;
import com.java2nb.novel.core.enums.ResponseStatus;
import com.java2nb.novel.service.BookService;
import com.java2nb.novel.service.FriendLinkService;
import com.java2nb.novel.service.NewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 11797
 */
@RequestMapping("cache")
@RestController
@Slf4j
@RequiredArgsConstructor
public class CacheController {

    @Value("${cache.manager.password}")
    private String cacheManagerPass;

    private final CacheService cacheService;

    private final BookService bookService;

    private final NewsService newsService;

    private final FriendLinkService friendLinkService;

    /**
     * 刷新缓存
     * @param type 缓存类型，1：首页书籍推荐，2：首页新闻，3：首页友情链接
     * */
    @GetMapping("refresh/{pass}/{type}")
    public ResultBean refreshCache(@PathVariable("type") Byte type, @PathVariable("pass") String pass){
        if(!cacheManagerPass.equals(pass)){
            return ResultBean.fail(ResponseStatus.PASSWORD_ERROR);
        }
        switch (type){
            case 1:{
                //刷新首页推荐书籍缓存
                cacheService.del(CacheKey.INDEX_BOOK_SETTINGS_KEY);
                bookService.listBookSettingVO();
                break;
            }
            case 2:{
                //刷新首页新闻缓存
                cacheService.del(CacheKey.INDEX_NEWS_KEY);
                newsService.listIndexNews();
                break;
            }
            case 3:{
                //刷新首页友情链接
                cacheService.del(CacheKey.INDEX_LINK_KEY);
                friendLinkService.listIndexLink();
                break;
            }
            default:{
                break;
            }

        }

        return ResultBean.ok();
    }




}

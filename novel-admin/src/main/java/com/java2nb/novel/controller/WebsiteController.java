package com.java2nb.novel.controller;

import com.java2nb.novel.entity.Book;
import com.java2nb.novel.entity.BookSetting;
import com.java2nb.novel.entity.FriendLink;
import com.java2nb.novel.entity.WebsiteInfo;
import com.java2nb.novel.service.WebsiteService;
import com.java2nb.novel.vo.AdminBookSettingVO;
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
 * 网站管理Controller：网站信息/友情链接/小说推荐
 *
 * @author Administrator
 */
@RestController
@RequestMapping("website")
@RequiredArgsConstructor
public class WebsiteController {

    private final WebsiteService websiteService;

    @GetMapping("websiteInfo/get")
    public RestResult<WebsiteInfo> getWebsiteInfo() {
        return RestResult.ok(websiteService.getWebsiteInfo());
    }

    @PostMapping("websiteInfo/update")
    public RestResult<Void> updateWebsiteInfo(WebsiteInfo websiteInfo) {
        websiteService.updateWebsiteInfo(websiteInfo);
        return RestResult.ok();
    }

    @GetMapping("friendLink/list")
    public RestResult<PageBean<FriendLink>> friendLinkList(@RequestParam(required = false) String keyword,
        @RequestParam(value = "curr", defaultValue = "1") int page,
        @RequestParam(value = "limit", defaultValue = "10") int pageSize) {
        return RestResult.ok(websiteService.friendLinkList(keyword, page, pageSize));
    }

    @GetMapping("friendLink/get")
    public RestResult<FriendLink> friendLinkGet(@RequestParam Integer id) {
        return RestResult.ok(websiteService.friendLinkGet(id));
    }

    @PostMapping("friendLink/save")
    public RestResult<Void> friendLinkSave(FriendLink friendLink) {
        websiteService.friendLinkSave(friendLink);
        return RestResult.ok();
    }

    @PostMapping("friendLink/update")
    public RestResult<Void> friendLinkUpdate(FriendLink friendLink) {
        websiteService.friendLinkUpdate(friendLink);
        return RestResult.ok();
    }

    @PostMapping("friendLink/remove")
    public RestResult<Void> friendLinkRemove(@RequestParam Integer id) {
        websiteService.friendLinkRemove(id);
        return RestResult.ok();
    }

    @PostMapping("friendLink/batchRemove")
    public RestResult<Void> friendLinkBatchRemove(@RequestParam String ids) {
        websiteService.friendLinkBatchRemove(ids);
        return RestResult.ok();
    }

    @GetMapping("bookSetting/list")
    public RestResult<PageBean<AdminBookSettingVO>> settingList(@RequestParam(required = false) Byte type,
        @RequestParam(value = "curr", defaultValue = "1") int page,
        @RequestParam(value = "limit", defaultValue = "10") int pageSize) {
        return RestResult.ok(websiteService.settingList(type, page, pageSize));
    }

    @GetMapping("bookSetting/get")
    public RestResult<BookSetting> settingGet(@RequestParam Long id) {
        return RestResult.ok(websiteService.settingGet(id));
    }

    @PostMapping("bookSetting/save")
    public RestResult<Void> settingSave(BookSetting bookSetting) {
        websiteService.settingSave(bookSetting);
        return RestResult.ok();
    }

    @PostMapping("bookSetting/update")
    public RestResult<Void> settingUpdate(BookSetting bookSetting) {
        websiteService.settingUpdate(bookSetting);
        return RestResult.ok();
    }

    @PostMapping("bookSetting/remove")
    public RestResult<Void> settingRemove(@RequestParam Long id) {
        websiteService.settingRemove(id);
        return RestResult.ok();
    }

    @PostMapping("bookSetting/batchRemove")
    public RestResult<Void> settingBatchRemove(@RequestParam String ids) {
        websiteService.settingBatchRemove(ids);
        return RestResult.ok();
    }

    /**
     * 书籍简化列表（小说推荐选择书籍用）
     */
    @GetMapping("bookSetting/books")
    public RestResult<List<Book>> simpleBookList(@RequestParam(required = false) String keyword) {
        return RestResult.ok(websiteService.simpleBookList(keyword));
    }

}

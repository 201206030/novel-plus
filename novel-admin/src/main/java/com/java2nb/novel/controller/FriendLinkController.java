package com.java2nb.novel.controller;

import com.java2nb.common.config.CacheKey;
import com.java2nb.common.utils.PageBean;
import com.java2nb.common.utils.Query;
import com.java2nb.common.utils.R;
import com.java2nb.novel.domain.FriendLinkDO;
import com.java2nb.novel.service.FriendLinkService;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author xiongxy
 * @email 1179705413@qq.com
 * @date 2023-04-14 15:12:25
 */

@Controller
@RequestMapping("/novel/friendLink")
public class FriendLinkController {

    @Autowired
    private FriendLinkService friendLinkService;
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @GetMapping()
    @RequiresPermissions("novel:friendLink:friendLink")
    String FriendLink() {
        return "novel/friendLink/friendLink";
    }

    @ApiOperation(value = "获取列表", notes = "获取列表")
    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("novel:friendLink:friendLink")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        List<FriendLinkDO> friendLinkList = friendLinkService.list(query);
        int total = friendLinkService.count(query);
        PageBean pageBean = new PageBean(friendLinkList, total);
        return R.ok().put("data", pageBean);
    }

    @ApiOperation(value = "新增页面", notes = "新增页面")
    @GetMapping("/add")
    @RequiresPermissions("novel:friendLink:add")
    String add() {
        return "novel/friendLink/add";
    }

    @ApiOperation(value = "修改页面", notes = "修改页面")
    @GetMapping("/edit/{id}")
    @RequiresPermissions("novel:friendLink:edit")
    String edit(@PathVariable("id") Integer id, Model model) {
        FriendLinkDO friendLink = friendLinkService.get(id);
        model.addAttribute("friendLink", friendLink);
        return "novel/friendLink/edit";
    }

    @ApiOperation(value = "查看页面", notes = "查看页面")
    @GetMapping("/detail/{id}")
    @RequiresPermissions("novel:friendLink:detail")
    String detail(@PathVariable("id") Integer id, Model model) {
        FriendLinkDO friendLink = friendLinkService.get(id);
        model.addAttribute("friendLink", friendLink);
        return "novel/friendLink/detail";
    }

    /**
     * 保存
     */
    @ApiOperation(value = "新增", notes = "新增")
    @ResponseBody
    @PostMapping("/save")
    @RequiresPermissions("novel:friendLink:add")
    public R save(FriendLinkDO friendLink) {
        if (friendLinkService.save(friendLink) > 0) {
            redisTemplate.delete(CacheKey.INDEX_LINK_KEY);
            return R.ok();
        }
        return R.error();
    }

    /**
     * 修改
     */
    @ApiOperation(value = "修改", notes = "修改")
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("novel:friendLink:edit")
    public R update(FriendLinkDO friendLink) {
        friendLinkService.update(friendLink);
        redisTemplate.delete(CacheKey.INDEX_LINK_KEY);
        return R.ok();
    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除", notes = "删除")
    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("novel:friendLink:remove")
    public R remove(Integer id) {
        if (friendLinkService.remove(id) > 0) {
            redisTemplate.delete(CacheKey.INDEX_LINK_KEY);
            return R.ok();
        }
        return R.error();
    }

    /**
     * 删除
     */
    @ApiOperation(value = "批量删除", notes = "批量删除")
    @PostMapping("/batchRemove")
    @ResponseBody
    @RequiresPermissions("novel:friendLink:batchRemove")
    public R remove(@RequestParam("ids[]") Integer[] ids) {
        friendLinkService.batchRemove(ids);
        redisTemplate.delete(CacheKey.INDEX_LINK_KEY);
        return R.ok();
    }

}

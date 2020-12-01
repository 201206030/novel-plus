package com.java2nb.novel.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import io.swagger.annotations.ApiOperation;


import com.java2nb.novel.domain.NewsDO;
import com.java2nb.novel.service.NewsService;
import com.java2nb.common.utils.PageBean;
import com.java2nb.common.utils.Query;
import com.java2nb.common.utils.R;

/**
 * 新闻表
 *
 * @author xiongxy
 * @email 1179705413@qq.com
 * @date 2020-12-01 10:05:51
 */

@Controller
@RequestMapping("/novel/news")
public class NewsController {
    @Autowired
    private NewsService newsService;

    @GetMapping()
    @RequiresPermissions("novel:news:news")
    String News() {
        return "novel/news/news";
    }

    @ApiOperation(value = "获取新闻表列表", notes = "获取新闻表列表")
    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("novel:news:news")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        List<NewsDO> newsList = newsService.list(query);
        int total = newsService.count(query);
        PageBean pageBean = new PageBean(newsList, total);
        return R.ok().put("data", pageBean);
    }

    @ApiOperation(value = "新增新闻表页面", notes = "新增新闻表页面")
    @GetMapping("/add")
    @RequiresPermissions("novel:news:add")
    String add() {
        return "novel/news/add";
    }

    @ApiOperation(value = "修改新闻表页面", notes = "修改新闻表页面")
    @GetMapping("/edit/{id}")
    @RequiresPermissions("novel:news:edit")
    String edit(@PathVariable("id") Long id, Model model) {
            NewsDO news = newsService.get(id);
        model.addAttribute("news", news);
        return "novel/news/edit";
    }

    @ApiOperation(value = "查看新闻表页面", notes = "查看新闻表页面")
    @GetMapping("/detail/{id}")
    @RequiresPermissions("novel:news:detail")
    String detail(@PathVariable("id") Long id, Model model) {
			NewsDO news = newsService.get(id);
        model.addAttribute("news", news);
        return "novel/news/detail";
    }

    /**
     * 保存
     */
    @ApiOperation(value = "新增新闻表", notes = "新增新闻表")
    @ResponseBody
    @PostMapping("/save")
    @RequiresPermissions("novel:news:add")
    public R save( NewsDO news) {
        if (newsService.save(news) > 0) {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 修改
     */
    @ApiOperation(value = "修改新闻表", notes = "修改新闻表")
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("novel:news:edit")
    public R update( NewsDO news) {
            newsService.update(news);
        return R.ok();
    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除新闻表", notes = "删除新闻表")
    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("novel:news:remove")
    public R remove( Long id) {
        if (newsService.remove(id) > 0) {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 删除
     */
    @ApiOperation(value = "批量删除新闻表", notes = "批量删除新闻表")
    @PostMapping("/batchRemove")
    @ResponseBody
    @RequiresPermissions("novel:news:batchRemove")
    public R remove(@RequestParam("ids[]") Long[] ids) {
            newsService.batchRemove(ids);
        return R.ok();
    }

}

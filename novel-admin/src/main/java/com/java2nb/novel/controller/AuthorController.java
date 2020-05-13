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


import com.java2nb.novel.domain.AuthorDO;
import com.java2nb.novel.service.AuthorService;
import com.java2nb.common.utils.PageBean;
import com.java2nb.common.utils.Query;
import com.java2nb.common.utils.R;

/**
 * 作者表
 *
 * @author xiongxy
 * @email 1179705413@qq.com
 * @date 2020-05-13 11:16:51
 */

@Controller
@RequestMapping("/novel/author")
public class AuthorController {
    @Autowired
    private AuthorService authorService;

    @GetMapping()
    @RequiresPermissions("novel:author:author")
    String Author() {
        return "novel/author/author";
    }

    @ApiOperation(value = "获取作者表列表", notes = "获取作者表列表")
    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("novel:author:author")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        List<AuthorDO> authorList = authorService.list(query);
        int total = authorService.count(query);
        PageBean pageBean = new PageBean(authorList, total);
        return R.ok().put("data", pageBean);
    }

    @ApiOperation(value = "新增作者表页面", notes = "新增作者表页面")
    @GetMapping("/add")
    @RequiresPermissions("novel:author:add")
    String add() {
        return "novel/author/add";
    }

    @ApiOperation(value = "修改作者表页面", notes = "修改作者表页面")
    @GetMapping("/edit/{id}")
    @RequiresPermissions("novel:author:edit")
    String edit(@PathVariable("id") Long id, Model model) {
            AuthorDO author = authorService.get(id);
        model.addAttribute("author", author);
        return "novel/author/edit";
    }

    @ApiOperation(value = "查看作者表页面", notes = "查看作者表页面")
    @GetMapping("/detail/{id}")
    @RequiresPermissions("novel:author:detail")
    String detail(@PathVariable("id") Long id, Model model) {
			AuthorDO author = authorService.get(id);
        model.addAttribute("author", author);
        return "novel/author/detail";
    }

    /**
     * 保存
     */
    @ApiOperation(value = "新增作者表", notes = "新增作者表")
    @ResponseBody
    @PostMapping("/save")
    @RequiresPermissions("novel:author:add")
    public R save( AuthorDO author) {
        if (authorService.save(author) > 0) {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 修改
     */
    @ApiOperation(value = "修改作者表", notes = "修改作者表")
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("novel:author:edit")
    public R update( AuthorDO author) {
            authorService.update(author);
        return R.ok();
    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除作者表", notes = "删除作者表")
    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("novel:author:remove")
    public R remove( Long id) {
        if (authorService.remove(id) > 0) {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 删除
     */
    @ApiOperation(value = "批量删除作者表", notes = "批量删除作者表")
    @PostMapping("/batchRemove")
    @ResponseBody
    @RequiresPermissions("novel:author:batchRemove")
    public R remove(@RequestParam("ids[]") Long[] ids) {
            authorService.batchRemove(ids);
        return R.ok();
    }

}

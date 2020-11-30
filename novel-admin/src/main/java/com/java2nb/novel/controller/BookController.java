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


import com.java2nb.novel.domain.BookDO;
import com.java2nb.novel.service.BookService;
import com.java2nb.common.utils.PageBean;
import com.java2nb.common.utils.Query;
import com.java2nb.common.utils.R;

/**
 * 小说表
 *
 * @author xiongxy
 * @email 1179705413@qq.com
 * @date 2020-12-01 03:49:46
 */

@Controller
@RequestMapping("/novel/book")
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping()
    @RequiresPermissions("novel:book:book")
    String Book() {
        return "novel/book/book";
    }

    @ApiOperation(value = "获取小说表列表", notes = "获取小说表列表")
    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("novel:book:book")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        List<BookDO> bookList = bookService.list(query);
        int total = bookService.count(query);
        PageBean pageBean = new PageBean(bookList, total);
        return R.ok().put("data", pageBean);
    }

    @ApiOperation(value = "新增小说表页面", notes = "新增小说表页面")
    @GetMapping("/add")
    @RequiresPermissions("novel:book:add")
    String add() {
        return "novel/book/add";
    }

    @ApiOperation(value = "修改小说表页面", notes = "修改小说表页面")
    @GetMapping("/edit/{id}")
    @RequiresPermissions("novel:book:edit")
    String edit(@PathVariable("id") Long id, Model model) {
            BookDO book = bookService.get(id);
        model.addAttribute("book", book);
        return "novel/book/edit";
    }

    @ApiOperation(value = "查看小说表页面", notes = "查看小说表页面")
    @GetMapping("/detail/{id}")
    @RequiresPermissions("novel:book:detail")
    String detail(@PathVariable("id") Long id, Model model) {
			BookDO book = bookService.get(id);
        model.addAttribute("book", book);
        return "novel/book/detail";
    }

    /**
     * 保存
     */
    @ApiOperation(value = "新增小说表", notes = "新增小说表")
    @ResponseBody
    @PostMapping("/save")
    @RequiresPermissions("novel:book:add")
    public R save( BookDO book) {
        if (bookService.save(book) > 0) {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 修改
     */
    @ApiOperation(value = "修改小说表", notes = "修改小说表")
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("novel:book:edit")
    public R update( BookDO book) {
            bookService.update(book);
        return R.ok();
    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除小说表", notes = "删除小说表")
    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("novel:book:remove")
    public R remove( Long id) {
        if (bookService.remove(id) > 0) {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 删除
     */
    @ApiOperation(value = "批量删除小说表", notes = "批量删除小说表")
    @PostMapping("/batchRemove")
    @ResponseBody
    @RequiresPermissions("novel:book:batchRemove")
    public R remove(@RequestParam("ids[]") Long[] ids) {
            bookService.batchRemove(ids);
        return R.ok();
    }

}

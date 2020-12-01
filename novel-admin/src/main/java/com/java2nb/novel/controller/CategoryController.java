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


import com.java2nb.novel.domain.CategoryDO;
import com.java2nb.novel.service.CategoryService;
import com.java2nb.common.utils.PageBean;
import com.java2nb.common.utils.Query;
import com.java2nb.common.utils.R;

/**
 * 新闻类别表
 *
 * @author xiongxy
 * @email 1179705413@qq.com
 * @date 2020-12-01 10:03:41
 */

@Controller
@RequestMapping("/novel/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping()
    @RequiresPermissions("novel:category:category")
    String Category() {
        return "novel/category/category";
    }

    @ApiOperation(value = "获取新闻类别表列表", notes = "获取新闻类别表列表")
    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("novel:category:category")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        List<CategoryDO> categoryList = categoryService.list(query);
        int total = categoryService.count(query);
        PageBean pageBean = new PageBean(categoryList, total);
        return R.ok().put("data", pageBean);
    }

    @ApiOperation(value = "新增新闻类别表页面", notes = "新增新闻类别表页面")
    @GetMapping("/add")
    @RequiresPermissions("novel:category:add")
    String add() {
        return "novel/category/add";
    }

    @ApiOperation(value = "修改新闻类别表页面", notes = "修改新闻类别表页面")
    @GetMapping("/edit/{id}")
    @RequiresPermissions("novel:category:edit")
    String edit(@PathVariable("id") Integer id, Model model) {
            CategoryDO category = categoryService.get(id);
        model.addAttribute("category", category);
        return "novel/category/edit";
    }

    @ApiOperation(value = "查看新闻类别表页面", notes = "查看新闻类别表页面")
    @GetMapping("/detail/{id}")
    @RequiresPermissions("novel:category:detail")
    String detail(@PathVariable("id") Integer id, Model model) {
			CategoryDO category = categoryService.get(id);
        model.addAttribute("category", category);
        return "novel/category/detail";
    }

    /**
     * 保存
     */
    @ApiOperation(value = "新增新闻类别表", notes = "新增新闻类别表")
    @ResponseBody
    @PostMapping("/save")
    @RequiresPermissions("novel:category:add")
    public R save( CategoryDO category) {
        if (categoryService.save(category) > 0) {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 修改
     */
    @ApiOperation(value = "修改新闻类别表", notes = "修改新闻类别表")
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("novel:category:edit")
    public R update( CategoryDO category) {
            categoryService.update(category);
        return R.ok();
    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除新闻类别表", notes = "删除新闻类别表")
    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("novel:category:remove")
    public R remove( Integer id) {
        if (categoryService.remove(id) > 0) {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 删除
     */
    @ApiOperation(value = "批量删除新闻类别表", notes = "批量删除新闻类别表")
    @PostMapping("/batchRemove")
    @ResponseBody
    @RequiresPermissions("novel:category:batchRemove")
    public R remove(@RequestParam("ids[]") Integer[] ids) {
            categoryService.batchRemove(ids);
        return R.ok();
    }

}

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


import com.java2nb.novel.domain.AuthorCodeDO;
import com.java2nb.novel.service.AuthorCodeService;
import com.java2nb.common.utils.PageBean;
import com.java2nb.common.utils.Query;
import com.java2nb.common.utils.R;

/**
 * 作家邀请码表
 *
 * @author xiongxy
 * @email 1179705413@qq.com
 * @date 2020-05-13 11:29:15
 */

@Controller
@RequestMapping("/novel/authorCode")
public class AuthorCodeController {
    @Autowired
    private AuthorCodeService authorCodeService;

    @GetMapping()
    @RequiresPermissions("novel:authorCode:authorCode")
    String AuthorCode() {
        return "novel/authorCode/authorCode";
    }

    @ApiOperation(value = "获取作家邀请码表列表", notes = "获取作家邀请码表列表")
    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("novel:authorCode:authorCode")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        List<AuthorCodeDO> authorCodeList = authorCodeService.list(query);
        int total = authorCodeService.count(query);
        PageBean pageBean = new PageBean(authorCodeList, total);
        return R.ok().put("data", pageBean);
    }

    @ApiOperation(value = "新增作家邀请码表页面", notes = "新增作家邀请码表页面")
    @GetMapping("/add")
    @RequiresPermissions("novel:authorCode:add")
    String add() {
        return "novel/authorCode/add";
    }

    @ApiOperation(value = "修改作家邀请码表页面", notes = "修改作家邀请码表页面")
    @GetMapping("/edit/{id}")
    @RequiresPermissions("novel:authorCode:edit")
    String edit(@PathVariable("id") Long id, Model model) {
            AuthorCodeDO authorCode = authorCodeService.get(id);
        model.addAttribute("authorCode", authorCode);
        return "novel/authorCode/edit";
    }

    @ApiOperation(value = "查看作家邀请码表页面", notes = "查看作家邀请码表页面")
    @GetMapping("/detail/{id}")
    @RequiresPermissions("novel:authorCode:detail")
    String detail(@PathVariable("id") Long id, Model model) {
			AuthorCodeDO authorCode = authorCodeService.get(id);
        model.addAttribute("authorCode", authorCode);
        return "novel/authorCode/detail";
    }

    /**
     * 保存
     */
    @ApiOperation(value = "新增作家邀请码表", notes = "新增作家邀请码表")
    @ResponseBody
    @PostMapping("/save")
    @RequiresPermissions("novel:authorCode:add")
    public R save( AuthorCodeDO authorCode) {
        if (authorCodeService.save(authorCode) > 0) {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 修改
     */
    @ApiOperation(value = "修改作家邀请码表", notes = "修改作家邀请码表")
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("novel:authorCode:edit")
    public R update( AuthorCodeDO authorCode) {
            authorCodeService.update(authorCode);
        return R.ok();
    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除作家邀请码表", notes = "删除作家邀请码表")
    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("novel:authorCode:remove")
    public R remove( Long id) {
        if (authorCodeService.remove(id) > 0) {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 删除
     */
    @ApiOperation(value = "批量删除作家邀请码表", notes = "批量删除作家邀请码表")
    @PostMapping("/batchRemove")
    @ResponseBody
    @RequiresPermissions("novel:authorCode:batchRemove")
    public R remove(@RequestParam("ids[]") Long[] ids) {
            authorCodeService.batchRemove(ids);
        return R.ok();
    }

}

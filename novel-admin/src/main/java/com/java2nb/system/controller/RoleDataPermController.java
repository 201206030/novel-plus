package com.java2nb.system.controller;

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


import com.java2nb.system.domain.RoleDataPermDO;
import com.java2nb.system.service.RoleDataPermService;
import com.java2nb.common.utils.PageBean;
import com.java2nb.common.utils.Query;
import com.java2nb.common.utils.R;

/**
 * 角色与数据权限对应关系
 *
 * @author xiongxy
 * @email 1179705413@qq.com
 * @date 2019-11-25 11:32:49
 */

@Controller
@RequestMapping("/system/roleDataPerm")
public class RoleDataPermController {
    @Autowired
    private RoleDataPermService roleDataPermService;

    @GetMapping()
    @RequiresPermissions("system:roleDataPerm:roleDataPerm")
    String RoleDataPerm() {
        return "system/roleDataPerm/roleDataPerm";
    }

    @ApiOperation(value = "获取角色与数据权限对应关系列表", notes = "获取角色与数据权限对应关系列表")
    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("system:roleDataPerm:roleDataPerm")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        List<RoleDataPermDO> roleDataPermList = roleDataPermService.list(query);
        int total = roleDataPermService.count(query);
        PageBean pageBean = new PageBean(roleDataPermList, total);
        return R.ok().put("data", pageBean);
    }

    @ApiOperation(value = "新增角色与数据权限对应关系页面", notes = "新增角色与数据权限对应关系页面")
    @GetMapping("/add")
    @RequiresPermissions("system:roleDataPerm:add")
    String add() {
        return "system/roleDataPerm/add";
    }

    @ApiOperation(value = "修改角色与数据权限对应关系页面", notes = "修改角色与数据权限对应关系页面")
    @GetMapping("/edit/{id}")
    @RequiresPermissions("system:roleDataPerm:edit")
    String edit(@PathVariable("id") Long id, Model model) {
            RoleDataPermDO roleDataPerm = roleDataPermService.get(id);
        model.addAttribute("roleDataPerm", roleDataPerm);
        return "system/roleDataPerm/edit";
    }

    @ApiOperation(value = "查看角色与数据权限对应关系页面", notes = "查看角色与数据权限对应关系页面")
    @GetMapping("/detail/{id}")
    @RequiresPermissions("system:roleDataPerm:detail")
    String detail(@PathVariable("id") Long id, Model model) {
			RoleDataPermDO roleDataPerm = roleDataPermService.get(id);
        model.addAttribute("roleDataPerm", roleDataPerm);
        return "system/roleDataPerm/detail";
    }

    /**
     * 保存
     */
    @ApiOperation(value = "新增角色与数据权限对应关系", notes = "新增角色与数据权限对应关系")
    @ResponseBody
    @PostMapping("/save")
    @RequiresPermissions("system:roleDataPerm:add")
    public R save( RoleDataPermDO roleDataPerm) {
        if (roleDataPermService.save(roleDataPerm) > 0) {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 修改
     */
    @ApiOperation(value = "修改角色与数据权限对应关系", notes = "修改角色与数据权限对应关系")
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("system:roleDataPerm:edit")
    public R update( RoleDataPermDO roleDataPerm) {
            roleDataPermService.update(roleDataPerm);
        return R.ok();
    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除角色与数据权限对应关系", notes = "删除角色与数据权限对应关系")
    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("system:roleDataPerm:remove")
    public R remove( Long id) {
        if (roleDataPermService.remove(id) > 0) {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 删除
     */
    @ApiOperation(value = "批量删除角色与数据权限对应关系", notes = "批量删除角色与数据权限对应关系")
    @PostMapping("/batchRemove")
    @ResponseBody
    @RequiresPermissions("system:roleDataPerm:batchRemove")
    public R remove(@RequestParam("ids[]") Long[] ids) {
            roleDataPermService.batchRemove(ids);
        return R.ok();
    }

}

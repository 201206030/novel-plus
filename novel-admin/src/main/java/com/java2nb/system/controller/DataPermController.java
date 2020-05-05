package com.java2nb.system.controller;

import java.util.List;
import java.util.Map;

import com.java2nb.common.domain.DictDO;
import com.java2nb.common.domain.Tree;
import com.java2nb.system.domain.MenuDO;
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


import com.java2nb.system.domain.DataPermDO;
import com.java2nb.system.service.DataPermService;
import com.java2nb.common.utils.PageBean;
import com.java2nb.common.utils.Query;
import com.java2nb.common.utils.R;

/**
 * 数据权限管理
 *
 * @author xiongxy
 * @email 1179705413@qq.com
 * @date 2019-11-25 11:40:03
 */

@Controller
@RequestMapping("/system/dataPerm")
public class DataPermController {
    @Autowired
    private DataPermService dataPermService;

    @GetMapping()
    @RequiresPermissions("system:dataPerm:dataPerm")
    String DataPerm() {
        return "system/dataPerm/dataPerm";
    }

    @ApiOperation(value = "获取数据权限管理列表", notes = "获取数据权限管理列表")
    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("system:dataPerm:dataPerm")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        List<DataPermDO> dataPermList = dataPermService.list(query);
        int total = dataPermService.count(query);
        PageBean pageBean = new PageBean(dataPermList, total);
        return R.ok().put("data", pageBean);
    }

    @ApiOperation(value = "新增数据权限管理页面", notes = "新增数据权限管理页面")
    @GetMapping("/add")
    @RequiresPermissions("system:dataPerm:add")
    String add() {
        return "system/dataPerm/add";
    }

    @ApiOperation(value = "修改数据权限管理页面", notes = "修改数据权限管理页面")
    @GetMapping("/edit/{id}")
    @RequiresPermissions("system:dataPerm:edit")
    String edit(@PathVariable("id") Long id, Model model) {
            DataPermDO dataPerm = dataPermService.get(id);
        model.addAttribute("dataPerm", dataPerm);
        return "system/dataPerm/edit";
    }

    @ApiOperation(value = "查看数据权限管理页面", notes = "查看数据权限管理页面")
    @GetMapping("/detail/{id}")
    @RequiresPermissions("system:dataPerm:detail")
    String detail(@PathVariable("id") Long id, Model model) {
			DataPermDO dataPerm = dataPermService.get(id);
        model.addAttribute("dataPerm", dataPerm);
        return "system/dataPerm/detail";
    }

    /**
     * 保存
     */
    @ApiOperation(value = "新增数据权限管理", notes = "新增数据权限管理")
    @ResponseBody
    @PostMapping("/save")
    @RequiresPermissions("system:dataPerm:add")
    public R save( DataPermDO dataPerm) {
        if (dataPermService.save(dataPerm) > 0) {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 修改
     */
    @ApiOperation(value = "修改数据权限管理", notes = "修改数据权限管理")
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("system:dataPerm:edit")
    public R update( DataPermDO dataPerm) {
            dataPermService.update(dataPerm);
        return R.ok();
    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除数据权限管理", notes = "删除数据权限管理")
    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("system:dataPerm:remove")
    public R remove( Long id) {
        if (dataPermService.remove(id) > 0) {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 删除
     */
    @ApiOperation(value = "批量删除数据权限管理", notes = "批量删除数据权限管理")
    @PostMapping("/batchRemove")
    @ResponseBody
    @RequiresPermissions("system:dataPerm:batchRemove")
    public R remove(@RequestParam("ids[]") Long[] ids) {
            dataPermService.batchRemove(ids);
        return R.ok();
    }


    @GetMapping("/moduleName")
    @ResponseBody
    public List<DataPermDO> listModuleName() {
        return dataPermService.listModuleName();
    };


    @GetMapping("/tree")
    @ResponseBody
    Tree<DataPermDO> tree() {
        Tree<DataPermDO>  tree = dataPermService.getTree();
        return tree;
    }

    @GetMapping("/tree/{roleId}")
    @ResponseBody
    Tree<DataPermDO> tree(@PathVariable("roleId") Long roleId) {
        Tree<DataPermDO>  tree = dataPermService.getTree(roleId);
        return tree;
    }

}

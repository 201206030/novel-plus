package com.java2nb.common.controller;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.java2nb.common.domain.GenColumnsDO;
import com.java2nb.common.service.GeneratorService;
import com.java2nb.common.utils.GenUtils;
import com.java2nb.common.utils.PageBean;
import com.java2nb.common.utils.R;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/common/generator")
@Controller
public class GeneratorController {
    String prefix = "common/generator";
    @Autowired
    GeneratorService generatorService;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping()
    String generator() {
        return prefix + "/list";
    }

    @ResponseBody
    @GetMapping("/list")
    List<Map<String, Object>> list(String tableName) {
        List<Map<String, Object>> list = generatorService.list(tableName);
        return list;
    }

    ;

    @RequestMapping("/downLoadCode/{tableName}")
    public void downLoadCode(HttpServletRequest request, HttpServletResponse response,
                             @PathVariable("tableName") String tableName) throws IOException {
        String[] tableNames = new String[]{tableName};
        byte[] data = generatorService.downloadCode(tableNames);
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"java2nb.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");

        IOUtils.write(data, response.getOutputStream());
    }

    @ResponseBody
    @PostMapping("/genCode")
    public R genCode(String tableName) {
        String[] tableNames = new String[]{tableName};
        generatorService.generatorCode(tableNames);
        return R.ok("代码生成成功，请到本地项目中查看！");
    }

    @RequestMapping("/batchDownload")
    public void batchDownload(HttpServletRequest request, HttpServletResponse response, String tables) throws IOException {
        String[] tableNames = new String[]{};
        tableNames = JSON.parseArray(tables).toArray(tableNames);
        byte[] data = generatorService.downloadCode(tableNames);
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"java2nb.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");

        IOUtils.write(data, response.getOutputStream());
    }

    @ResponseBody
    @PostMapping("/batchCode")
    public R batchCode(String tables) {
        String[] tableNames = new String[]{};
        tableNames = JSON.parseArray(tables).toArray(tableNames);
        generatorService.generatorCode(tableNames);
        return R.ok("代码批量生成成功，请到本地项目中查看！");
    }

    @GetMapping("/edit")
    public String edit(Model model) {
        Configuration conf = GenUtils.getConfig();
        Map<String, Object> property = new HashMap<>(16);
        property.put("author", conf.getProperty("author"));
        property.put("email", conf.getProperty("email"));
        property.put("package", conf.getProperty("package"));
        property.put("autoRemovePre", conf.getProperty("autoRemovePre"));
        property.put("tablePrefix", conf.getProperty("tablePrefix"));
        property.put("srcPath", conf.getProperty("srcPath"));
        model.addAttribute("property", property);
        return prefix + "/edit";
    }

    @ResponseBody
    @PostMapping("/update")
    R update(@RequestParam Map<String, Object> map) {
        try {
            PropertiesConfiguration conf = new PropertiesConfiguration("generator.properties");
            conf.setProperty("author", map.get("author"));
            conf.setProperty("email", map.get("email"));
            conf.setProperty("package", map.get("package"));
            conf.setProperty("autoRemovePre", map.get("autoRemovePre"));
            conf.setProperty("tablePrefix", map.get("tablePrefix"));
            conf.setProperty("srcPath", map.get("srcPath"));
            conf.save();
        } catch (ConfigurationException e) {
            return R.error("保存配置文件出错");
        }
        return R.ok();
    }

    @GetMapping("/genColumns")
    String genColumns(String tableName, Model model) {
        model.addAttribute("tableName", tableName);
        return "common/genColumns/genColumns";
    }


    @ResponseBody
    @GetMapping("/genColumns/list")
    @SneakyThrows
    public R genColumnsList(String tableName) {
        List<GenColumnsDO> genColumns = generatorService.listColumnsByTableName(tableName);
        int total = genColumns.size();
        PageBean pageBean = new PageBean(genColumns, total);
        return R.ok().put("data", pageBean);
    }


    /**
     * 保存
     */
    @ApiOperation(value = "新增", notes = "新增")
    @ResponseBody
    @PostMapping("/genColumns/save")
    public R save(@RequestBody List<GenColumnsDO> list) {
        generatorService.genColumnsSave(list);
        return R.ok();
    }


}

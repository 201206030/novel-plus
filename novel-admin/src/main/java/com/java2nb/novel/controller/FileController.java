package com.java2nb.novel.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 后台文件上传（富文本编辑器 wangEditor 图片上传）
 * 图片保存到与 novel-front 相同的目录，通过 /localPic/** 访问
 *
 * @author Administrator
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class FileController {

    private static final List<String> ALLOW_EXT = Arrays.asList("jpg", "jpeg", "png", "gif", "bmp", "webp");

    @Value("${pic.save.path}")
    private String picSavePath;

    /**
     * wangEditor 图片上传，返回其约定的 {errno, data:[{url,alt,href}]} 格式
     */
    @SneakyThrows
    @ResponseBody
    @PostMapping(value = "/file/upload", produces = MediaType.APPLICATION_JSON_VALUE)
    Map<String, Object> upload(@RequestParam("wangeditor-file") MultipartFile[] files) {
        List<Map<String, Object>> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            String oriName = file.getOriginalFilename();
            String ext = oriName == null || oriName.lastIndexOf('.') < 0
                    ? "" : oriName.substring(oriName.lastIndexOf('.') + 1).toLowerCase();
            if (!ALLOW_EXT.contains(ext)) {
                return err("不支持的图片格式，仅允许 " + String.join("/", ALLOW_EXT));
            }
            String datePath = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
            String saveFileName = UUID.randomUUID().toString().replace("-", "") + "." + ext;
            String basePath = picSavePath.endsWith("/") ? picSavePath : picSavePath + "/";
            File saveFile = new File(basePath + "localPic/" + datePath, saveFileName);
            if (!saveFile.getParentFile().exists() && !saveFile.getParentFile().mkdirs()) {
                return err("图片目录创建失败");
            }
            file.transferTo(saveFile);
            String url = "/localPic/" + datePath + "/" + saveFileName;
            Map<String, Object> item = new HashMap<>();
            item.put("url", url);
            item.put("alt", oriName);
            item.put("href", url);
            urls.add(item);
        }
        Map<String, Object> resp = new HashMap<>();
        resp.put("errno", 0);
        resp.put("data", urls);
        return resp;
    }

    private Map<String, Object> err(String message) {
        Map<String, Object> resp = new HashMap<>();
        resp.put("errno", 1);
        resp.put("message", message);
        return resp;
    }
}

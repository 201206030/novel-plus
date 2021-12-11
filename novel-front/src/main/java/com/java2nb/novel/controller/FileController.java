package com.java2nb.novel.controller;


import com.java2nb.novel.core.cache.CacheService;
import com.java2nb.novel.core.enums.ResponseStatus;
import com.java2nb.novel.core.utils.Constants;
import com.java2nb.novel.core.utils.FileUtil;
import com.java2nb.novel.core.utils.RandomValidateCodeUtil;
import io.github.xxyopen.model.resp.RestResult;
import io.github.xxyopen.util.UUIDUtil;
import io.github.xxyopen.web.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Date;

/**
 * @author 11797
 */
@Controller
@RequestMapping("file")
@Slf4j
@RequiredArgsConstructor
public class FileController {

    private final CacheService cacheService;

    @Value("${pic.save.path}")
    private String picSavePath;

    /**
     * 生成验证码
     */
    @GetMapping(value = "getVerify")
    public void getVerify(HttpServletRequest request, HttpServletResponse response) {
        try {
            //设置相应类型,告诉浏览器输出的内容为图片
            response.setContentType("image/jpeg");
            //设置响应头信息，告诉浏览器不要缓存此内容
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expire", 0);
            RandomValidateCodeUtil randomValidateCode = new RandomValidateCodeUtil();
            //输出验证码图片方法
            randomValidateCode.getRandcode(cacheService, response);
        } catch (Exception e) {
            log.error("获取验证码失败>>>> ", e);
        }
    }

    /**
     * 图片上传
     * @return
     */
    @SneakyThrows
    @ResponseBody
    @PostMapping("/picUpload")
    RestResult<String> upload(@RequestParam("file") MultipartFile file) {
        Date currentDate = new Date();
        String savePath =
                Constants.LOCAL_PIC_PREFIX + DateUtils.formatDate(currentDate, "yyyy") + "/" +
                        DateUtils.formatDate(currentDate, "MM") + "/" +
                        DateUtils.formatDate(currentDate, "dd");
        String oriName = file.getOriginalFilename();
        assert oriName != null;
        String saveFileName = UUIDUtil.getUUID32() + oriName.substring(oriName.lastIndexOf("."));
        File saveFile = new File(picSavePath + savePath, saveFileName);
        if (!saveFile.getParentFile().exists()) {
            boolean isSuccess = saveFile.getParentFile().mkdirs();
            if(!isSuccess){
                throw new BusinessException(ResponseStatus.FILE_DIR_MAKE_FAIL);
            }
        }
        file.transferTo(saveFile);
        if(!FileUtil.isImage(saveFile)){
            //上传的文件不是图片
            saveFile.delete();
            throw new BusinessException(ResponseStatus.FILE_NOT_IMAGE);
        };
        return RestResult.ok(savePath + "/" + saveFileName);

    }


}

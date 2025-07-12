package com.java2nb.novel.controller;


import com.java2nb.novel.core.cache.CacheService;
import com.java2nb.novel.core.enums.ResponseStatus;
import com.java2nb.novel.core.utils.Constants;
import com.java2nb.novel.core.utils.FileUtil;
import com.java2nb.novel.core.utils.IpUtil;
import com.java2nb.novel.core.utils.RandomValidateCodeUtil;
import io.github.xxyopen.model.resp.RestResult;
import io.github.xxyopen.util.UUIDUtil;
import io.github.xxyopen.web.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @SneakyThrows
    public void getVerify(HttpServletRequest request, HttpServletResponse response) {
        //设置相应类型,告诉浏览器输出的内容为图片
        response.setContentType("image/jpeg");
        //设置响应头信息，告诉浏览器不要缓存此内容
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expire", 0);
        RandomValidateCodeUtil randomValidateCode = new RandomValidateCodeUtil();
        //输出验证码图片方法
        String randomString = randomValidateCode.genRandCodeImage(response.getOutputStream());
        //将生成的随机字符串保存到缓存中
        cacheService.set(RandomValidateCodeUtil.RANDOM_CODE_KEY + ":" + IpUtil.getRealIp(request), randomString,
            60 * 5);
    }

    /**
     * 图片上传
     *
     * - 当使用 `$.ajax`发起异步请求时 ，设置`dataType: "json"`会在请求头中自动添加`Accept: application/json`，表示客户端期望服务器返回
     *   `JSON`格式的数据。
     * - 当使用 `$.ajaxFileUpload` 上传文件时，它的行为与`$.ajax`不同，不会自动修改`Accept`请求头，即使设置了`dataType: "json"`，
     *   `$.ajaxFileUpload`也不会在请求头中添加`Accept: application/json`。
     *
     * Spring Boot 默认返回`JSON`格式的响应，但它支持内容协商，它会根据客户端请求的`Accept`头来决定返回的响应格式。
     * 如果浏览器发送的请求中`Accept`头包含`application/xml`，并且 Spring Boot 支持`XML`格式响应的话，Spring Boot 会返回`XML`格式的响应。
     * 但 Spring Boot 默认不支持`XML`格式的响应，当升级`Sharding-JDBC `版本后，自动引入了`jackson-dataformat-xml`依赖，才开始支持`XML`格式的响应，
     * 由于`$.ajaxFileUpload`上传文件的默认`Accept`头包含`application/xml`，所以需要在后端上传文件接口处明确指定返回的数据类型为`application/json`。
     *
     */
    @SneakyThrows
    @ResponseBody
    @PostMapping(value = "/picUpload", produces = MediaType.APPLICATION_JSON_VALUE)
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
            if (!isSuccess) {
                throw new BusinessException(ResponseStatus.FILE_DIR_MAKE_FAIL);
            }
        }
        file.transferTo(saveFile);
        if (!FileUtil.isImage(saveFile)) {
            //上传的文件不是图片
            saveFile.delete();
            throw new BusinessException(ResponseStatus.FILE_NOT_IMAGE);
        }
        ;
        return RestResult.ok(savePath + "/" + saveFileName);

    }


}

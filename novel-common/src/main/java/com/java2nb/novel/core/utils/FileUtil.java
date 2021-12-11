package com.java2nb.novel.core.utils;

import io.github.xxyopen.util.UUIDUtil;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.Charsets;
import org.apache.http.client.utils.DateUtils;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Date;
import java.util.Objects;

/**
 * 文件操作工具类
 *
 * @author 11797
 */
@UtilityClass
@Slf4j
public class FileUtil {

    /**
     * 网络图片转本地
     */
    public String network2Local(String picSrc, String picSavePath, String visitPrefix) {
        InputStream input = null;
        OutputStream out = null;
        try {
            //本地图片保存
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
            ResponseEntity<Resource> resEntity = RestTemplateUtil.getInstance(Charsets.ISO_8859_1.name()).exchange(picSrc, HttpMethod.GET, requestEntity, Resource.class);
            input = Objects.requireNonNull(resEntity.getBody()).getInputStream();
            Date currentDate = new Date();
            picSrc = visitPrefix + DateUtils.formatDate(currentDate, "yyyy") + "/" + DateUtils.formatDate(currentDate, "MM") + "/" + DateUtils.formatDate(currentDate, "dd") + "/"
                    + UUIDUtil.getUUID32()
                    + picSrc.substring(picSrc.lastIndexOf("."));
            File picFile = new File(picSavePath + picSrc);
            File parentFile = picFile.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            out = new FileOutputStream(picFile);
            byte[] b = new byte[4096];
            for (int n; (n = input.read(b)) != -1; ) {
                out.write(b, 0, n);
            }

            out.flush();
            if (ImageIO.read(picFile) == null) {
                picSrc = "/images/default.gif";
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);

            picSrc = "/images/default.gif";
        } finally {
            closeStream(input, out);
        }


        return picSrc;
    }

    @SneakyThrows
    private void closeStream(InputStream input, OutputStream out) {
        if (input != null) {
            input.close();
        }
        if (out != null) {
            out.close();
        }
    }


    /**
     * 判断文件是否为图片
     *
     * @param file 需要判断的文件
     * @return true:是图片，false:不是图片
     */
    @SneakyThrows
    public boolean isImage(File file) {

        BufferedImage bi = ImageIO.read(file);

        return bi != null;


    }

    public void writeContentToFile(String fileSavePath, String fileSrc, String content) {
        OutputStream out = null;
        try {
            File file = new File(fileSavePath + fileSrc);
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            out = new FileOutputStream(file);
            out.write(content.getBytes());
            byte[] b = new byte[4096];
            out.flush();

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("文件写入失败");
        } finally {
            closeStream(null, out);
        }

    }


}

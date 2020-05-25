package com.java2nb.novel.core.utils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.Charsets;
import org.apache.http.client.utils.DateUtils;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import javax.imageio.ImageIO;
import java.io.*;
import java.util.Date;
import java.util.Objects;

/**
 * 文件操作工具类
 * @author 11797
 */
@Slf4j
public class FileUtil {

    /**
     * 网络图片转本地
     * */
    public static String network2Local(String picSrc,String picSavePath,String visitPrefix) {
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
            if( ImageIO.read(picFile) == null){
                picSrc = "/images/default.gif";
            }

        }catch (Exception e){
            log.error(e.getMessage(),e);

            picSrc = "/images/default.gif";
        }finally {
            if(input != null){
                try {
                    input.close();
                } catch (IOException e) {
                    log.error(e.getMessage(),e);
                }finally {
                    if(out != null){
                        try {
                            out.close();
                        } catch (IOException e) {
                            log.error(e.getMessage(),e);
                        }
                    }
                }
            }
        }



        return picSrc;
    }



}

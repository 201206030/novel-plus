package com.java2nb.novel.core.utils;

import com.java2nb.novel.core.enums.ResponseStatus;
import io.github.xxyopen.util.UUIDUtil;
import io.github.xxyopen.web.exception.BusinessException;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.Charsets;
import org.apache.hc.client5.http.utils.DateUtils;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
        picSavePath = processPicSavePath(picSavePath);
        InputStream input = null;
        OutputStream out = null;
        try {
            //本地图片保存
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
            ResponseEntity<Resource> resEntity = RestTemplates.newInstance(Charsets.ISO_8859_1.name())
                .exchange(picSrc, HttpMethod.GET, requestEntity, Resource.class);
            input = Objects.requireNonNull(resEntity.getBody()).getInputStream();
            String fileExt = picSrc.substring(picSrc.lastIndexOf("."));
            File saveFile = buildSaveFile(picSavePath, visitPrefix, fileExt);
            out = new FileOutputStream(saveFile);
            byte[] b = new byte[4096];
            for (int n; (n = input.read(b)) != -1; ) {
                out.write(b, 0, n);
            }

            out.flush();
            if (ImageIO.read(saveFile) == null) {
                return "/images/default.gif";
            }
            return saveFile.getAbsolutePath().replace(picSavePath, "");

        } catch (Exception e) {
            log.error(e.getMessage(), e);

            return "/images/default.gif";
        } finally {
            closeStream(input, out);
        }
    }


    /**
     * 将用户上传的 MultipartFile 保存为本地文件。
     * <p>
     * 生成 {@code visitPrefix/yyyy/MM/dd/uuid.ext} 路径，创建目录、写入文件、校验是否为图片。 校验失败或写入失败时抛出 {@link BusinessException}，携带对应的
     * {@link ResponseStatus} 错误码。
     *
     * @param file        用户上传的文件
     * @param picSavePath 本地保存根目录
     * @param visitPrefix 访问路径前缀，如 {@code /localPic/}
     * @return 文件的相对访问路径（如 {@code /localPic/2026/11/07/xxx.jpg}）
     * @throws BusinessException 目录创建失败、文件写入失败或文件不是图片时抛出
     */
    public String multipart2Local(MultipartFile file, String picSavePath, String visitPrefix) {
        picSavePath = processPicSavePath(picSavePath);
        String oriName = file.getOriginalFilename();
        assert oriName != null;
        String fileExt = oriName.substring(oriName.lastIndexOf("."));
        File saveFile = buildSaveFile(picSavePath, visitPrefix, fileExt);
        try {
            file.transferTo(saveFile);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(ResponseStatus.FILE_UPLOAD_FAIL);
        }
        if (!isImage(saveFile)) {
            saveFile.delete();
            throw new BusinessException(ResponseStatus.FILE_NOT_IMAGE);
        }
        return saveFile.getAbsolutePath().replace(picSavePath, "");
    }

    /**
     * 根据保存根目录、访问前缀和文件扩展名，构建本地保存文件。
     * <p>
     * 生成 {@code visitPrefix/yyyy/MM/dd/uuid.ext} 路径，创建必要的父目录。
     *
     * @param picSavePath 本地保存根目录
     * @param visitPrefix 访问路径前缀，如 {@code /localPic/}
     * @param fileExt     文件扩展名（含点，如 {@code .jpg}）
     * @return 已创建好父目录的 File 对象
     * @throws BusinessException 父目录创建失败时抛出
     */
    private File buildSaveFile(String picSavePath, String visitPrefix, String fileExt) {
        Date currentDate = new Date();
        String relativePath =
            visitPrefix + DateUtils.formatDate(currentDate, "yyyy") + "/" +
                DateUtils.formatDate(currentDate, "MM") + "/" +
                DateUtils.formatDate(currentDate, "dd") + "/" +
                UUIDUtil.getUUID32() + fileExt;
        File saveFile = new File(picSavePath + relativePath);
        File parentFile = saveFile.getParentFile();
        if (!parentFile.exists()) {
            boolean isSuccess = parentFile.mkdirs();
            if (!isSuccess) {
                throw new BusinessException(ResponseStatus.FILE_DIR_MAKE_FAIL);
            }
        }
        return saveFile;
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

    /**
     * 下载文件
     *
     * @param downloadUrl 下载的URL
     * @param savePath    保存的路径
     */
    @SneakyThrows
    public void downloadFile(String downloadUrl, String savePath) {
        Path path = Paths.get(savePath);
        Path parentPath = path.getParent();
        if (Files.notExists(parentPath)) {
            Files.createDirectories(parentPath);
        }
        URL url = new URL(downloadUrl);
        try (InputStream in = url.openStream()) {
            Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private static String processPicSavePath(String picSavePath) {
        // 如果图片保存路径末尾以 / 结尾，则去掉末尾的 /
        return picSavePath.replaceAll("/+$", "");
    }

}

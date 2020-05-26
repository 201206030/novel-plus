package com.java2nb.novel.service.impl;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.java2nb.novel.core.utils.Constants;
import com.java2nb.novel.core.utils.FileUtil;
import com.java2nb.novel.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;

/**
 * @author 11797
 */
@Service
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(prefix = "pic.save", name = "storage", havingValue = "fastDfs")
public class FastDfsFileServiceImpl implements FileService {

    private final FastFileStorageClient storageClient;

    @Value("${fdfs.webUrl}")
    private String webUrl;

    @Override
    public String transFile(String picSrc, String picSavePath) {

        File file;
        String filePath = FileUtil.network2Local(picSrc, picSavePath, Constants.LOCAL_PIC_PREFIX);
        if (filePath.contains(Constants.LOCAL_PIC_PREFIX)) {
            file = new File(picSavePath + filePath);
        } else {
            //默认图片不存储
            return filePath;
        }

        try {
            FileInputStream inputStream = new FileInputStream(file);
            StorePath storePath = storageClient.uploadFile(inputStream, file.length(),
                    FilenameUtils.getExtension(file.getName()), null);
            //这里额外加上LOCAL_PIC_PREFIX路径，表明该图片是个人资源，而不是爬虫爬取的网络资源，不需要再次进行转换，
            // 实际访问时，再通过nginx的rewite指令来重写路径，去掉LOCAL_PIC_PREFIX
            return webUrl+Constants.LOCAL_PIC_PREFIX+storePath.getFullPath();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            //删除
            file.delete();
        }

        return "/images/default.gif";
    }
}

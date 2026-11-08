package com.java2nb.novel.service.impl;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.CreateBucketRequest;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.java2nb.novel.core.config.OssProperties;
import com.java2nb.novel.core.enums.ResponseStatus;
import com.java2nb.novel.core.utils.Constants;
import com.java2nb.novel.core.utils.FileUtil;
import com.java2nb.novel.service.FileService;
import io.github.xxyopen.web.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * OSS 文件存储实现。
 * <p>
 * 当 {@code pic.save.storage=OSS} 时激活。 上传流程：先写入本地临时文件 → 校验是否为图片 → 上传到 OSS → 删除临时文件。
 *
 * @author 11797
 */
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "pic.save", name = "storage", havingValue = "OSS")
@Slf4j
public class OssFileServiceImpl implements FileService {

    private final OssProperties ossProperties;

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
            return uploadToOss(file, filePath);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return "/images/default.gif";
        }
    }

    @Override
    public String uploadFile(MultipartFile file, String picSavePath) {
        // 复用 FileUtil 统一的 MultipartFile → 本地文件逻辑（含目录创建、图片校验）
        String filePath = FileUtil.multipart2Local(file, picSavePath, Constants.LOCAL_PIC_PREFIX);
        File localFile = new File(picSavePath + filePath);
        return uploadToOss(localFile, filePath);
    }

    /**
     * 将本地文件上传到 OSS，上传完成后删除本地临时文件。
     *
     * @param file     本地文件
     * @param filePath 本地文件路径
     * @return OSS 可访问 URL；失败时返回默认图片路径
     */
    private String uploadToOss(File file, String filePath) {
        String ossKey = filePath.startsWith("/") ? filePath.replaceFirst("/", "") : filePath;
        OSSClient ossClient = new OSSClient(ossProperties.getEndpoint(), ossProperties.getKeyId(),
            ossProperties.getKeySecret());
        try {
            //容器不存在，就创建
            if (!ossClient.doesBucketExist(ossProperties.getBucketName())) {
                CreateBucketRequest request = new CreateBucketRequest(ossProperties.getBucketName());
                request.setCannedACL(CannedAccessControlList.PublicRead);
                ossClient.createBucket(request);
            }
            //上传文件
            PutObjectResult result = ossClient.putObject(
                new PutObjectRequest(ossProperties.getBucketName(), ossKey, file));
            //设置权限 这里是公开读
            ossClient.setBucketAcl(ossProperties.getBucketName(), CannedAccessControlList.PublicRead);

            if (result != null) {
                return ossProperties.getWebUrl() + "/" + ossKey;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            //关闭
            ossClient.shutdown();
            file.delete();
        }

        throw new BusinessException(ResponseStatus.FILE_UPLOAD_FAIL);

    }


}

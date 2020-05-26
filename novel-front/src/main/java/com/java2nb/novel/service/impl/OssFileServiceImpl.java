package com.java2nb.novel.service.impl;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.CreateBucketRequest;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.java2nb.novel.core.config.OssProperties;
import com.java2nb.novel.core.utils.Constants;
import com.java2nb.novel.core.utils.FileUtil;
import com.java2nb.novel.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.File;

/**
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
            file = new File(picSavePath+filePath);
        } else {
            //默认图片不存储
            return filePath;
        }

        filePath = filePath.replaceFirst(picSavePath,"");

        filePath = filePath.startsWith("/") ? filePath.replaceFirst("/","") : filePath;


        OSSClient ossClient = new OSSClient(ossProperties.getEndpoint(), ossProperties.getKeyId(), ossProperties.getKeySecret());
        try {
            //容器不存在，就创建
            if (!ossClient.doesBucketExist(ossProperties.getBucketName())) {
                ossClient.createBucket(ossProperties.getBucketName());
                CreateBucketRequest createBucketRequest = new CreateBucketRequest(ossProperties.getBucketName());
                createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
                ossClient.createBucket(createBucketRequest);
            }
            //上传文件
            PutObjectResult result = ossClient.putObject(new PutObjectRequest(ossProperties.getBucketName(), filePath, file));
            //设置权限 这里是公开读
            ossClient.setBucketAcl(ossProperties.getBucketName(), CannedAccessControlList.PublicRead);

            if(result != null) {
                return ossProperties.getWebUrl() + "/" + filePath;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            //关闭
            ossClient.shutdown();
            file.delete();
        }

        return "/images/default.gif";
    }


}

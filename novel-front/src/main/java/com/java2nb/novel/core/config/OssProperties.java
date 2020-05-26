package com.java2nb.novel.core.config;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 11797
 */
@Data
@Component
@ConfigurationProperties(prefix="novel.file")
public class OssProperties{

    private String endpoint;

    private String keyId;

    private String keySecret;

    private String fileHost;

    private String bucketName;

    private String webUrl;


}

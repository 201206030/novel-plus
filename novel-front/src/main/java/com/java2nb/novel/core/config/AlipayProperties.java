package com.java2nb.novel.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 11797
 */
@Data
@Component
@ConfigurationProperties(prefix="alipay")
public class AlipayProperties {

    private String appId;
    private String merchantPrivateKey;
    private String publicKey;
    private String notifyUrl;
    private String returnUrl;
    private String signType;
    private String charset;
    private String gatewayUrl;
}

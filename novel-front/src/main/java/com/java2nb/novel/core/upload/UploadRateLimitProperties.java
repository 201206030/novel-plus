package com.java2nb.novel.core.upload;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 上传接口限流参数，通过 {@code novel.upload.rate-limit.*} 覆盖。
 *
 * @author xiongxiaoyang
 */
@Data
@Component
@ConfigurationProperties(prefix = "novel.upload.rate-limit")
public class UploadRateLimitProperties {

    /** 限流总开关 */
    private boolean enabled = true;

    /** 单用户每分钟最大上传次数 */
    private int perMinute = 10;

    /** 全站每分钟最大上传次数（防多账号并发刷） */
    private int globalPerMinute = 200;
}

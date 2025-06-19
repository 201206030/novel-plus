package com.java2nb.novel.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Administrator
 */
@Slf4j
public class HttpUtil {

    private static final String DEFAULT_CHARSET = "utf-8";

    private static final Map<String, RestTemplate> REST_TEMPLATE_MAP = new ConcurrentHashMap<>();

    public static String getByHttpClientWithChrome(String url, String charset) {
        log.debug("Get url：{}", url);
        if (!Charset.isSupported(charset)) {
            log.error("字符编码{}无效！", charset);
            return null;
        }
        RestTemplate restTemplate = REST_TEMPLATE_MAP.computeIfAbsent(charset,
            k -> RestTemplates.newInstance(charset));
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("user-agent",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.67 Safari/537.36");
            HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
            ResponseEntity<String> forEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
                String.class);
            log.debug("Response code：{}", forEntity.getStatusCode());
            if (forEntity.getStatusCode() == HttpStatus.OK) {
                return forEntity.getBody();
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public static String getByHttpClientWithChrome(String url) {
        return getByHttpClientWithChrome(url, DEFAULT_CHARSET);
    }

}

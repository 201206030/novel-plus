package com.java2nb.novel.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Administrator
 */
@Slf4j
public class HttpUtil {

    private static final String DEFAULT_CHARSET = "utf-8";

    private static final Map<String, RestTemplate> REST_TEMPLATE_MAP = new ConcurrentHashMap<>();

    private static final String[] USER_AGENTS = {
        // Windows Chrome
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36",
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.0.0 Safari/537.36",

        // Windows Firefox
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:133.0) Gecko/20100101 Firefox/133.0",

        // macOS Chrome
        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36",

        // macOS Safari
        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/18.1 Safari/605.1.15",

        // Linux Chrome
        "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36",

        // Android Chrome
        "Mozilla/5.0 (Linux; Android 14; Pixel 8) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Mobile Safari/537.36",

        // iPhone Safari
        "Mozilla/5.0 (iPhone; CPU iPhone OS 18_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/18.1 Mobile/15E148 Safari/604.1"
    };

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
                randomUserAgent());
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

    private static String randomUserAgent() {
        int index = ThreadLocalRandom.current()
            .nextInt(USER_AGENTS.length);
        return USER_AGENTS[index];
    }

}

package com.java2nb.novel.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

/**
 * @author Administrator
 */
@Slf4j
public class HttpUtil {

    private static final RestTemplate REST_TEMPLATE = RestTemplates.newInstance("utf-8");

    public static String getByHttpClientWithChrome(String url) {
        try {
            log.debug("Get url：{}", url);
            HttpHeaders headers = new HttpHeaders();
            headers.add("user-agent",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.67 Safari/537.36");
            HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
            ResponseEntity<String> forEntity = REST_TEMPLATE.exchange(url, HttpMethod.GET, requestEntity, String.class);
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

}

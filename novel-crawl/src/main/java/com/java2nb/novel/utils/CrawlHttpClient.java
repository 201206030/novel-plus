package com.java2nb.novel.utils;

import com.java2nb.novel.core.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Random;

/**
 * @author Administrator
 */
@Slf4j
@Component
public class CrawlHttpClient {

    @Value("${crawl.interval.min}")
    private Integer intervalMin;

    @Value("${crawl.interval.max}")
    private Integer intervalMax;

    private final Random random = new Random();

    private static final ThreadLocal<Integer> RETRY_COUNT = new ThreadLocal<>();

    public String get(String url) {
        if (Objects.nonNull(intervalMin) && Objects.nonNull(intervalMax) && intervalMax > intervalMin) {
            try {
                Thread.sleep(random.nextInt(intervalMax - intervalMin + 1) + intervalMin);
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        }
        String body = HttpUtil.getByHttpClientWithChrome(url);
        if (Objects.isNull(body) || body.length() < Constants.INVALID_HTML_LENGTH) {
            return processErrorHttpResult(url);
        }
        //成功获得html内容
        return body;
    }

    private String processErrorHttpResult(String url) {
        Integer count = RETRY_COUNT.get();
        if (count == null) {
            count = 0;
        }
        if (count < Constants.HTTP_FAIL_RETRY_COUNT) {
            RETRY_COUNT.set(++count);
            return get(url);
        }
        RETRY_COUNT.remove();
        return null;
    }

}

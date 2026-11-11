package com.java2nb.novel.core.ai;

import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.StringRedisTemplate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * AI 限流窗口与故障降级测试（不依赖真实 Redis）。
 */
class AiApiRateLimiterTest {

    /** 模拟 Redis 完全不可用，限流应降级到进程内计数而不是整体失效 */
    private StringRedisTemplate brokenRedis() {
        return mock(StringRedisTemplate.class, invocation -> {
            throw new RuntimeException("redis down");
        });
    }

    @Test
    void fallsBackToLocalWindowWhenRedisIsDown() {
        AiRateLimitProperties properties = new AiRateLimitProperties();
        properties.setPerMinute(3);
        properties.setPerDay(100);
        properties.setGlobalPerMinute(100);
        AiApiRateLimiter limiter = new AiApiRateLimiter(brokenRedis(), properties);

        assertTrue(limiter.tryAcquire(1L, 1));
        assertTrue(limiter.tryAcquire(1L, 1));
        assertTrue(limiter.tryAcquire(1L, 1));
        assertFalse(limiter.tryAcquire(1L, 1), "同一用户超过每分钟点数应被拒绝");
        assertTrue(limiter.tryAcquire(2L, 1), "不同用户之间的配额互不影响");
    }

    @Test
    void longTextCostsMorePoints() {
        AiRateLimitProperties properties = new AiRateLimitProperties();
        properties.setPerMinute(4);
        properties.setPerDay(100);
        properties.setGlobalPerMinute(100);
        AiApiRateLimiter limiter = new AiApiRateLimiter(brokenRedis(), properties);

        assertTrue(limiter.tryAcquire(3L, 3));
        assertFalse(limiter.tryAcquire(3L, 3), "两次长文本合计超过每分钟点数应被拒绝");
    }

    @Test
    void globalWindowBlocksManyAccounts() {
        AiRateLimitProperties properties = new AiRateLimitProperties();
        properties.setPerMinute(100);
        properties.setPerDay(1000);
        properties.setGlobalPerMinute(3);
        AiApiRateLimiter limiter = new AiApiRateLimiter(brokenRedis(), properties);

        assertTrue(limiter.tryAcquire(1L, 1));
        assertTrue(limiter.tryAcquire(2L, 1));
        assertTrue(limiter.tryAcquire(3L, 1));
        assertFalse(limiter.tryAcquire(4L, 1), "全站额度耗尽后新账号也应被拒绝");
    }

    @Test
    void missingRedisCounterIsRejectedConservatively() {
        // execute 返回 null 表示拿不到计数，此时不能静默放行
        StringRedisTemplate redis = mock(StringRedisTemplate.class, invocation -> null);
        AiApiRateLimiter limiter = new AiApiRateLimiter(redis, new AiRateLimitProperties());

        assertFalse(limiter.tryAcquire(1L, 1));
    }

    @Test
    void disabledLimiterDoesNotTouchRedis() {
        AiRateLimitProperties properties = new AiRateLimitProperties();
        properties.setEnabled(false);
        AiApiRateLimiter limiter = new AiApiRateLimiter(brokenRedis(), properties);

        assertTrue(limiter.tryAcquire(1L, 100));
        assertTrue(limiter.tryAcquire(1L, 100));
        assertTrue(limiter.tryAcquireLight(1L));
    }

    @Test
    void lightWindowIsIndependentFromQuotaWindow() {
        AiRateLimitProperties properties = new AiRateLimitProperties();
        properties.setLightPerMinute(2);
        properties.setPerMinute(100);
        properties.setPerDay(1000);
        properties.setGlobalPerMinute(1000);
        AiApiRateLimiter limiter = new AiApiRateLimiter(brokenRedis(), properties);

        assertTrue(limiter.tryAcquireLight(1L));
        assertTrue(limiter.tryAcquireLight(1L));
        assertFalse(limiter.tryAcquireLight(1L), "轻量窗口超限后应被拒绝");
        assertTrue(limiter.tryAcquireLight(2L), "不同用户的轻量配额互不影响");
        assertTrue(limiter.tryAcquire(1L, 1), "任务轮询不应消耗模型额度");
    }
}

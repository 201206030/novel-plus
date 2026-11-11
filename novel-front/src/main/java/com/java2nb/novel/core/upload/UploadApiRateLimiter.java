package com.java2nb.novel.core.upload;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 上传接口限流器。
 *
 * <p>两个固定窗口：单用户每分钟、全站每分钟。计数通过 Redis Lua 脚本原子完成
 *（INCRBY + 首次 EXPIRE），与 {@code AiApiRateLimiter} 共享同一套模式但独立计数，
 * 互不干扰；Redis 不可用时自动降级为进程内计数。</p>
 *
 * @author xiongxiaoyang
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UploadApiRateLimiter {

    private static final String KEY_PREFIX = "novel:upload:limit:";
    private static final long MINUTE_SECONDS = 60L;
    private static final int LOCAL_WINDOW_CLEAN_THRESHOLD = 5_000;

    private static final DefaultRedisScript<Long> INCR_SCRIPT = new DefaultRedisScript<>(
        "local c = redis.call('INCRBY', KEYS[1], tonumber(ARGV[2])) "
            + "if redis.call('TTL', KEYS[1]) < 0 then redis.call('EXPIRE', KEYS[1], tonumber(ARGV[1])) end "
            + "return c",
        Long.class);

    private final StringRedisTemplate stringRedisTemplate;
    private final UploadRateLimitProperties properties;

    private final Map<String, LocalWindow> localWindows = new ConcurrentHashMap<>();

    /**
     * 尝试获取上传配额：单用户窗口 + 全站窗口，两个都通过才放行。
     *
     * @param userId 已登录用户 ID
     * @return true 放行，false 触发限流
     */
    public boolean tryAcquire(long userId) {
        if (!properties.isEnabled()) {
            return true;
        }
        long minute = System.currentTimeMillis() / 1000L / 60L;
        String userKey = "u:" + userId + ":m:" + minute;
        String globalKey = "g:m:" + minute;

        try {
            boolean userOk = increment(userKey, properties.getPerMinute());
            boolean globalOk = increment(globalKey, properties.getGlobalPerMinute());
            return userOk && globalOk;
        } catch (Exception e) {
            log.warn("上传限流 Redis 不可用，降级为单机内存限流：{}", e.toString());
            cleanExpiredLocalWindows();
            boolean userOk = localIncrement(userKey, properties.getPerMinute());
            boolean globalOk = localIncrement(globalKey, properties.getGlobalPerMinute());
            return userOk && globalOk;
        }
    }

    private boolean increment(String key, int limit) {
        Long current = stringRedisTemplate.execute(INCR_SCRIPT,
            Collections.singletonList(KEY_PREFIX + key),
            String.valueOf(MINUTE_SECONDS), "1");
        return current <= limit;
    }

    private boolean localIncrement(String key, int limit) {
        long now = System.currentTimeMillis();
        LocalWindow window = localWindows.compute(key, (k, old) ->
            old == null || old.expireAt <= now ? new LocalWindow(now + MINUTE_SECONDS * 1000L) : old);
        return window.count.incrementAndGet() <= limit;
    }

    private void cleanExpiredLocalWindows() {
        if (localWindows.size() > LOCAL_WINDOW_CLEAN_THRESHOLD) {
            long now = System.currentTimeMillis();
            localWindows.entrySet().removeIf(entry -> entry.getValue().expireAt <= now);
        }
    }

    private static final class LocalWindow {
        private final AtomicInteger count = new AtomicInteger();
        private final long expireAt;

        private LocalWindow(long expireAt) {
            this.expireAt = expireAt;
        }
    }
}

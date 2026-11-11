package com.java2nb.novel.core.ai;

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
 * AI 创作接口限流器。
 *
 * <p>三个固定窗口：单用户每分钟、单用户每天、全站每分钟。计数通过 Redis Lua 脚本
 * 原子完成（INCRBY + 首次设置 TTL），集群部署下共享同一份配额；Redis 不可用时
 * 自动降级为进程内计数，保证限流不会因为缓存故障而完全失效。</p>
 *
 * @author xiongxiaoyang
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AiApiRateLimiter {

    private static final String KEY_PREFIX = "novel:ai:limit:";
    private static final long MINUTE_SECONDS = 60L;
    private static final long DAY_SECONDS = 24L * 60L * 60L;
    private static final int LOCAL_WINDOW_CLEAN_THRESHOLD = 10_000;

    private static final DefaultRedisScript<Long> INCR_SCRIPT = new DefaultRedisScript<>(
        "local c = redis.call('INCRBY', KEYS[1], tonumber(ARGV[2])) "
            + "if redis.call('TTL', KEYS[1]) < 0 then redis.call('EXPIRE', KEYS[1], tonumber(ARGV[1])) end "
            + "return c",
        Long.class);

    private final StringRedisTemplate stringRedisTemplate;
    private final AiRateLimitProperties properties;

    /** Redis 故障时的单机降级窗口，key 中带时间片，过期后自动失效 */
    private final Map<String, LocalWindow> localWindows = new ConcurrentHashMap<>();

    /**
     * 尝试消耗指定点数。
     *
     * @param userId 已登录用户 ID（不是作者 ID，避免一个作者多账号绕过配额）
     * @param cost   本次请求折算的点数
     * @return true 表示放行，false 表示触发限流
     */
    public boolean tryAcquire(long userId, int cost) {
        if (!properties.isEnabled()) {
            return true;
        }
        int maxCost = Math.max(1, properties.getMaxCostPerRequest());
        int points = Math.max(1, Math.min(cost, maxCost));

        long minute = System.currentTimeMillis() / 1000L / 60L;
        long day = minute / (24L * 60L);
        String userMinuteKey = "u:" + userId + ":m:" + minute;
        String userDayKey = "u:" + userId + ":d:" + day;
        String globalMinuteKey = "g:m:" + minute;

        try {
            // 三个窗口都执行，不使用短路，避免某个窗口漏计数
            boolean minuteOk = increment(userMinuteKey, MINUTE_SECONDS, points, properties.getPerMinute());
            boolean dayOk = increment(userDayKey, DAY_SECONDS, points, properties.getPerDay());
            boolean globalOk = increment(globalMinuteKey, MINUTE_SECONDS, points,
                properties.getGlobalPerMinute());
            return minuteOk && dayOk && globalOk;
        } catch (Exception e) {
            log.warn("AI 限流 Redis 不可用，降级为单机内存限流：{}", e.toString());
            cleanExpiredLocalWindows();
            boolean minuteOk = localIncrement(userMinuteKey, MINUTE_SECONDS, points,
                properties.getPerMinute());
            boolean dayOk = localIncrement(userDayKey, DAY_SECONDS, points, properties.getPerDay());
            boolean globalOk = localIncrement(globalMinuteKey, MINUTE_SECONDS, points,
                properties.getGlobalPerMinute());
            return minuteOk && dayOk && globalOk;
        }
    }

    /**
     * 轻量频率保护：用于不消耗模型额度的 AI 助手 CRUD / 任务轮询接口。
     *
     * <p>与额度窗口分开计数，既防高频刷接口，又不会让 2.5 秒一次的任务轮询吃掉创作额度，
     * 也不会反过来把轮询限死。</p>
     *
     * @param userId 已登录用户 ID
     * @return true 表示放行，false 表示触发限流
     */
    public boolean tryAcquireLight(long userId) {
        if (!properties.isEnabled()) {
            return true;
        }
        long minute = System.currentTimeMillis() / 1000L / 60L;
        String key = "light:u:" + userId + ":m:" + minute;
        try {
            return increment(key, MINUTE_SECONDS, 1, properties.getLightPerMinute());
        } catch (Exception e) {
            log.warn("AI 轻量限流 Redis 不可用，降级为单机内存限流：{}", e.toString());
            cleanExpiredLocalWindows();
            return localIncrement(key, MINUTE_SECONDS, 1, properties.getLightPerMinute());
        }
    }

    private boolean increment(String key, long ttlSeconds, int points, int limit) {
        Long current = stringRedisTemplate.execute(INCR_SCRIPT,
            Collections.singletonList(KEY_PREFIX + key),
            String.valueOf(ttlSeconds), String.valueOf(points));
        if (current == null) {
            // 拿不到计数时保守拒绝，避免限流被静默绕过
            log.warn("AI 限流脚本未返回计数，拒绝本次请求：key={}", key);
            return false;
        }
        return current <= limit;
    }

    private boolean localIncrement(String key, long ttlSeconds, int points, int limit) {
        long now = System.currentTimeMillis();
        LocalWindow window = localWindows.compute(key, (k, old) ->
            old == null || old.expireAt <= now ? new LocalWindow(now + ttlSeconds * 1000L) : old);
        return window.count.addAndGet(points) <= limit;
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

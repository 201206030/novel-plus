package com.java2nb.novel.service.impl;

import com.java2nb.novel.service.LikeService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.StaticScriptSource;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * @author xiongxiaoyang
 * @date 2025/7/12
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LikeServiceImpl implements LikeService {

    private final RedisTemplate<Object, Object> redisTemplate;

    private DefaultRedisScript<Long> toggleLikeScript;

    private static final String COMMENT_LIKE_KEY_PREFIX = "like:comment:";
    private static final String COMMENT_REPLY_LIKE_KEY_PREFIX = "like:comment:reply:";
    private static final String COMMENT_UN_LIKE_KEY_PREFIX = "unlike:comment:";
    private static final String COMMENT_REPLY_UN_LIKE_KEY_PREFIX = "unlike:comment:reply:";

    @PostConstruct
    public void init() {
        // Lua 脚本保证原子性操作
        String script = """
            local key = KEYS[1]
            local userId = ARGV[1]

            local isLiked = redis.call('SISMEMBER', key, userId)

            if isLiked == 1 then
                redis.call('SREM', key, userId)
            else
                redis.call('SADD', key, userId)
            end

            return redis.call('SCARD', key)
        """;

        toggleLikeScript = new DefaultRedisScript<>();
        toggleLikeScript.setScriptSource(new StaticScriptSource(script));
        toggleLikeScript.setResultType(Long.class);
    }

    public Long toggleCommentLike(Long commentId, Long userId) {
        return executeToggle(COMMENT_LIKE_KEY_PREFIX + commentId, userId);
    }

    @Override
    public Long toggleCommentUnLike(Long commentId, Long userId) {
        return executeToggle(COMMENT_UN_LIKE_KEY_PREFIX + commentId, userId);
    }

    public Long getCommentLikesCount(Long commentId) {
        return redisTemplate.opsForSet().size(COMMENT_LIKE_KEY_PREFIX + commentId);
    }

    @Override
    public Long getCommentUnLikesCount(Long commentId) {
        return redisTemplate.opsForSet().size(COMMENT_UN_LIKE_KEY_PREFIX + commentId);
    }

    public Long toggleReplyLike(Long replyId, Long userId) {
        return executeToggle(COMMENT_REPLY_LIKE_KEY_PREFIX + replyId, userId);
    }

    @Override
    public Long toggleReplyUnLike(Long replyId, Long userId) {
        return executeToggle(COMMENT_REPLY_UN_LIKE_KEY_PREFIX + replyId, userId);
    }

    public Long getReplyLikesCount(Long replyId) {
        return redisTemplate.opsForSet().size(COMMENT_REPLY_LIKE_KEY_PREFIX + replyId);
    }

    @Override
    public Long getReplyUnLikesCount(Long replyId) {
        return redisTemplate.opsForSet().size(COMMENT_REPLY_UN_LIKE_KEY_PREFIX + replyId);
    }

    private Long executeToggle(String key, Long userId) {
        return redisTemplate.execute(toggleLikeScript, Collections.singletonList(key), userId);
    }
}

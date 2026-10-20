package com.java2nb.novel.mapper;

import com.java2nb.novel.entity.AiAuthorPref;
import org.apache.ibatis.annotations.Param;

/**
 * AI创作全局偏好 Mapper
 *
 * @author 11797
 */
public interface FrontAiAuthorPrefMapper {

    /**
     * 查询作者偏好
     */
    AiAuthorPref selectByAuthorId(@Param("authorId") Long authorId);

    /**
     * 插入或更新偏好
     */
    int upsert(AiAuthorPref pref);
}

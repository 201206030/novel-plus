package com.java2nb.novel.vo;

import com.java2nb.novel.entity.AiAuthorPref;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * AI创作作者偏好管理列表VO
 *
 * @author Administrator
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AdminAiPrefVO extends AiAuthorPref {

    /**
     * 作家笔名
     */
    private String penName;

}

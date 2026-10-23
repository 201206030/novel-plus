package com.java2nb.novel.vo;

import com.java2nb.novel.entity.AiCreationPlan;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * AI创作方案管理列表VO
 *
 * @author Administrator
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AdminAiPlanVO extends AiCreationPlan {

    /**
     * 作家笔名
     */
    private String penName;

    /**
     * 关联小说名（未关联时为空）
     */
    private String bookName;

}

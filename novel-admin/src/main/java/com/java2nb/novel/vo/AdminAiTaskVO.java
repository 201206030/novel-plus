package com.java2nb.novel.vo;

import com.java2nb.novel.entity.AiCreationTask;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * AI创作任务管理列表VO
 *
 * @author Administrator
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AdminAiTaskVO extends AiCreationTask {

    /**
     * 作家笔名
     */
    private String penName;

    /**
     * 关联小说名（方案未关联小说时为空）
     */
    private String bookName;

}

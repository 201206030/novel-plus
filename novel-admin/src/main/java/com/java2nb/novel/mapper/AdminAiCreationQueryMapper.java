package com.java2nb.novel.mapper;

import com.java2nb.novel.entity.AiChapterPlan;
import com.java2nb.novel.entity.AiCreationTask;
import com.java2nb.novel.vo.AdminAiPlanVO;
import com.java2nb.novel.vo.AdminAiPrefVO;
import com.java2nb.novel.vo.AdminAiTaskVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * AI创作模块管理查询Mapper（列表联表查询；增删改复用公共生成Mapper）
 *
 * @author Administrator
 */
@Mapper
public interface AdminAiCreationQueryMapper {

    /**
     * 分页查询创作方案列表（含笔名/关联小说名）
     *
     * @param keyword 笔名/书名关键字（可空）
     * @param stage   创作阶段（可空）
     * @return 方案列表
     */
    List<AdminAiPlanVO> listPlans(@Param("keyword") String keyword, @Param("stage") Byte stage);

    /**
     * 分页查询章节规划列表（按方案，章节序号升序）
     *
     * @param planId  创作方案ID
     * @param status  章节状态（可空）
     * @return 章节规划列表
     */
    List<AiChapterPlan> listChapterPlans(@Param("planId") Long planId, @Param("status") Byte status);

    /**
     * 分页查询生成任务列表（含笔名/关联小说名）
     *
     * @param planId   创作方案ID（可空）
     * @param status   任务状态：0执行中 1成功 2失败（可空）
     * @param taskType 任务类型（可空）
     * @return 任务列表
     */
    List<AdminAiTaskVO> listTasks(@Param("planId") Long planId, @Param("status") Byte status,
        @Param("taskType") Byte taskType);

    /**
     * 查询单个任务（删除前状态校验用）
     *
     * @param id 任务ID
     * @return 任务
     */
    AiCreationTask getTask(@Param("id") Long id);

    /**
     * 分页查询作者创作偏好列表（含笔名）
     *
     * @param keyword 笔名关键字（可空）
     * @return 偏好列表
     */
    List<AdminAiPrefVO> listPrefs(@Param("keyword") String keyword);

}

package com.java2nb.novel.mapper;

import com.java2nb.novel.entity.AiCreationPlan;
import org.apache.ibatis.annotations.Param;

/**
 * AI创作方案 Mapper
 *
 * @author 11797
 */
public interface FrontAiCreationPlanMapper {

    /**
     * 新增创作方案
     */
    int insert(AiCreationPlan plan);

    /**
     * 按ID与作者查询（权限隔离）
     */
    AiCreationPlan selectByIdAndAuthor(@Param("id") Long id, @Param("authorId") Long authorId);

    /**
     * 按小说ID查询
     */
    AiCreationPlan selectByBookId(@Param("bookId") Long bookId);

    /**
     * 查询作者最近一条未绑定小说的方案（新建创作时复用，避免重复创建空方案）
     */
    AiCreationPlan selectLatestUnboundByAuthor(@Param("authorId") Long authorId);

    /**
     * 更新方案（限定作者，stage/参数/方案内容/绑定小说等）
     */
    int update(AiCreationPlan plan);
}

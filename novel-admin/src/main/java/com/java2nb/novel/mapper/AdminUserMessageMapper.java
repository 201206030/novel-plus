package com.java2nb.novel.mapper;

import com.java2nb.novel.entity.UserMessage;
import com.java2nb.novel.vo.AdminUserMessageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 消息管理Mapper
 *
 * @author Administrator
 */
@Mapper
public interface AdminUserMessageMapper {

    /**
     * 分页查询消息列表（含收发用户昵称/书名）
     *
     * @param userId   接收用户ID（可空）
     * @param msgType  消息类型（可空）
     * @param readFlag 是否已读（可空）
     * @return 消息列表
     */
    List<AdminUserMessageVO> listAll(@Param("userId") Long userId, @Param("msgType") Byte msgType,
        @Param("readFlag") Byte readFlag);

    /**
     * 根据ID查询消息
     *
     * @param id 消息ID
     * @return 消息
     */
    UserMessage getById(@Param("id") Long id);

    /**
     * 新增消息（后台发送消息）
     *
     * @param userMessage 消息
     * @return 影响行数
     */
    int insert(UserMessage userMessage);

    /**
     * 删除消息
     *
     * @param id 消息ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 批量删除消息
     *
     * @param ids 消息ID集合
     * @return 影响行数
     */
    int batchDeleteByIds(@Param("ids") List<Long> ids);

}

package com.java2nb.novel.mapper;

import com.java2nb.novel.entity.UserMessage;
import com.java2nb.novel.vo.UserMessageVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户消息 Mapper
 *
 * @author 11797
 */
public interface FrontUserMessageMapper {

    /**
     * 插入一条消息
     */
    int insert(UserMessage userMessage);

    /**
     * 分页查询用户消息（按接收人），附带发送人昵称/头像与书名/章节名
     */
    List<UserMessageVO> listByPage(@Param("userId") Long userId, @Param("msgType") Byte msgType);

    /**
     * 未读消息数
     */
    long countUnread(@Param("userId") Long userId);

    /**
     * 单条标记已读（限本人消息）
     */
    int markRead(@Param("userId") Long userId, @Param("id") Long id);

    /**
     * 全部标记已读
     */
    int markAllRead(@Param("userId") Long userId);
}

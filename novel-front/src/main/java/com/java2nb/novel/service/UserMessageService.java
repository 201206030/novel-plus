package com.java2nb.novel.service;

import com.java2nb.novel.vo.UserMessageVO;
import io.github.xxyopen.model.page.PageBean;

/**
 * 用户消息 Service
 *
 * @author 11797
 */
public interface UserMessageService {

    /**
     * 投递一条被回复消息（自己回复自己不通知；接收人为空时跳过）
     *
     * @param sendUserId   发送人（回复者）
     * @param toUserId     接收人（被回复者）
     * @param msgType      消息类型 {@link com.java2nb.novel.entity.UserMessage#TYPE_BOOK_COMMENT_REPLY} 等
     * @param bookId       书籍ID
     * @param bookIndexId  章节ID（段评消息传入，书评消息可为空）
     * @param targetId     被回复的主评论ID
     * @param replyContent 回复内容（截取摘要）
     */
    void sendReplyMessage(Long sendUserId, Long toUserId, byte msgType, Long bookId, Long bookIndexId, Long targetId, String replyContent);

    /**
     * 分页查询我的消息
     */
    PageBean<UserMessageVO> listMyMessageByPage(Long userId, Byte msgType, int page, int pageSize);

    /**
     * 我的未读消息数
     */
    long countUnread(Long userId);

    /**
     * 单条标记已读
     */
    void markRead(Long userId, Long id);

    /**
     * 全部标记已读
     */
    void markAllRead(Long userId);
}

package com.java2nb.novel.service.impl;

import com.github.pagehelper.PageHelper;
import com.java2nb.novel.entity.UserMessage;
import com.java2nb.novel.mapper.FrontUserMessageMapper;
import com.java2nb.novel.service.UserMessageService;
import com.java2nb.novel.vo.UserMessageVO;
import io.github.xxyopen.model.page.PageBean;
import io.github.xxyopen.model.page.builder.pagehelper.PageBuilder;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 用户消息服务实现
 *
 * @author 11797
 */
@Service
@RequiredArgsConstructor
public class UserMessageServiceImpl implements UserMessageService {

    /**
     * 消息内容摘要最大长度，与表字段 content 保持一致
     */
    private static final int CONTENT_MAX_LENGTH = 200;

    private final FrontUserMessageMapper userMessageMapper;

    @Override
    public void sendReplyMessage(Long sendUserId, Long toUserId, byte msgType, Long bookId,
        Long bookIndexId, Long targetId, String replyContent) {
        // 无接收人、自己回复自己、无被回复主评论，均不通知
        if (toUserId == null || targetId == null || toUserId.equals(sendUserId)) {
            return;
        }
        UserMessage message = new UserMessage();
        message.setUserId(toUserId);
        message.setSendUserId(sendUserId);
        message.setMsgType(msgType);
        message.setBookId(bookId);
        message.setBookIndexId(bookIndexId);
        message.setTargetId(targetId);
        message.setContent(abbreviateContent(replyContent));
        message.setReadFlag((byte) 0);
        userMessageMapper.insert(message);
    }

    /**
     * 截取回复内容摘要，去除换行避免破坏展示
     */
    private String abbreviateContent(String content) {
        if (StringUtils.isBlank(content)) {
            return null;
        }
        String clean = content.replaceAll("\\s+", " ").trim();
        return clean.length() > CONTENT_MAX_LENGTH ? clean.substring(0, CONTENT_MAX_LENGTH) : clean;
    }

    @Override
    public PageBean<UserMessageVO> listMyMessageByPage(Long userId, Byte msgType, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return PageBuilder.build(userMessageMapper.listByPage(userId, msgType));
    }

    @Override
    public long countUnread(Long userId) {
        return userMessageMapper.countUnread(userId);
    }

    @Override
    public void markRead(Long userId, Long id) {
        userMessageMapper.markRead(userId, id);
    }

    @Override
    public void markAllRead(Long userId) {
        userMessageMapper.markAllRead(userId);
    }
}

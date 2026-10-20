package com.java2nb.novel.service;

import com.github.pagehelper.PageHelper;
import com.java2nb.novel.entity.UserMessage;
import com.java2nb.novel.mapper.AdminUserMessageMapper;
import com.java2nb.novel.vo.AdminUserMessageVO;
import io.github.xxyopen.model.page.PageBean;
import io.github.xxyopen.model.page.builder.pagehelper.PageBuilder;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static com.java2nb.novel.core.utils.AdminUtil.biz;

/**
 * 消息管理Service
 *
 * @author Administrator
 */
@Service
@RequiredArgsConstructor
public class MessageService {

    private final AdminUserMessageMapper userMessageMapper;

    public PageBean<AdminUserMessageVO> list(Long userId, Byte msgType, Byte readFlag, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        List<AdminUserMessageVO> list = userMessageMapper.listAll(userId, msgType, readFlag);
        return PageBuilder.build(list);
    }

    public UserMessage get(Long id) {
        UserMessage userMessage = userMessageMapper.getById(id);
        if (userMessage == null) {
            throw biz("消息不存在");
        }
        return userMessage;
    }

    /**
     * 后台发送消息
     *
     * @param userMessage 消息
     */
    public void send(UserMessage userMessage) {
        if (userMessage.getUserId() == null) {
            throw biz("请选择接收用户");
        }
        if (StringUtils.isBlank(userMessage.getContent())) {
            throw biz("消息内容不能为空");
        }
        // 后台发送固定为系统通知，不接受前端传入的类型
        userMessage.setMsgType((byte) UserMessage.TYPE_SYSTEM_NOTICE);
        if (userMessage.getSendUserId() == null) {
            // 默认以系统管理员身份发送
            userMessage.setSendUserId(1L);
        }
        if (userMessage.getTargetId() == null) {
            // 手动发送时无关联评论，列非空，用0占位
            userMessage.setTargetId(0L);
        }
        userMessage.setReadFlag((byte) 0);
        userMessage.setCreateTime(new Date());
        userMessageMapper.insert(userMessage);
    }

    public void remove(Long id) {
        throw biz("消息不支持删除，如需清理请联系用户前台删除");
    }

    public void batchRemove(String ids) {
        throw biz("消息不支持删除，如需清理请联系用户前台删除");
    }

}

package com.java2nb.novel.vo;

import com.java2nb.novel.entity.UserMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 消息管理列表VO
 *
 * @author Administrator
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AdminUserMessageVO extends UserMessage {

    /**
     * 发送用户昵称
     */
    private String sendUserName;

    /**
     * 接收用户昵称
     */
    private String receiveUserName;

    /**
     * 关联书名
     */
    private String bookName;

}

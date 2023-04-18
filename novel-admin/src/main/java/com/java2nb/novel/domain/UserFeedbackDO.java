package com.java2nb.novel.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.java2nb.common.jsonserializer.LongToStringSerializer;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;


/**
 * @author xiongxy
 * @email 1179705413@qq.com
 * @date 2023-04-18 11:08:54
 */
public class UserFeedbackDO implements Serializable {

    private static final long serialVersionUID = 1L;


    //主键id
    //java中的long能表示的范围比js中number大,也就意味着部分数值在js中存不下(变成不准确的值)
    //所以通过序列化成字符串来解决
    @JsonSerialize(using = LongToStringSerializer.class)
    private Long id;
    //用户id
    //java中的long能表示的范围比js中number大,也就意味着部分数值在js中存不下(变成不准确的值)
    //所以通过序列化成字符串来解决
    @JsonSerialize(using = LongToStringSerializer.class)
    private Long userId;
    //反馈内容
    private String content;
    //反馈时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private String userName;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 设置：主键id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取：主键id
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置：用户id
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 获取：用户id
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 设置：反馈内容
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 获取：反馈内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置：反馈时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取：反馈时间
     */
    public Date getCreateTime() {
        return createTime;
    }
}

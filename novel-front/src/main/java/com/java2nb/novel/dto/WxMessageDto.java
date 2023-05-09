package com.java2nb.novel.dto;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author zhu
 * @date 2023/5/9
 * @description
 */
@Data
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class WxMessageDto {
    /**
     * 开发者微信号
     */
    @XmlElement(name = "ToUserName")
    private String toUserName;

    /**
     * 发送方帐号（一个OpenID）
     */
    @XmlElement(name = "FromUserName")
    private String fromUserName;

    /**
     * 消息创建时间 （整型）
     */
    @XmlElement(name = "CreateTime")
    private String createTime;

    /**
     * 消息类型，文本为text
     */
    @XmlElement(name = "MsgType")
    private String msgType;

    /**
     * 文本消息内容
     */
    @XmlElement(name = "Content")
    private String content;

    /**
     * 消息id，64位整型
     */
    @XmlElement(name = "MsgId")
    private String msgId;

    /**
     * 消息的数据id
     */
    @XmlElement(name = "MsgDataId")
    private String msgDataId;

    /**
     * 多图文时第几篇文章，从1开始（消息如果来自文章时才有）
     */
    @XmlElement(name = "Idx")
    private String idx;

    /**
     * 图片链接（由系统生成）
     */
    @XmlElement(name = "PicUrl")
    private String picUrl;

    /**
     * 图片消息媒体id，可以调用获取临时素材接口拉取数据。
     */
    @XmlElement(name = "MediaId")
    private String mediaId;

    /**
     * 语音格式：amr
     */
    @XmlElement(name = "Format")
    private String format;
    /**
     * 语音识别结果，UTF8编码
     */
    @XmlElement(name = "Recognition")
    private String recognition;

    /**
     * 视频消息缩略图的媒体id，可以调用多媒体文件下载接口拉取数据。
     */
    @XmlElement(name = "ThumbMediaId")
    private String thumbMediaId;
}

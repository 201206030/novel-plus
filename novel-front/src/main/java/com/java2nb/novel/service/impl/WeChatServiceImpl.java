package com.java2nb.novel.service.impl;

import cn.hutool.core.util.XmlUtil;
import com.java2nb.novel.core.except.AesException;
import com.java2nb.novel.core.utils.WXPublicUtils;
import com.java2nb.novel.dto.WxMessageDto;
import com.java2nb.novel.service.WeChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhu
 * @date 2023/5/9
 * @description
 */
@Service
@Slf4j
public class WeChatServiceImpl implements WeChatService {
    @Override
    public String weCheck(HttpServletRequest request) throws AesException {
        String msgSignature = request.getParameter("signature");
        String msgTimestamp = request.getParameter("timestamp");
        String msgNonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        if (WXPublicUtils.verifyUrl(msgSignature, msgTimestamp, msgNonce)) {
            return echostr;
        }
        return null;
    }

    @Override
    public String getMessage(HttpServletRequest request, WxMessageDto dto) {
        String msgSignature = request.getParameter("signature");
        String msgTimestamp = request.getParameter("timestamp");
        String msgNonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        StringBuilder sb = new StringBuilder("<xml>\n" +
                "  <ToUserName><![CDATA[" + dto.getFromUserName() + "]]></ToUserName>\n" +
                "  <FromUserName><![CDATA[" + dto.getToUserName() + "]]></FromUserName>\n" +
                "  <CreateTime>"+dto.getCreateTime()+"</CreateTime>\n");
        switch (dto.getMsgType()) {
            case "text":
                //处理文本消息
                disposeText(sb, dto.getContent());
                break;
            default:
                break;
        }
        sb.append("</xml>");
        return sb.toString();
    }

    private void disposeText(StringBuilder sb, String content) {
        //查询是否存在该返回信息逻辑
        //无逻辑，返回菜单
        String stringBuilder = "你发的信息小仙无法识别嗷！\n" +
                "请回复以下数字：\n" +
                "1. 北京\n" +
                "2. 上海\n" +
                "3. 武汉\n" +
                "4. 小说";
        sb.append("<MsgType><![CDATA[text]]></MsgType>\n");
        sb.append("<Content><![CDATA[" + stringBuilder + "]]></Content>\n");
    }
}

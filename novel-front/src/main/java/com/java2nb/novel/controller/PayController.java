package com.java2nb.novel.controller;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.java2nb.novel.core.bean.UserDetails;
import com.java2nb.novel.core.config.AlipayProperties;
import com.java2nb.novel.core.utils.ThreadLocalUtil;
import com.java2nb.novel.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 11797
 */
@Controller
@RequestMapping("pay")
@RequiredArgsConstructor
@Slf4j
public class PayController extends BaseController {


    private final AlipayProperties alipayConfig;

    private final OrderService orderService;


    /**
     * 支付宝支付
     */
    @SneakyThrows
    @PostMapping("aliPay")
    public void aliPay(Integer payAmount, HttpServletRequest request, HttpServletResponse httpResponse) {

        UserDetails userDetails = getUserDetails(request);
        if (userDetails == null) {
            //未登录，跳转到登陆页面
            httpResponse.sendRedirect("/user/login.html?originUrl=/pay/index.html");
        } else {
            //创建充值订单
            Long outTradeNo = orderService.createPayOrder((byte) 1, payAmount, userDetails.getId());
            //获得初始化的AlipayClient
            AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig.getGatewayUrl(),
                alipayConfig.getAppId(), alipayConfig.getMerchantPrivateKey(), "json", alipayConfig.getCharset(),
                alipayConfig.getPublicKey(), alipayConfig.getSignType());
            String form;
            if (ThreadLocalUtil.getTemplateDir().contains("mobile")) {
                // 手机站
                AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
                alipayRequest.setReturnUrl(alipayConfig.getReturnUrl());
                //在公共参数中设置回跳和通知地址
                alipayRequest.setNotifyUrl(alipayConfig.getNotifyUrl());
                /******必传参数******/
                JSONObject bizContent = new JSONObject();
                //商户订单号，商家自定义，保持唯一性
                bizContent.put("out_trade_no", outTradeNo);
                //支付金额，最小值0.01元
                bizContent.put("total_amount", payAmount);
                //订单标题，不可使用特殊符号
                bizContent.put("subject", "小说精品屋-plus");

                /******可选参数******/
                //手机网站支付默认传值FAST_INSTANT_TRADE_PAY
                bizContent.put("product_code", "QUICK_WAP_WAY");

                alipayRequest.setBizContent(bizContent.toString());
                AlipayTradeWapPayResponse payResponse = alipayClient.pageExecute(alipayRequest);
                form = payResponse.getBody();
            } else {
                // 电脑站
                //创建API对应的request
                AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
                alipayRequest.setReturnUrl(alipayConfig.getReturnUrl());
                //在公共参数中设置回跳和通知地址
                alipayRequest.setNotifyUrl(alipayConfig.getNotifyUrl());
                //填充业务参数
                alipayRequest.setBizContent("{" +
                    "    \"out_trade_no\":\"" + outTradeNo + "\"," +
                    "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                    "    \"total_amount\":" + payAmount + "," +
                    "    \"subject\":\"小说精品屋-plus\"" +
                    "  }");
                //调用SDK生成表单
                AlipayTradePagePayResponse payResponse = alipayClient.pageExecute(alipayRequest);
                form = payResponse.getBody();

            }

            httpResponse.setContentType("text/html;charset=utf-8");
            //直接将完整的表单html输出到页面
            httpResponse.getWriter().write(form);
            httpResponse.getWriter().flush();
            httpResponse.getWriter().close();
        }


    }

    /**
     * 支付宝支付通知
     */
    @SneakyThrows
    @RequestMapping("aliPay/notify")
    public void aliPayNotify(HttpServletRequest request, HttpServletResponse httpResponse) {

        PrintWriter out = httpResponse.getWriter();

        //获取支付宝POST过来的信息
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                    : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }

        //验证签名
        boolean signVerified = AlipaySignature.rsaCheckV1(params, alipayConfig.getPublicKey(),
            alipayConfig.getCharset(), alipayConfig.getSignType());

        if (signVerified) {
            //验证成功
            //商户订单号
            String outTradeNo = new String(request.getParameter("out_trade_no").getBytes(StandardCharsets.ISO_8859_1),
                StandardCharsets.UTF_8);

            //支付宝交易号
            String tradeNo = new String(request.getParameter("trade_no").getBytes(StandardCharsets.ISO_8859_1),
                StandardCharsets.UTF_8);

            //交易状态
            String tradeStatus = new String(request.getParameter("trade_status").getBytes(StandardCharsets.ISO_8859_1),
                StandardCharsets.UTF_8);

            if ("TRADE_SUCCESS".equals(tradeStatus)) {
                //支付成功
                orderService.updatePayOrder(Long.parseLong(outTradeNo), tradeNo, 1);
            }

            out.println("success");

        } else {//验证失败
            out.println("fail");

        }

    }

}
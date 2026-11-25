package com.java2nb.novel.controller;

import com.java2nb.novel.core.bean.UserDetails;
import com.java2nb.novel.core.enums.ResponseStatus;
import com.java2nb.novel.service.UserOrderService;
import io.github.xxyopen.model.resp.RestResult;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户充值记录与订阅明细
 *
 * @author 11797
 */
@RestController
@RequestMapping("user/finance")
@RequiredArgsConstructor
public class UserFinanceController extends BaseController {

    private final UserOrderService userOrderService;

    /**
     * 分页查询我的充值记录
     */
    @GetMapping("rechargeRecord")
    public RestResult<?> rechargeRecord(@RequestParam(value = "curr", defaultValue = "1") int page,
        @RequestParam(value = "limit", defaultValue = "10") int pageSize, HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(request);
        if (userDetails == null) {
            return RestResult.fail(ResponseStatus.NO_LOGIN);
        }
        return RestResult.ok(userOrderService.listRechargeRecordByPage(userDetails.getId(), page, pageSize));
    }

    /**
     * 分页查询我的章节订阅明细
     */
    @GetMapping("subscribeDetail")
    public RestResult<?> subscribeDetail(@RequestParam(value = "curr", defaultValue = "1") int page,
        @RequestParam(value = "limit", defaultValue = "10") int pageSize, HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(request);
        if (userDetails == null) {
            return RestResult.fail(ResponseStatus.NO_LOGIN);
        }
        return RestResult.ok(userOrderService.listSubscribeDetailByPage(userDetails.getId(), page, pageSize));
    }
}

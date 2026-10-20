package com.java2nb.novel.controller;

import com.java2nb.novel.service.OrderService;
import com.java2nb.novel.vo.AdminOrderPayVO;
import io.github.xxyopen.model.page.PageBean;
import io.github.xxyopen.model.resp.RestResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单管理Controller
 *
 * @author Administrator
 */
@RestController
@RequestMapping("order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("pay/list")
    public RestResult<PageBean<AdminOrderPayVO>> payList(@RequestParam(required = false) String keyword,
        @RequestParam(required = false) Long userId,
        @RequestParam(value = "curr", defaultValue = "1") int page,
        @RequestParam(value = "limit", defaultValue = "10") int pageSize) {
        return RestResult.ok(orderService.payList(keyword, userId, page, pageSize));
    }

    @GetMapping("pay/get")
    public RestResult<AdminOrderPayVO> payGet(@RequestParam Long id) {
        return RestResult.ok(orderService.payGet(id));
    }

    @PostMapping("pay/remove")
    public RestResult<Void> payRemove(@RequestParam Long id) {
        orderService.payRemove(id);
        return RestResult.ok();
    }

    @PostMapping("pay/batchRemove")
    public RestResult<Void> payBatchRemove(@RequestParam String ids) {
        orderService.payBatchRemove(ids);
        return RestResult.ok();
    }

}

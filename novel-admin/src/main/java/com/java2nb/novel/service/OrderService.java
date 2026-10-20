package com.java2nb.novel.service;

import com.github.pagehelper.PageHelper;
import com.java2nb.novel.mapper.AdminOrderPayQueryMapper;
import com.java2nb.novel.mapper.OrderPayMapper;
import com.java2nb.novel.vo.AdminOrderPayVO;
import io.github.xxyopen.model.page.PageBean;
import io.github.xxyopen.model.page.builder.pagehelper.PageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.java2nb.novel.core.utils.AdminUtil.biz;

/**
 * 订单管理Service
 *
 * @author Administrator
 */
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderPayMapper orderPayMapper;

    private final AdminOrderPayQueryMapper orderPayQueryMapper;

    public PageBean<AdminOrderPayVO> payList(String keyword, Long userId, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        List<AdminOrderPayVO> list = orderPayQueryMapper.listAll(keyword, userId);
        return PageBuilder.build(list);
    }

    public AdminOrderPayVO payGet(Long id) {
        return orderPayQueryMapper.getById(id);
    }

    public void payRemove(Long id) {
        throw biz("支付订单不支持删除");
    }

    public void payBatchRemove(String ids) {
        throw biz("支付订单不支持删除");
    }

}

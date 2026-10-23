package com.java2nb.novel.service;

import com.github.pagehelper.PageHelper;
import com.java2nb.novel.entity.AuthorIncome;
import com.java2nb.novel.mapper.AdminAuthorIncomeQueryMapper;
import com.java2nb.novel.mapper.AuthorIncomeDynamicSqlSupport;
import com.java2nb.novel.mapper.AuthorIncomeMapper;
import com.java2nb.novel.vo.AdminAuthorIncomeVO;
import io.github.xxyopen.model.page.PageBean;
import io.github.xxyopen.model.page.builder.pagehelper.PageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.java2nb.novel.core.utils.AdminUtil.biz;
import static com.java2nb.novel.core.utils.AdminUtil.parseIds;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;
import static org.mybatis.dynamic.sql.SqlBuilder.isIn;

/**
 * 作家收入管理Service：稿费月收入列表与结算（pay_status：0 待结算，1 已结算）
 *
 * @author Administrator
 */
@Service
@RequiredArgsConstructor
public class AuthorIncomeService {

    private final AuthorIncomeMapper authorIncomeMapper;

    private final AdminAuthorIncomeQueryMapper authorIncomeQueryMapper;

    /**
     * 支付状态：已结算
     */
    private static final byte PAY_STATUS_SETTLED = 1;

    public PageBean<AdminAuthorIncomeVO> incomeList(String keyword, Byte payStatus, String month, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        List<AdminAuthorIncomeVO> list = authorIncomeQueryMapper.listAll(keyword, payStatus, month);
        return PageBuilder.build(list);
    }

    /**
     * 结算单条稿费收入：置为已结算。已是结算状态的不重复处理。
     *
     * @param id 收入记录ID
     */
    public void incomeSettle(Long id) {
        AuthorIncome income = authorIncomeMapper.selectByPrimaryKey(id).orElseThrow(() -> biz("收入记录不存在"));
        if (income.getPayStatus() != null && income.getPayStatus() == PAY_STATUS_SETTLED) {
            throw biz("该笔稿费已结算，请勿重复操作");
        }
        markSettled(List.of(id));
    }

    /**
     * 批量结算：仅对当前待结算的记录生效，返回本次实际结算数量。
     *
     * @param ids 逗号分隔的收入记录ID
     * @return 实际结算的记录数
     */
    public int incomeBatchSettle(String ids) {
        List<Long> idList = parseIds(ids);
        if (idList.isEmpty()) {
            return 0;
        }
        // 过滤掉已结算记录，避免重复统计数量与无意义更新
        List<Long> pendingIds = authorIncomeMapper.select(c -> c
                .where(AuthorIncomeDynamicSqlSupport.id, isIn(idList))
                .and(AuthorIncomeDynamicSqlSupport.payStatus, isEqualTo((byte) 0)))
                .stream().map(AuthorIncome::getId).toList();
        if (pendingIds.isEmpty()) {
            throw biz("所选记录均已结算");
        }
        markSettled(pendingIds);
        return pendingIds.size();
    }

    /**
     * 一键结算全部待结算稿费：将所有 pay_status=0 的记录置为已结算。
     *
     * @return 本次结算的记录数
     */
    public int incomeSettleAll() {
        List<Long> pendingIds = authorIncomeMapper.select(c -> c
                .where(AuthorIncomeDynamicSqlSupport.payStatus, isEqualTo((byte) 0)))
                .stream().map(AuthorIncome::getId).toList();
        if (pendingIds.isEmpty()) {
            throw biz("当前没有待结算的稿费记录");
        }
        markSettled(pendingIds);
        return pendingIds.size();
    }

    /**
     * 将指定记录置为已结算
     *
     * @param ids 收入记录ID列表
     */
    private void markSettled(List<Long> ids) {
        authorIncomeMapper.update(u -> u
                .set(AuthorIncomeDynamicSqlSupport.payStatus).equalTo(PAY_STATUS_SETTLED)
                .where(AuthorIncomeDynamicSqlSupport.id, isIn(ids)));
    }

}

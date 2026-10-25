package com.java2nb.novel.service;

import com.github.pagehelper.PageHelper;
import com.java2nb.novel.entity.AuthorIncome;
import com.java2nb.novel.entity.UserMessage;
import com.java2nb.novel.mapper.AdminAuthorIncomeQueryMapper;
import com.java2nb.novel.mapper.AuthorIncomeDynamicSqlSupport;
import com.java2nb.novel.mapper.AuthorIncomeMapper;
import com.java2nb.novel.vo.AdminAuthorIncomeVO;
import io.github.xxyopen.model.page.PageBean;
import io.github.xxyopen.model.page.builder.pagehelper.PageBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
@Slf4j
public class AuthorIncomeService {

    private final AuthorIncomeMapper authorIncomeMapper;

    private final AdminAuthorIncomeQueryMapper authorIncomeQueryMapper;

    private final MessageService messageService;

    /**
     * 支付状态：已结算
     */
    private static final byte PAY_STATUS_SETTLED = 1;

    /**
     * 汇总行 bookId 约定值
     */
    private static final long BOOK_ID_ALL = 0L;

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
        notifySettled(List.of(income));
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
        List<AuthorIncome> pendingList = authorIncomeMapper.select(c -> c
            .where(AuthorIncomeDynamicSqlSupport.id, isIn(idList))
            .and(AuthorIncomeDynamicSqlSupport.payStatus, isEqualTo((byte) 0)));
        if (pendingList.isEmpty()) {
            throw biz("所选记录均已结算");
        }
        List<Long> pendingIds = pendingList.stream().map(AuthorIncome::getId).toList();
        markSettled(pendingIds);
        notifySettled(pendingList);
        return pendingIds.size();
    }

    /**
     * 一键结算全部待结算稿费：将所有 pay_status=0 的记录置为已结算。
     *
     * @return 本次结算的记录数
     */
    public int incomeSettleAll() {
        List<AuthorIncome> pendingList = authorIncomeMapper.select(c -> c
            .where(AuthorIncomeDynamicSqlSupport.payStatus, isEqualTo((byte) 0)));
        if (pendingList.isEmpty()) {
            throw biz("当前没有待结算的稿费记录");
        }
        List<Long> pendingIds = pendingList.stream().map(AuthorIncome::getId).toList();
        markSettled(pendingIds);
        notifySettled(pendingList);
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

    /**
     * 结算成功后向作者推送站内系统通知（每作者每月一条，汇总当月税后总额）。
     * 通知失败不影响结算主流程。
     *
     * @param settled 本次由待结算变为已结算的收入记录
     */
    private void notifySettled(List<AuthorIncome> settled) {
        // 按 (userId, incomeMonth) 分组，保留插入顺序
        Map<String, List<AuthorIncome>> groups = new LinkedHashMap<>();
        for (AuthorIncome income : settled) {
            if (income.getUserId() == null || income.getIncomeMonth() == null) {
                continue;
            }
            String monthKey = new SimpleDateFormat("yyyy-MM").format(income.getIncomeMonth());
            groups.computeIfAbsent(income.getUserId() + "@" + monthKey, k -> new java.util.ArrayList<>()).add(income);
        }
        for (List<AuthorIncome> group : groups.values()) {
            try {
                AuthorIncome sample = group.get(0);
                // 优先取汇总行(bookId=0)的税后额作为当月总额，无则对明细求和
                long afterTax = group.stream()
                    .filter(i -> i.getBookId() != null && i.getBookId() == BOOK_ID_ALL)
                    .map(AuthorIncome::getAfterTaxIncome)
                    .findFirst()
                    .orElseGet(() -> group.stream()
                        .mapToLong(i -> i.getAfterTaxIncome() == null ? 0L : i.getAfterTaxIncome()).sum());
                String month = new SimpleDateFormat("yyyy年MM月").format(sample.getIncomeMonth());
                String yuan = BigDecimal.valueOf(afterTax)
                    .divide(BigDecimal.valueOf(100), 2, java.math.RoundingMode.HALF_UP).toPlainString();
                UserMessage message = new UserMessage();
                message.setUserId(sample.getUserId());
                message.setBookId(BOOK_ID_ALL);
                message.setContent("您" + month + "的稿费已完成结算，税后约" + yuan + "元，可前往“稿费收入”查看明细。");
                messageService.send(message);
            } catch (Exception e) {
                log.warn("稿费结算通知发送失败，userId={}", group.get(0).getUserId(), e);
            }
        }
    }

}

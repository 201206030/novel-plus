package com.java2nb.novel.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 稿费分成规则展示VO（供作者后台收入明细页说明用）
 *
 * @author 11797
 */
@Data
public class IncomeRuleVO {

    /**
     * 订阅收入分成比例（作者分成占比）
     */
    private BigDecimal shareProportion;

    /**
     * 屋币兑换人民币比例
     */
    private BigDecimal exchangeProportion;

    /**
     * 税率
     */
    private BigDecimal taxRate;

}

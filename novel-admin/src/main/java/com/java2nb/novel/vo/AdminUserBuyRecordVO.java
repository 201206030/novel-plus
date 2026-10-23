package com.java2nb.novel.vo;

import com.java2nb.novel.entity.UserBuyRecord;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 购买记录管理列表VO
 *
 * @author Administrator
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AdminUserBuyRecordVO extends UserBuyRecord {

    /**
     * 购买用户显示名（昵称优先，否则用户名）
     */
    private String userName;

}

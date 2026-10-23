package com.java2nb.novel.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 会员管理写入Mapper（书币原子增减，避免与前台购章扣款并发覆盖）
 *
 * @author Administrator
 */
@Mapper
public interface AdminMemberCmdMapper {

    /**
     * 原子调整用户书币：account_balance += amount，且保证调整后不为负
     *
     * @param userId 用户ID
     * @param amount 增减书币数（正数补发，负数扣除）
     * @return 影响行数（0 表示用户不存在或余额不足）
     */
    int adjustBalance(@Param("userId") Long userId, @Param("amount") Long amount);

}

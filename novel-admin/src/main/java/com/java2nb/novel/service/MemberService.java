package com.java2nb.novel.service;

import com.github.pagehelper.PageHelper;
import com.java2nb.novel.entity.User;
import com.java2nb.novel.entity.UserFeedback;
import com.java2nb.novel.mapper.AdminMemberCmdMapper;
import com.java2nb.novel.mapper.AdminUserFeedbackQueryMapper;
import com.java2nb.novel.mapper.UserDynamicSqlSupport;
import com.java2nb.novel.mapper.UserFeedbackDynamicSqlSupport;
import com.java2nb.novel.mapper.UserFeedbackMapper;
import com.java2nb.novel.mapper.UserMapper;
import com.java2nb.novel.vo.AdminUserFeedbackVO;
import io.github.xxyopen.model.page.PageBean;
import io.github.xxyopen.model.page.builder.pagehelper.PageBuilder;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static com.java2nb.novel.core.utils.AdminUtil.biz;
import static com.java2nb.novel.core.utils.AdminUtil.like;
import static com.java2nb.novel.core.utils.AdminUtil.parseIds;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;
import static org.mybatis.dynamic.sql.SqlBuilder.isIn;
import static org.mybatis.dynamic.sql.SqlBuilder.isLike;
import static org.mybatis.dynamic.sql.SqlBuilder.isLikeWhenPresent;

/**
 * 会员管理Service：会员列表/会员反馈
 *
 * @author Administrator
 */
@Service
@RequiredArgsConstructor
public class MemberService {

    /**
     * 会员状态：正常
     */
    private static final byte USER_STATUS_NORMAL = 0;

    /**
     * 会员状态：封禁（禁用）
     */
    private static final byte USER_STATUS_BANNED = 1;

    /**
     * 单次书币调整上限（绝对值），防止误操作大额加减
     */
    private static final long MAX_ADJUST_AMOUNT = 1_000_000L;

    private final UserMapper userMapper;

    private final UserFeedbackMapper userFeedbackMapper;

    private final AdminUserFeedbackQueryMapper userFeedbackQueryMapper;

    private final AdminMemberCmdMapper memberCmdMapper;

    public PageBean<User> userList(String keyword, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        List<User> list = userMapper.select(c -> {
            if (StringUtils.isNotBlank(keyword)) {
                // 用户名或昵称任一匹配
                String likeVal = like(keyword);
                return c.where(UserDynamicSqlSupport.username, isLike(likeVal))
                        .or(UserDynamicSqlSupport.nickName, isLike(likeVal))
                        .orderBy(UserDynamicSqlSupport.createTime.descending());
            }
            return c.orderBy(UserDynamicSqlSupport.createTime.descending());
        });
        list.forEach(user -> user.setPassword(null));
        return PageBuilder.build(list);
    }

    public User userGet(Long id) {
        User user = userMapper.selectByPrimaryKey(id).orElseThrow(() -> biz("会员不存在"));
        user.setPassword(null);
        return user;
    }

    public void userSave(User user) {
        throw biz("会员列表仅供查看，不支持新增");
    }

    public void userUpdate(User user) {
        throw biz("会员列表仅供查看，不支持修改");
    }

    public void userRemove(Long id) {
        throw biz("会员列表仅供查看，不支持删除");
    }

    public void userBatchRemove(String ids) {
        throw biz("会员列表仅供查看，不支持删除");
    }

    /**
     * 封禁/解封会员账号。封禁后前台登录会被拦截（已登录的 JWT 到期前仍有效，属可接受窗口）
     *
     * @param id     会员ID
     * @param status 0正常（解封），1封禁
     */
    public void userBan(Long id, Byte status) {
        if (status == null || (status != USER_STATUS_NORMAL && status != USER_STATUS_BANNED)) {
            throw biz("状态参数有误，仅支持：0正常 / 1封禁");
        }
        User user = userMapper.selectByPrimaryKey(id).orElseThrow(() -> biz("会员不存在"));
        if (user.getStatus() != null && user.getStatus().equals(status)) {
            throw biz(status == USER_STATUS_BANNED ? "该会员已是封禁状态" : "该会员已是正常状态");
        }
        userMapper.update(u -> u.set(UserDynamicSqlSupport.status).equalTo(status)
                .set(UserDynamicSqlSupport.updateTime).equalTo(new Date())
                .where(UserDynamicSqlSupport.id, isEqualTo(id)));
    }

    /**
     * 调整会员书币（补发/扣除）。SQL 原子增减并保证余额不为负，
     * 避免与前台购章扣款并发时读-算-写互相覆盖
     *
     * @param id     会员ID
     * @param amount 增减书币数，正数补发、负数扣除
     * @return 调整后的最新余额
     */
    public long userAdjustBalance(Long id, Long amount) {
        if (amount == null || amount == 0) {
            throw biz("调整书币数不能为0");
        }
        if (Math.abs(amount) > MAX_ADJUST_AMOUNT) {
            throw biz("单次调整不能超过 " + MAX_ADJUST_AMOUNT + " 书币");
        }
        userMapper.selectByPrimaryKey(id).orElseThrow(() -> biz("会员不存在"));
        int rows = memberCmdMapper.adjustBalance(id, amount);
        if (rows == 0) {
            throw biz("扣除失败：会员余额不足");
        }
        return userMapper.selectByPrimaryKey(id).orElseThrow(() -> biz("会员不存在")).getAccountBalance();
    }

    public PageBean<AdminUserFeedbackVO> feedbackList(String keyword, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        List<AdminUserFeedbackVO> list = userFeedbackQueryMapper.listAll(keyword);
        return PageBuilder.build(list);
    }

    public UserFeedback feedbackGet(Long id) {
        return userFeedbackMapper.selectByPrimaryKey(id).orElseThrow(() -> biz("反馈不存在"));
    }

    public void feedbackSave(UserFeedback feedback) {
        throw biz("会员反馈仅供查看，不支持新增");
    }

    public void feedbackUpdate(UserFeedback feedback) {
        throw biz("会员反馈仅供查看，不支持修改");
    }

    public void feedbackRemove(Long id) {
        throw biz("会员反馈仅供查看，不支持删除");
    }

    public void feedbackBatchRemove(String ids) {
        throw biz("会员反馈仅供查看，不支持删除");
    }

}

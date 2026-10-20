package com.java2nb.novel.service;

import com.github.pagehelper.PageHelper;
import com.java2nb.novel.entity.User;
import com.java2nb.novel.entity.UserFeedback;
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

    private final UserMapper userMapper;

    private final UserFeedbackMapper userFeedbackMapper;

    private final AdminUserFeedbackQueryMapper userFeedbackQueryMapper;

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

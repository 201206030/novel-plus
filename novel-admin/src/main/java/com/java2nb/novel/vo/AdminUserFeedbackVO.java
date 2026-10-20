package com.java2nb.novel.vo;

import com.java2nb.novel.entity.UserFeedback;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 会员反馈管理列表VO
 *
 * @author Administrator
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AdminUserFeedbackVO extends UserFeedback {

    /**
     * 反馈用户昵称
     */
    private String userName;

}

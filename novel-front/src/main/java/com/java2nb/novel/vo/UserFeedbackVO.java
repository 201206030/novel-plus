package com.java2nb.novel.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.java2nb.novel.entity.UserFeedback;
import lombok.Data;

import java.util.Date;

/**
 * @author Administrator
 */
@Data
public class UserFeedbackVO extends UserFeedback {

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Override
    public String toString() {
        return super.toString();
    }
}

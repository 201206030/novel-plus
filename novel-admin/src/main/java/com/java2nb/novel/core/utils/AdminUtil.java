package com.java2nb.novel.core.utils;

import io.github.xxyopen.model.resp.IResultCode;
import io.github.xxyopen.web.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 后台管理通用工具
 *
 * @author Administrator
 */
public final class AdminUtil {

    private AdminUtil() {
    }

    /**
     * 构造业务异常（带自定义错误信息）
     *
     * @param msg 错误信息
     * @return 业务异常
     */
    public static BusinessException biz(String msg) {
        return new BusinessException(new IResultCode() {
            @Override
            public int getCode() {
                return 9001;
            }

            @Override
            public String getMsg() {
                return msg;
            }
        });
    }

    /**
     * 解析逗号分隔的ID串
     *
     * @param ids 逗号分隔的ID串
     * @return ID集合
     */
    public static List<Long> parseIds(String ids) {
        List<Long> idList = new ArrayList<>();
        if (StringUtils.isBlank(ids)) {
            return idList;
        }
        for (String id : ids.split(",")) {
            if (StringUtils.isNotBlank(id)) {
                idList.add(Long.valueOf(id.trim()));
            }
        }
        return idList;
    }

    /**
     * 构造模糊匹配参数：关键字为空时返回 null，配合 {@code isLikeWhenPresent}
     * 可将该条件从 SQL 的 where 子句中剔除（不作为查询条件）；
     * 注意：单一条件的查询需先判断关键字非空再拼接 where。
     *
     * @param keyword 关键字
     * @return like条件，关键字为空时返回 null
     */
    public static String like(String keyword) {
        return StringUtils.isBlank(keyword) ? null : "%" + keyword.trim() + "%";
    }

}

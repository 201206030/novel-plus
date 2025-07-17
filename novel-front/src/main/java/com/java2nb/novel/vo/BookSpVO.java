package com.java2nb.novel.vo;

import com.java2nb.novel.core.vo.SortOrderVO;
import lombok.Data;

import java.util.Date;

/**
 * 小说搜索参数
 * @author 11797
 */
@Data
public class BookSpVO extends SortOrderVO {

    private String keyword;

    private Byte workDirection;

    private Integer catId;

    private Byte isVip;

    private Byte bookStatus;

    private Integer wordCountMin;

    private Integer wordCountMax;

    private Date updateTimeMin;

    private Long updatePeriod;


}

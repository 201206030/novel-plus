package com.java2nb.novel.vo;

import com.java2nb.novel.core.vo.SortOrderVO;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.Date;

/**
 * 小说搜索参数
 * @author 11797
 */
@Data
public class BookSpVO {

    private String keyword;

    private Byte workDirection;

    private Integer catId;

    private Byte isVip;

    private Byte bookStatus;

    private Integer wordCountMin;

    private Integer wordCountMax;

    private Date updateTimeMin;

    private Long updatePeriod;

    @Pattern(regexp = "^(last_index_update_time|word_count|visit_count)$")
    private String sort;


}

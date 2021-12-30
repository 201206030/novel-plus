package com.java2nb.novel.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.java2nb.novel.entity.CrawlSource;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author Administrator
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CrawlSourceVO extends CrawlSource{

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    private Date updateTime;



    @Override
    public String toString() {
        return super.toString();
    }
}

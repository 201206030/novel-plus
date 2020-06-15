package com.java2nb.novel.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.java2nb.novel.entity.CrawlSingleTask;
import com.java2nb.novel.entity.CrawlSource;
import lombok.Data;

import java.util.Date;

/**
 * @author Administrator
 */
@Data
public class CrawlSingleTaskVO extends CrawlSingleTask {

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    private Date createTime;




    @Override
    public String toString() {
        return super.toString();
    }
}

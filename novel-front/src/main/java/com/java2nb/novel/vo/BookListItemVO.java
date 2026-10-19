package com.java2nb.novel.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.java2nb.novel.core.serialize.TimeAgoFormatSerialize;
import lombok.Data;

import java.util.Date;

/**
 * 书单条目VO
 *
 * @author 11797
 */
@Data
public class BookListItemVO {

    private Long bookId;

    private String bookName;

    private String authorName;

    private String picUrl;

    private String catName;

    /**
     * 0连载中 1已完结
     */
    private Byte bookStatus;

    /**
     * 推荐语
     */
    private String remark;

    @JsonSerialize(using = TimeAgoFormatSerialize.class)
    private Date createTime;
}

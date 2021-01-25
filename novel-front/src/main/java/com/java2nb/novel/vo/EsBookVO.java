package com.java2nb.novel.vo;

import lombok.Data;

/**
 * @author Administrator
 */
@Data
public class EsBookVO  {

    private Long id;

    
    private Byte workDirection;

    
    private Integer catId;

    
    private String catName;

    
    private String picUrl;

    
    private String bookName;

    
    private Long authorId;

    
    private String authorName;

    
    private String bookDesc;

    
    private Float score;

    
    private Byte bookStatus;

    
    private Long visitCount;

    
    private Integer wordCount;

    
    private Integer commentCount;

    
    private Long lastIndexId;

    
    private String lastIndexName;

    private String lastIndexUpdateTime;

    
    private Byte isVip;

    
    private Byte status;

    

    
    private Integer crawlSourceId;

    
    private String crawlBookId;

    

    private Byte crawlIsStop;


}

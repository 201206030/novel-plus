package com.java2nb.novel.core.bean;

import com.github.pagehelper.PageInfo;
import lombok.Data;

import java.util.List;

/**
 * 封装通用分页数据,接收PageHelper、SpringData等框架的分页数据，转换成通用的PageBean对象
 * @author xiongxiaoyang
 * @version 1.0
 * @since 2021/2/4
 * @param <T> 分页集合类型
 */
@Data
public class PageBean<T> {

    private Integer pageNum;
    private Integer pageSize;
    private Long total;
    private List<? extends T> list;


    /**
     * 该构造函数用于PageHelper工具进行分页查询的场景
     * 接收PageHelper分页后的list
     */
    public PageBean(List<T> list){
        PageInfo<T> pageInfo = new PageInfo<>(list);
        this.pageNum = pageInfo.getPageNum();
        this.pageSize = pageInfo.getPageSize();
        this.total = pageInfo.getTotal();
        this.list = pageInfo.getList();

    }

    /**
     * 该构造函数用于通用分页查询的场景
     * 接收普通分页数据和普通集合
     */
    public PageBean(Integer pageNum, Integer pageSize, Long total, List<T> list) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
        this.list = list;
    }


    //TODO 使用其他的分页工具或框架进行分页查询的场景



}

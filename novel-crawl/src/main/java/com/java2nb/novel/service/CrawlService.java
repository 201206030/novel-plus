package com.java2nb.novel.service;

import io.github.xxyopen.model.page.PageBean;
import com.java2nb.novel.core.crawl.RuleBean;
import com.java2nb.novel.entity.CrawlSingleTask;
import com.java2nb.novel.entity.CrawlSource;

import java.util.List;

/**
 * @author Administrator
 */
public interface CrawlService {

    /**
     * 新增爬虫源
     * @param source 爬虫源提交的数据对象
     * */
    void addCrawlSource(CrawlSource source);

    /**
     * 修改爬虫源
     * @param source
     */
    void updateCrawlSource(CrawlSource source);
    /**
     * 爬虫源分页列表
     * @param page 当前页码
     * @param pageSize 分页大小
     *@return 爬虫源分页数据
     * */
    PageBean<CrawlSource> listCrawlByPage(int page, int pageSize);

    /**
     * 开启或停止爬虫
     * @param sourceId 爬虫源ID
     * @param sourceStatus 状态，0关闭，1开启
     * */
    void openOrCloseCrawl(Integer sourceId, Byte sourceStatus);

    /**
     * 更新爬虫状态
     * @param sourceId 爬虫源ID
     * @param sourceStatus 状态，0关闭，1开启
     * */
    void updateCrawlSourceStatus(Integer sourceId, Byte sourceStatus);

    /**
     * 采集并保存小说
     * @param catId 分类ID
     * @param bookId 小说ID
     * @param sourceId 源ID
     * @param ruleBean 采集规则\
     * @return true:成功，false:失败
     * */
    boolean parseBookAndSave(int catId, RuleBean ruleBean, Integer sourceId, String bookId);

    /**
     * 根据爬虫状态查询爬虫源集合
     * @param sourceStatus 状态，0关闭，1开启
     * @return 返回爬虫源集合
     * */
    List<CrawlSource> queryCrawlSourceByStatus(Byte sourceStatus);

    /**
     * 根据分类ID和规则解析分类列表
     * @param catId 分类ID
     * @param ruleBean 规则对象
     * @param sourceId 爬虫源ID
     */
    void parseBookList(int catId, RuleBean ruleBean, Integer sourceId);


    /**
     * 查询爬虫源
     * @param sourceId 源ID
     * @return 源信息
     * */
    CrawlSource queryCrawlSource(Integer sourceId);

    /**
     * 新增单本采集任务
     * @param singleTask 任务信息对象
     * */
    void addCrawlSingleTask(CrawlSingleTask singleTask);

    /**
     * 单本采集任务分页列表查询
     * @param page 当前页码
     * @param pageSize 分页大小
     * @return 单本采集任务分页数据
     * */
    PageBean<CrawlSingleTask> listCrawlSingleTaskByPage(int page, int pageSize);

    /**
     * 删除采集任务
     * @param id 任务ID
     * */
    void delCrawlSingleTask(Long id);

    /**
     * 获取采集任务
     * @return 采集任务
     * */
    CrawlSingleTask getCrawlSingleTask();

    /**
     * 更新单本采集任务
     * @param task 采集任务
     * @param status 采集状态
     * */
    void updateCrawlSingleTask(CrawlSingleTask task, Byte status);

    /**
     * 获取采集规则详细
     * @param id
     * @return
     */
    CrawlSource getCrawlSource(Integer id);
}

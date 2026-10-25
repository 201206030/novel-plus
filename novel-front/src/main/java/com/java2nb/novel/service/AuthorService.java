package com.java2nb.novel.service;


import com.java2nb.novel.entity.Author;
import com.java2nb.novel.entity.AuthorIncome;
import com.java2nb.novel.entity.AuthorIncomeDetail;
import com.java2nb.novel.vo.BookIncomeSummaryVO;
import com.java2nb.novel.vo.IncomeRuleVO;
import io.github.xxyopen.model.page.PageBean;

import java.util.Date;
import java.util.List;

/**
 * @author 11797
 */
public interface AuthorService {

    /**
     * 校验笔名是否存在
     *
     * @param penName 校验的笔名
     * @return true：存在该笔名，false: 不存在该笔名
     */
    Boolean checkPenName(String penName);

    /**
     * 校验笔名是否存在（可排除指定作者自身，供资料页改笔名查重用）
     *
     * @param penName          校验的笔名
     * @param excludeAuthorId  需要排除的作者ID（为空则不排除）
     * @return true：存在该笔名，false：不存在该笔名
     */
    Boolean checkPenName(String penName, Long excludeAuthorId);

    /**
     * 查询作家完整信息（含头像/简介等全部字段）
     *
     * @param userId 用户ID
     * @return 作家对象
     */
    Author queryAuthorAll(Long userId);

    /**
     * 根据作家ID（author.id，即 book.author_id）查询作家完整信息
     *
     * @param authorId 作家主键
     * @return 作家对象，不存在返回 null
     */
    Author queryAuthorById(Long authorId);

    /**
     * 更新作家资料（仅笔名、头像、简介、联系方式，作品方向与状态不可改）
     *
     * @param userId 用户ID
     * @param form   待更新字段
     */
    void updateAuthorInfo(Long userId, Author form);

    /**
     * 查询稿费分成规则（供收入明细页说明用）
     *
     * @return 分成规则
     */
    IncomeRuleVO incomeRule();

    /**
     * 按作品汇总订阅收入
     *
     * @param userId    用户ID
     * @param bookId    作品ID（0表示全部）
     * @param startTime 收入起始日期
     * @param endTime   收入结束日期
     * @return 每个作品的汇总
     */
    List<BookIncomeSummaryVO> bookIncomeSummary(Long userId, Long bookId, Date startTime, Date endTime);

    /**
     * 作家注册
     *
     * @param userId 注册用户ID
     * @param author 注册信息
     * @return 返回错误信息
     */
    String register(Long userId, Author author);

    /**
     * 判断是否是作家
     *
     * @param userId 用户ID
     * @return true：是作家，false: 不是作家
     */
    Boolean isAuthor(Long userId);

    /**
     * 查询作家信息
     *
     * @param userId 用户ID
     * @return 作家对象
     */
    Author queryAuthor(Long userId);

    /**
     * 查询作家列表
     *
     * @param limit               查询条数
     * @param maxAuthorCreateTime 最大申请时间
     * @return 作家列表
     */
    List<Author> queryAuthorList(int limit, Date maxAuthorCreateTime);

    /**
     * 查询收入日统计是否入库
     *
     * @param bookId 作品ID
     * @param date   收入时间
     * @return true:已入库，false：未入库
     */
    boolean queryIsStatisticsDaily(Long bookId, Date date);


    /**
     * 保存日收入统计(按作品)
     *
     * @param authorIncomeDetail 收入详情
     */
    void saveDailyIncomeSta(AuthorIncomeDetail authorIncomeDetail);


    /**
     * 查询月收入统计是否入库
     *
     * @param bookId     作品ID
     * @param incomeDate 收入时间
     * @return true:已入库，false：未入库
     */
    boolean queryIsStatisticsMonth(Long bookId, Date incomeDate);

    boolean queryIsStatisticsMonth(Long authorId, Long bookId, Date incomeDate);

    /**
     * 查询时间段内总订阅额
     *
     * @param userId
     * @param bookId    作品ID
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 订阅额（屋币）
     */
    Long queryTotalAccount(Long userId, Long bookId, Date startTime, Date endTime);


    /**
     * 保存月收入统计
     *
     * @param authorIncome 收入详情
     */
    void saveAuthorIncomeSta(AuthorIncome authorIncome);

    /**
     * 查询收入日统计是否入库
     *
     * @param authorId 作家ID
     * @param bookId   作品ID
     * @param date     收入时间
     * @return true:已入库，false：未入库
     */
    boolean queryIsStatisticsDaily(Long authorId, Long bookId, Date date);

    /**
     * 作家日收入统计数据分页列表查询
     *
     * @param userId
     * @param page      页码
     * @param pageSize  分页大小
     * @param bookId    小说ID
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 日收入统计数据分页数据
     */
    PageBean<AuthorIncomeDetail> listIncomeDailyByPage(int page, int pageSize, Long userId, Long bookId, Date startTime,
        Date endTime);


    /**
     * 作家月收入统计数据分页列表查询
     *
     * @param page     页码
     * @param pageSize 分页大小
     * @param userId   用户ID
     * @param bookId   小说ID
     * @return 分页数据
     */
    PageBean<AuthorIncome> listIncomeMonthByPage(int page, int pageSize, Long userId, Long bookId);
}

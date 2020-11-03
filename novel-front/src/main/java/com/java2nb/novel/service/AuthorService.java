package com.java2nb.novel.service;


import com.java2nb.novel.entity.Author;
import com.java2nb.novel.entity.AuthorIncome;
import com.java2nb.novel.entity.AuthorIncomeDetail;
import com.java2nb.novel.entity.FriendLink;

import java.util.Date;
import java.util.List;

/**
 * @author 11797
 */
public interface AuthorService {

    /**
     * 校验笔名是否存在
     * @param penName 校验的笔名
     * @return true：存在该笔名，false: 不存在该笔名
     * */
    Boolean checkPenName(String penName);

    /**
     * 作家注册
     * @param userId 注册用户ID
     *@param author 注册信息
     * @return 返回错误信息
     * */
    String register(Long userId, Author author);

    /**
     * 判断是否是作家
     * @param userId 用户ID
     * @return true：是作家，false: 不是作家
     * */
    Boolean isAuthor(Long userId);

    /**
     * 查询作家信息
     * @param userId 用户ID
     * @return 作家对象
     * */
    Author queryAuthor(Long userId);

    /**
     * 查询作家列表
     * @return 作家列表
     * @param limit 查询条数
     * @param maxAuthorCreateTime 最大申请时间
     */
    List<Author> queryAuthorList(int limit, Date maxAuthorCreateTime);

    /**
     * 查询收入日统计是否入库
     * @param bookId 作品ID
     * @param date 收入时间
     * @return true:已入库，false：未入库
     */
    boolean queryIsStatisticsDaily(Long bookId, Date date);


    /**
     * 保存日收入统计(按作品)
     * @param authorIncomeDetail 收入详情
     * */
    void saveDailyIncomeSta(AuthorIncomeDetail authorIncomeDetail);



    /**
     * 查询月收入统计是否入库
     * @param bookId 作品ID
     * @param incomeDate 收入时间
     * @return true:已入库，false：未入库
     * */
    boolean queryIsStatisticsMonth(Long bookId, Date incomeDate);

    /**
     * 查询时间段内总订阅额
     *
     * @param userId
     * @param bookId 作品ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 订阅额（屋币）
     * */
    Long queryTotalAccount(Long userId, Long bookId, Date startTime, Date endTime);


    /**
     * 保存月收入统计
     * @param authorIncome 收入详情
     * */
    void saveAuthorIncomeSta(AuthorIncome authorIncome);

    /**
     * 查询收入日统计是否入库
     * @param authorId 作家ID
     * @param bookId 作品ID
     * @param date 收入时间
     * @return true:已入库，false：未入库
     */
    boolean queryIsStatisticsDaily(Long authorId, Long bookId, Date date);

    /**
     *作家日收入统计数据分页列表查询
     * @param userId
     * @param page 页码
     * @param pageSize 分页大小
     * @param bookId 小说ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 日收入统计数据列表
     */
    List<AuthorIncomeDetail> listIncomeDailyByPage(int page, int pageSize, Long userId, Long bookId, Date startTime, Date endTime);


    /**
     * 作家月收入统计数据分页列表查询
     * @param page 页码
     * @param pageSize 分页大小
     * @param userId 用户ID
     * @param bookId 小说ID
     * @return
     * */
    List<AuthorIncome> listIncomeMonthByPage(int page, int pageSize, Long userId, Long bookId);
}

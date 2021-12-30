package com.java2nb.novel.core.schedule;


import com.java2nb.novel.core.utils.DateUtil;
import com.java2nb.novel.entity.Author;
import com.java2nb.novel.entity.AuthorIncomeDetail;
import com.java2nb.novel.entity.Book;
import com.java2nb.novel.service.AuthorService;
import com.java2nb.novel.service.BookService;
import com.java2nb.novel.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 作家日收入统计任务
 *
 * @author cd
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DailyIncomeStaSchedule {

    private final AuthorService authorService;

    private final UserService userService;

    private final BookService bookService;


    /**
     * 每天凌晨0点统计上一天数据
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void statistics() {

        //获取昨天的日期时间
        Date yesterday = DateUtil.getYesterday();
        //获取昨天的开始时间
        Date startTime = DateUtil.getDateStartTime(yesterday);
        //获取昨天的结束时间
        Date endTime = DateUtil.getDateEndTime(yesterday);

        //每次查询的作家数量
        int needAuthorNumber = 10;
        //查询出来的真实作家数量
        int realAuthorNumber;
        //每次查询最大申请时间
        Date maxAuthorCreateTime = new Date();
        do {
            //1.查询作家列表
            List<Author> authors = authorService.queryAuthorList(needAuthorNumber, maxAuthorCreateTime);
            realAuthorNumber = authors.size();
            for (Author author : authors) {
                maxAuthorCreateTime = author.getCreateTime();
                Long authorId = author.getId();
                Long userId = author.getUserId();
                //2.查询作家作品
                List<Book> books = bookService.queryBookList(authorId);

                int buyTotalMember = 0;
                int buyTotalCount = 0;
                int buyTotalAccount = 0;
                List<Long> bookIds = new ArrayList<>(books.size());
                for (Book book : books) {

                    Long bookId = book.getId();

                    //3.查询该作家作品昨日的订阅人数
                    int buyMember = userService.queryBuyMember(bookId, startTime, endTime);

                    int buyCount = 0;

                    int buyAccount = 0;


                    if (buyMember > 0) {
                        //4.查询该作家作品昨日的订阅次数
                        buyCount = userService.queryBuyCount(bookId, startTime, endTime);
                        //5.查询该作家作品昨日的订阅总额
                        buyAccount = userService.queryBuyAccount(bookId, startTime, endTime);
                    }

                    //6.判断该作家作品昨日收入数据是否统计入库
                    boolean isStatistics = authorService.queryIsStatisticsDaily(bookId, yesterday);
                    if (!isStatistics) {
                        //7.该作家作品昨日收入数据未统计入库,分作品统计数据入库
                        AuthorIncomeDetail authorIncomeDetail = new AuthorIncomeDetail();
                        authorIncomeDetail.setAuthorId(authorId);
                        authorIncomeDetail.setUserId(userId);
                        authorIncomeDetail.setBookId(bookId);
                        authorIncomeDetail.setIncomeDate(yesterday);
                        authorIncomeDetail.setIncomeNumber(buyMember);
                        authorIncomeDetail.setIncomeCount(buyCount);
                        authorIncomeDetail.setIncomeAccount(buyAccount);
                        authorIncomeDetail.setCreateTime(new Date());
                        authorService.saveDailyIncomeSta(authorIncomeDetail);
                    }


                    buyTotalCount += buyCount;
                    buyTotalAccount += buyAccount;
                    bookIds.add(bookId);

                }

                //8.判断该作家所有作品昨日收入数据是否统计入库
                boolean isStatistics = authorService.queryIsStatisticsDaily(authorId,0L, yesterday);
                if (!isStatistics) {
                    if (buyTotalCount > 0) {
                        //总订阅次数大于0，则订阅人数也大于0
                        buyTotalMember = userService.queryBuyTotalMember(bookIds, startTime, endTime);
                    }

                    //9.作家所有作品昨日收入数据统计入库
                    AuthorIncomeDetail authorIncomeAllDetail = new AuthorIncomeDetail();
                    authorIncomeAllDetail.setAuthorId(authorId);
                    authorIncomeAllDetail.setUserId(userId);
                    authorIncomeAllDetail.setBookId(0L);
                    authorIncomeAllDetail.setIncomeDate(yesterday);
                    authorIncomeAllDetail.setIncomeNumber(buyTotalMember);
                    authorIncomeAllDetail.setIncomeCount(buyTotalCount);
                    authorIncomeAllDetail.setIncomeAccount(buyTotalAccount);
                    authorIncomeAllDetail.setCreateTime(new Date());
                    authorService.saveDailyIncomeSta(authorIncomeAllDetail);
                }

            }

        } while (needAuthorNumber == realAuthorNumber);


    }

}

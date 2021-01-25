package com.java2nb.novel.core.schedule;


import com.java2nb.novel.core.config.AuthorIncomeProperties;
import com.java2nb.novel.core.utils.DateUtil;
import com.java2nb.novel.entity.*;
import com.java2nb.novel.service.AuthorService;
import com.java2nb.novel.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
public class MonthIncomeStaSchedule {

    private final AuthorService authorService;

    private final BookService bookService;

    private final AuthorIncomeProperties authorIncomeConfig;

    /**
     * 每个月1号凌晨2点统计上个月数据
     */
    @Scheduled(cron = "0 0 2 1 * ?")
    public void statistics() {

        //获取上个月的开始时间和结束时间
        Date startTime = DateUtil.getLastMonthStartTime();
        Date endTime = DateUtil.getLastMonthEndTime();

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

                Long totalPreTaxIncome = 0L;
                Long totalAfterTaxIncome = 0L;
                for (Book book : books) {

                    Long bookId = book.getId();


                    //3.月收入数据未统计入库,分作品统计数据入库
                    Long monthIncome = authorService.queryTotalAccount(userId, bookId, startTime, endTime);

                    BigDecimal monthIncomeShare = new BigDecimal(monthIncome)
                            .multiply(authorIncomeConfig.getShareProportion());
                    Long preTaxIncome = monthIncomeShare
                            .multiply(authorIncomeConfig.getExchangeProportion())
                            .multiply(new BigDecimal(100))
                            .longValue();

                    totalPreTaxIncome += preTaxIncome;

                    Long afterTaxIncome = monthIncomeShare
                            .multiply(authorIncomeConfig.getTaxRate())
                            .multiply(authorIncomeConfig.getExchangeProportion())
                            .multiply(new BigDecimal(100))
                            .longValue();

                    totalAfterTaxIncome += afterTaxIncome;

                    //4.查询月收入统计是否入库
                    if (monthIncome > 0 && !authorService.queryIsStatisticsMonth(bookId, endTime)) {
                        AuthorIncome authorIncome = new AuthorIncome();
                        authorIncome.setAuthorId(authorId);
                        authorIncome.setUserId(userId);
                        authorIncome.setBookId(bookId);
                        authorIncome.setPreTaxIncome(preTaxIncome);
                        authorIncome.setAfterTaxIncome(afterTaxIncome);
                        authorIncome.setIncomeMonth(endTime);
                        authorIncome.setCreateTime(new Date());

                        authorService.saveAuthorIncomeSta(authorIncome);
                    }


                }

                if (totalPreTaxIncome > 0 && !authorService.queryIsStatisticsMonth(0L, endTime)) {

                    AuthorIncome authorIncome = new AuthorIncome();
                    authorIncome.setAuthorId(authorId);
                    authorIncome.setUserId(userId);
                    authorIncome.setBookId(0L);
                    authorIncome.setPreTaxIncome(totalPreTaxIncome);
                    authorIncome.setAfterTaxIncome(totalAfterTaxIncome);
                    authorIncome.setIncomeMonth(endTime);
                    authorIncome.setCreateTime(new Date());

                    authorService.saveAuthorIncomeSta(authorIncome);
                }


            }

        } while (needAuthorNumber == realAuthorNumber);


    }

}

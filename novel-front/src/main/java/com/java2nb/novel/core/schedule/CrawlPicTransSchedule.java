package com.java2nb.novel.core.schedule;

import com.java2nb.novel.core.utils.Constants;
import com.java2nb.novel.entity.Book;
import com.java2nb.novel.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 将爬取的网络图片转存为自己的存储介质（本地、OSS、fastDfs）任务
 *
 * @author Administrator
 */
@ConditionalOnProperty(prefix = "pic.save", name = "type", havingValue = "2")
@Service
@RequiredArgsConstructor
@Slf4j
public class CrawlPicTransSchedule {

    private final BookService bookService;

    @Value("${pic.save.type}")
    private Integer picSaveType;

    @Value("${pic.save.path}")
    private String picSavePath;

    /**
     * 10分钟转一次
     */
    @Scheduled(fixedRate = 1000 * 60 * 10)
    @SneakyThrows
    public void trans() {

        log.info("Network2LocalPicSchedule。。。。。。。。。。。。");


        List<Book> networkPicBooks = bookService.queryNetworkPicBooks(Constants.LOCAL_PIC_PREFIX,100);
        for (Book book : networkPicBooks) {
            bookService.updateBookPicToLocal(book.getPicUrl(), book.getId());
            //3秒钟转化一张图片，10分钟转化200张
            Thread.sleep(3000);
        }


    }
}

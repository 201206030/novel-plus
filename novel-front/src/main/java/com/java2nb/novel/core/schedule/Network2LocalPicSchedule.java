package com.java2nb.novel.core.schedule;

import com.java2nb.novel.entity.Book;
import com.java2nb.novel.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 网络图片转存本地任务
 * @author Administrator
 */
@ConditionalOnProperty(prefix = "pic.save",name = "type",havingValue = "2")
@Service
@RequiredArgsConstructor
@Slf4j
public class Network2LocalPicSchedule {

    private final BookService bookService;

    @Value("${pic.save.type}")
    private Integer picSaveType;

    @Value("${pic.save.path}")
    private String picSavePath;

    /**
     * 10分钟转一次
     */
    @Scheduled(fixedRate = 1000 * 60 * 10)
    public void trans() {

        log.info("Network2LocalPicSchedule。。。。。。。。。。。。");


        Integer offset = 0, limit = 100;
        List<Book> networkPicBooks;
        do {
            networkPicBooks = bookService.queryNetworkPicBooks(limit, offset);
            for (Book book : networkPicBooks) {
                bookService.updateBookPicToLocal(book.getPicUrl(), book.getId());
            }
            offset += limit;
        } while (networkPicBooks.size() > 0);


    }
}

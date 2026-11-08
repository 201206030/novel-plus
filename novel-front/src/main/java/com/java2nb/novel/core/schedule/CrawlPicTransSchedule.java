package com.java2nb.novel.core.schedule;

import com.java2nb.novel.core.utils.Constants;
import com.java2nb.novel.entity.Book;
import com.java2nb.novel.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 将爬取的网络图片转存为自己的存储介质（本地、OSS）任务。
 * <p>
 * 当 {@code pic.save.type=2} 时启用。
 * 存储介质（本地/OSS）由 {@code pic.save.storage} 配置决定，
 * 具体转存逻辑委托给 {@link com.java2nb.novel.service.FileService} 实现。
 *
 * @author Administrator
 */
@ConditionalOnProperty(prefix = "pic.save", name = "type", havingValue = "2")
@Service
@RequiredArgsConstructor
@Slf4j
public class CrawlPicTransSchedule {

    private final BookService bookService;

    /**
     * 10分钟转一次
     */
    @Scheduled(fixedRate = 1000 * 60 * 10)
    @SneakyThrows
    public void trans() {

        log.info("CrawlPicTransSchedule。。。。。。。。。。。。");


        List<Book> networkPicBooks = bookService.queryNetworkPicBooks(Constants.LOCAL_PIC_PREFIX,100);
        for (Book book : networkPicBooks) {
            // transFile 内部根据 pic.save.storage 配置自动选择本地或 OSS 存储
            bookService.updateBookPicToLocal(book.getPicUrl(), book.getId());
            //3秒钟转化一张图片，10分钟转化200张
            Thread.sleep(3000);
        }


    }
}

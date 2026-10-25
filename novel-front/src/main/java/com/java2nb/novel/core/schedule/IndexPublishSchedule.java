package com.java2nb.novel.core.schedule;

import com.java2nb.novel.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 定时发布章节扫描任务：到达发布时刻的定时章节自动置为已发布
 *
 * @author 11797
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class IndexPublishSchedule {

    private final BookService bookService;

    /**
     * 每20秒扫描一次到时未发布的定时章节并逐条发布
     */
    @Scheduled(cron = "0/20 * * * * ?")
    public void publishDueChapters() {
        List<Long> dueIds = bookService.queryDueScheduledIndexIds(new Date(), 50);
        for (Long indexId : dueIds) {
            try {
                bookService.publishIndex(indexId, null);
            } catch (Exception e) {
                log.error("定时章节自动发布失败,indexId={}", indexId, e);
            }
        }
    }

}

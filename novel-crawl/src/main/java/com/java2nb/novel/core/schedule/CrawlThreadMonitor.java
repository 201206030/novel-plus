package com.java2nb.novel.core.schedule;


import com.java2nb.novel.core.cache.CacheKey;
import com.java2nb.novel.core.cache.CacheService;
import com.java2nb.novel.core.utils.ThreadUtil;
import com.java2nb.novel.entity.CrawlSource;
import com.java2nb.novel.service.CrawlService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 爬虫线程监控器,监控执行完成的爬虫源，并修改状态
 *
 * @author Administrator
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CrawlThreadMonitor {

    private final CacheService cacheService;

    private final CrawlService crawlService;

    @Scheduled(fixedRate = 1000 * 60 * 5)
    public void monitor() {

        //查询需要监控的正在运行的爬虫源
        List<CrawlSource> sources = crawlService.queryCrawlSourceByStatus((byte) 1);

        for (CrawlSource source : sources) {
            Set<Long> runningCrawlThreadIds = (Set<Long>) cacheService.getObject(CacheKey.RUNNING_CRAWL_THREAD_KEY_PREFIX + source.getId());
            boolean sourceStop = true;
            if (runningCrawlThreadIds != null) {
                for (Long threadId : runningCrawlThreadIds) {
                    Thread thread = ThreadUtil.findThread(threadId);

                    if (thread != null && thread.isAlive()) {
                        //有活跃线程，说明该爬虫源正在运行，数据库中状态正确，不需要修改
                        sourceStop = false;

                    }

                }
            }

            if (sourceStop) {
                crawlService.updateCrawlSourceStatus(source.getId(), (byte) 0);
            }


        }

    }
}

package com.java2nb.novel.core.schedule;

import com.java2nb.novel.core.cache.CacheKey;
import com.java2nb.novel.core.cache.CacheService;
import com.java2nb.novel.core.utils.BeanUtil;
import com.java2nb.novel.entity.Book;
import com.java2nb.novel.service.BookService;
import com.java2nb.novel.service.SearchService;
import com.java2nb.novel.vo.EsBookVO;
import io.searchbox.client.JestClient;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 小说导入搜索引擎
 *
 * @author Administrator
 */
@ConditionalOnProperty(prefix = "spring.elasticsearch", name = "enable", havingValue = "1")
@Service
@RequiredArgsConstructor
@Slf4j
public class BookToEsSchedule {

    private final BookService bookService;

    private final CacheService cacheService;



    private final SearchService searchService;


    /**
     * 1分钟导入一次
     */
    @Scheduled(fixedRate = 1000 * 60)
    public void saveToEs() {
        //TODO 引入Redisson框架实现分布式锁
        //可以重复更新，只是效率可能略有降低，所以暂不实现分布式锁
        if (cacheService.get(CacheKey.ES_TRANS_LOCK) == null) {
            cacheService.set(CacheKey.ES_TRANS_LOCK, "1", 60 * 20);
            try {
                //查询需要更新的小说
                Date lastDate = (Date) cacheService.getObject(CacheKey.ES_LAST_UPDATE_TIME);
                if (lastDate == null) {
                    lastDate = new SimpleDateFormat("yyyy-MM-dd").parse("2020-01-01");
                }


                List<Book> books = bookService.queryBookByUpdateTimeByPage(lastDate, 100);
                for (Book book : books) {
                    searchService.importToEs(book);
                    lastDate = book.getUpdateTime();
                    Thread.sleep(5000);

                }



                cacheService.setObject(CacheKey.ES_LAST_UPDATE_TIME, lastDate);

            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            cacheService.del(CacheKey.ES_TRANS_LOCK);


        }


    }


}

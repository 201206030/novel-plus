package com.java2nb.novel.core.schedule;

import com.java2nb.novel.core.cache.CacheKey;
import com.java2nb.novel.core.cache.CacheService;
import com.java2nb.novel.core.utils.BeanUtil;
import com.java2nb.novel.entity.Book;
import com.java2nb.novel.service.BookService;
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

    private final JestClient jestClient;


    private boolean lock = false;

    /**
     * 5秒导入一次
     */
    @Scheduled(fixedRate = 1000 * 5)
    public void saveToEs() {
        if (!lock) {
            lock = true;
            Date currentDate = new Date();
            try {
                //查询需要更新的小说
                Date lastDate = (Date) cacheService.getObject(CacheKey.ES_LAST_UPDATE_TIME);
                if (lastDate == null) {
                    lastDate = new SimpleDateFormat("yyyy-MM-dd").parse("2020-01-01");
                }

                long count ;
                int page = 1;
                do {

                    List<Book> books = bookService.queryBookByUpdateTimeByPage(lastDate, currentDate,page,100);
                    for(Book book : books) {
                        //导入到ES
                        EsBookVO esBookVO = new EsBookVO();
                        BeanUtils.copyProperties(book,esBookVO,"lastIndexUpdateTime");
                        esBookVO.setLastIndexUpdateTime(new SimpleDateFormat("yyyy/MM/dd HH:mm").format(book.getLastIndexUpdateTime()));
                        Index action = new Index.Builder(esBookVO).index("novel").type("book").id(book.getId().toString()).build();

                        jestClient.execute(action);

                    }

                    count = books.size();
                    page++;

                }while (count == 100);

                cacheService.setObject(CacheKey.ES_LAST_UPDATE_TIME, currentDate);

            } catch (Exception e) {
                log.error(e.getMessage(),e);
            }


            lock = false;
        }


    }


}

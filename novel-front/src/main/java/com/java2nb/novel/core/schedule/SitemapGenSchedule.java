package com.java2nb.novel.core.schedule;


import com.java2nb.novel.core.cache.CacheKey;
import com.java2nb.novel.core.cache.CacheService;
import com.java2nb.novel.core.utils.DateUtil;
import com.java2nb.novel.entity.Book;
import com.java2nb.novel.mapper.BookMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Date;
import java.util.List;

import static com.java2nb.novel.mapper.BookDynamicSqlSupport.*;
import static com.java2nb.novel.mapper.BookDynamicSqlSupport.book;
import static com.java2nb.novel.mapper.BookDynamicSqlSupport.lastIndexUpdateTime;
import static org.mybatis.dynamic.sql.SqlBuilder.isGreaterThan;
import static org.mybatis.dynamic.sql.SqlBuilder.isLessThan;
import static org.mybatis.dynamic.sql.select.SelectDSL.select;

/**
 * 生产环境自动生成网站地图
 *
 * @author cd
 */
@Profile("prod")
@Service
@RequiredArgsConstructor
@Slf4j
public class SitemapGenSchedule {

    @Value("${spring.web.resources.static-locations}")
    private String webRootDir;

    private static final String WEB_ROOT_DIR_PREFIX = "file:";

    private final BookMapper bookMapper;

    private final CacheService cacheService;

    /**
     * 每天自动更新 sitemap.xml 文件
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void statistics() {

        if (webRootDir.startsWith(WEB_ROOT_DIR_PREFIX)) {
            webRootDir = webRootDir.substring(WEB_ROOT_DIR_PREFIX.length());
            String webRootUrl = cacheService.get(CacheKey.WEB_SITE_ROOT_URL);
            if (StringUtils.isBlank(webRootUrl)) {
                return;
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(webRootDir + "sitemap.xml"))) {
                // 写入文件头
                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                writer.write("<urlset>\n");

                Date maxCreateTime = new Date();
                List<Book> books;
                do {
                    SelectStatementProvider selectStatement = select(id, createTime, lastIndexUpdateTime)
                        .from(book)
                        .where(createTime, isLessThan(maxCreateTime))
                        .and(wordCount, isGreaterThan(0))
                        .orderBy(createTime.descending())
                        .limit(100)
                        .build()
                        .render(RenderingStrategies.MYBATIS3);
                    books = bookMapper.selectMany(selectStatement);
                    for (Book book : books) {
                        maxCreateTime = book.getCreateTime();
                        if (book.getLastIndexUpdateTime() == null) {
                            continue;
                        }
                        writer.write("<url>\n");
                        writer.write("    <loc>" + webRootUrl + "/book/" + (book.getId()) + ".html" + "</loc>\n");
                        writer.write(
                            "    <lastmod>" + DateUtil.formatDate(book.getLastIndexUpdateTime(), DateUtil.DATE_PATTERN)
                                + "</lastmod>\n");
                        writer.write("    <changefreq>daily</changefreq>\n");
                        writer.write("    <priority>0.8</priority>\n");
                        writer.write("</url>\n");
                    }
                } while (!books.isEmpty());

                // 写入文件尾
                writer.write("</urlset>\n");
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }

    }

}

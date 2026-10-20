package com.java2nb.novel.service;

import com.github.pagehelper.PageHelper;
import com.java2nb.novel.core.cache.CacheKey;
import com.java2nb.novel.core.cache.CacheService;
import com.java2nb.novel.entity.News;
import com.java2nb.novel.entity.NewsCategory;
import com.java2nb.novel.mapper.NewsCategoryDynamicSqlSupport;
import com.java2nb.novel.mapper.NewsCategoryMapper;
import com.java2nb.novel.mapper.NewsDynamicSqlSupport;
import com.java2nb.novel.mapper.NewsMapper;
import io.github.xxyopen.model.page.PageBean;
import io.github.xxyopen.model.page.builder.pagehelper.PageBuilder;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static com.java2nb.novel.core.utils.AdminUtil.biz;
import static com.java2nb.novel.core.utils.AdminUtil.like;
import static com.java2nb.novel.core.utils.AdminUtil.parseIds;
import static org.mybatis.dynamic.sql.SqlBuilder.isIn;
import static org.mybatis.dynamic.sql.SqlBuilder.isLikeWhenPresent;

/**
 * 新闻管理Service：新闻列表/类别管理
 *
 * @author Administrator
 */
@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsMapper newsMapper;

    private final NewsCategoryMapper newsCategoryMapper;

    private final CacheService cacheService;

    public PageBean<News> newsList(String keyword, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        List<News> list = newsMapper.select(c -> {
            if (StringUtils.isNotBlank(keyword)) {
                return c.where(NewsDynamicSqlSupport.title, isLikeWhenPresent(like(keyword)))
                        .orderBy(NewsDynamicSqlSupport.createTime.descending());
            }
            return c.orderBy(NewsDynamicSqlSupport.createTime.descending());
        });
        return PageBuilder.build(list);
    }

    public News newsGet(Long id) {
        return newsMapper.selectByPrimaryKey(id).orElseThrow(() -> biz("新闻不存在"));
    }

    public void newsSave(News news) {
        fillCatName(news);
        Date now = new Date();
        news.setCreateTime(now);
        news.setUpdateTime(now);
        if (news.getReadCount() == null) {
            news.setReadCount(0L);
        }
        newsMapper.insertSelective(news);
        cacheService.del(CacheKey.INDEX_NEWS_KEY);
    }

    public void newsUpdate(News news) {
        fillCatName(news);
        news.setUpdateTime(new Date());
        newsMapper.updateByPrimaryKeySelective(news);
        cacheService.del(CacheKey.INDEX_NEWS_KEY);
    }

    public void newsRemove(Long id) {
        newsMapper.deleteByPrimaryKey(id);
        cacheService.del(CacheKey.INDEX_NEWS_KEY);
    }

    public void newsBatchRemove(String ids) {
        List<Long> idList = parseIds(ids);
        if (!idList.isEmpty()) {
            newsMapper.delete(c -> c.where(NewsDynamicSqlSupport.id, isIn(idList)));
            cacheService.del(CacheKey.INDEX_NEWS_KEY);
        }
    }

    private void fillCatName(News news) {
        if (news.getCatId() != null) {
            newsCategoryMapper.selectByPrimaryKey(news.getCatId())
                    .ifPresent(category -> news.setCatName(category.getName()));
        }
    }

    public PageBean<NewsCategory> categoryList(String keyword, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        List<NewsCategory> list = newsCategoryMapper.select(c -> {
            if (StringUtils.isNotBlank(keyword)) {
                return c.where(NewsCategoryDynamicSqlSupport.name, isLikeWhenPresent(like(keyword)))
                        .orderBy(NewsCategoryDynamicSqlSupport.sort);
            }
            return c.orderBy(NewsCategoryDynamicSqlSupport.sort);
        });
        return PageBuilder.build(list);
    }

    public NewsCategory categoryGet(Integer id) {
        return newsCategoryMapper.selectByPrimaryKey(id).orElseThrow(() -> biz("类别不存在"));
    }

    public void categorySave(NewsCategory category) {
        Date now = new Date();
        category.setCreateTime(now);
        category.setUpdateTime(now);
        newsCategoryMapper.insertSelective(category);
    }

    public void categoryUpdate(NewsCategory category) {
        category.setUpdateTime(new Date());
        newsCategoryMapper.updateByPrimaryKeySelective(category);
    }

    public void categoryRemove(Integer id) {
        newsCategoryMapper.deleteByPrimaryKey(id);
    }

    public void categoryBatchRemove(String ids) {
        List<Integer> idList = parseIds(ids).stream().map(Long::intValue).toList();
        if (!idList.isEmpty()) {
            newsCategoryMapper.delete(c -> c.where(NewsCategoryDynamicSqlSupport.id, isIn(idList)));
        }
    }

}

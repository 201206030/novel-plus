package com.java2nb.novel.service.impl;

import io.github.xxyopen.web.util.BeanUtil;
import com.java2nb.novel.service.FriendLinkService;
import com.java2nb.novel.core.cache.CacheKey;
import com.java2nb.novel.core.cache.CacheService;
import com.java2nb.novel.entity.FriendLink;
import com.java2nb.novel.mapper.FriendLinkMapper;
import lombok.RequiredArgsConstructor;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.java2nb.novel.mapper.FriendLinkDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;
import static org.mybatis.dynamic.sql.select.SelectDSL.select;

/**
 * @author 11797
 */
@Service
@RequiredArgsConstructor
public class FriendLinkServiceImpl implements FriendLinkService {

    private final FriendLinkMapper friendLinkMapper;

    private final CacheService cacheService;


    @Override
    public List<FriendLink> listIndexLink() {
        List<FriendLink> result = (List<FriendLink>) cacheService.getObject(CacheKey.INDEX_LINK_KEY);
        if(result == null || result.size() == 0) {
            SelectStatementProvider selectStatement = select(linkName,linkUrl)
                    .from(friendLink)
                    .where(isOpen,isEqualTo((byte)1))
                    .orderBy(sort)
                    .build()
                    .render(RenderingStrategies.MYBATIS3);
            result =  friendLinkMapper.selectMany(selectStatement);
            cacheService.setObject(CacheKey.INDEX_LINK_KEY,result,60 * 60 * 24);
        }
        return result;
    }
}

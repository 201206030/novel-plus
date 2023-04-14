package com.java2nb.novel.dao;

import com.java2nb.novel.domain.BookContentDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 小说内容表
 *
 * @author xiongxy
 * @email 1179705413@qq.com
 * @date 2023-04-14 19:52:06
 */
@Mapper
public interface BookContentDao {

    BookContentDO get(Long id);

    List<BookContentDO> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(BookContentDO bookContent);

    int update(BookContentDO bookContent);

    int remove(Long id);

    int batchRemove(Long[] ids);

    int removeByIndexIds(Long[] indexIds);
}

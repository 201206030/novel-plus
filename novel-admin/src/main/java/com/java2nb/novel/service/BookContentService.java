package com.java2nb.novel.service;

import com.java2nb.novel.domain.BookContentDO;

import java.util.List;
import java.util.Map;

/**
 * 小说内容表
 * 
 * @author xiongxy
 * @email 1179705413@qq.com
 * @date 2023-04-14 19:52:06
 */
public interface BookContentService {
	
	BookContentDO get(Long id);
	
	List<BookContentDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(BookContentDO bookContent);
	
	int update(BookContentDO bookContent);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}

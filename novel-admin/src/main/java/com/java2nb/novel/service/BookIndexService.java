package com.java2nb.novel.service;

import com.java2nb.novel.domain.BookIndexDO;

import java.util.List;
import java.util.Map;

/**
 * 小说目录表
 * 
 * @author xiongxy
 * @email 1179705413@qq.com
 * @date 2023-04-14 19:51:54
 */
public interface BookIndexService {
	
	BookIndexDO get(Long id);
	
	List<BookIndexDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(BookIndexDO bookIndex);
	
	int update(BookIndexDO bookIndex);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}

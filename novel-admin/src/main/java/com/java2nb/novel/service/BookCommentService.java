package com.java2nb.novel.service;

import com.java2nb.novel.domain.BookCommentDO;

import java.util.List;
import java.util.Map;

/**
 * 小说评论表
 * 
 * @author xiongxy
 * @email 1179705413@qq.com
 * @date 2023-04-14 21:59:28
 */
public interface BookCommentService {
	
	BookCommentDO get(Long id);
	
	List<BookCommentDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(BookCommentDO bookComment);
	
	int update(BookCommentDO bookComment);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}

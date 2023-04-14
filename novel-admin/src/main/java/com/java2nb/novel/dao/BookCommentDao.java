package com.java2nb.novel.dao;

import com.java2nb.novel.domain.BookCommentDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 小说评论表
 * @author xiongxy
 * @email 1179705413@qq.com
 * @date 2023-04-14 21:59:28
 */
@Mapper
public interface BookCommentDao {

	BookCommentDO get(Long id);
	
	List<BookCommentDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(BookCommentDO bookComment);
	
	int update(BookCommentDO bookComment);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}

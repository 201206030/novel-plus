package com.java2nb.novel.dao;

import com.java2nb.novel.domain.BookDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 小说表
 * @author phacks
 * @email 1179705413@qq.com
 * @date 2020-05-16 15:08:34
 */
@Mapper
public interface BookDao {

	BookDO get(Long id);
	
	List<BookDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(BookDO book);
	
	int update(BookDO book);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}

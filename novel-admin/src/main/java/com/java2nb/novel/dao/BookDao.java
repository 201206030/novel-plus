package com.java2nb.novel.dao;

import com.java2nb.novel.domain.BookDO;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 小说表
 * @author xiongxy
 * @email 1179705413@qq.com
 * @date 2020-12-01 03:49:46
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

    List<Map<Object, Object>> tableSta(Date minDate);
}

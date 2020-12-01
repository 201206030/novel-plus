package com.java2nb.novel.dao;

import com.java2nb.novel.domain.NewsDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 新闻表
 * @author xiongxy
 * @email 1179705413@qq.com
 * @date 2020-12-01 10:05:51
 */
@Mapper
public interface NewsDao {

	NewsDO get(Long id);
	
	List<NewsDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(NewsDO news);
	
	int update(NewsDO news);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}

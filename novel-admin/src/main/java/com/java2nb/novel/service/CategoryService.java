package com.java2nb.novel.service;

import com.java2nb.novel.domain.CategoryDO;

import java.util.List;
import java.util.Map;

/**
 * 新闻类别表
 * 
 * @author xiongxy
 * @email 1179705413@qq.com
 * @date 2020-12-01 10:03:41
 */
public interface CategoryService {
	
	CategoryDO get(Integer id);
	
	List<CategoryDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(CategoryDO category);
	
	int update(CategoryDO category);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);
}

package com.java2nb.novel.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.java2nb.novel.dao.CategoryDao;
import com.java2nb.novel.domain.CategoryDO;
import com.java2nb.novel.service.CategoryService;



@Service
public class CategoryServiceImpl implements CategoryService {
	@Autowired
	private CategoryDao categoryDao;
	
	@Override
	public CategoryDO get(Integer id){
		return categoryDao.get(id);
	}
	
	@Override
	public List<CategoryDO> list(Map<String, Object> map){
		return categoryDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return categoryDao.count(map);
	}
	
	@Override
	public int save(CategoryDO category){
		category.setCreateTime(new Date());
		return categoryDao.save(category);
	}
	
	@Override
	public int update(CategoryDO category){
		category.setUpdateTime(new Date());
		return categoryDao.update(category);
	}
	
	@Override
	public int remove(Integer id){
		return categoryDao.remove(id);
	}
	
	@Override
	public int batchRemove(Integer[] ids){
		return categoryDao.batchRemove(ids);
	}
	
}

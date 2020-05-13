package com.java2nb.novel.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.java2nb.novel.dao.AuthorDao;
import com.java2nb.novel.domain.AuthorDO;
import com.java2nb.novel.service.AuthorService;



@Service
public class AuthorServiceImpl implements AuthorService {
	@Autowired
	private AuthorDao authorDao;
	
	@Override
	public AuthorDO get(Long id){
		return authorDao.get(id);
	}
	
	@Override
	public List<AuthorDO> list(Map<String, Object> map){
		return authorDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return authorDao.count(map);
	}
	
	@Override
	public int save(AuthorDO author){
		return authorDao.save(author);
	}
	
	@Override
	public int update(AuthorDO author){
		return authorDao.update(author);
	}
	
	@Override
	public int remove(Long id){
		return authorDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return authorDao.batchRemove(ids);
	}
	
}

package com.java2nb.novel.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.java2nb.novel.dao.BookCommentDao;
import com.java2nb.novel.domain.BookCommentDO;
import com.java2nb.novel.service.BookCommentService;



@Service
public class BookCommentServiceImpl implements BookCommentService {
	@Autowired
	private BookCommentDao bookCommentDao;
	
	@Override
	public BookCommentDO get(Long id){
		return bookCommentDao.get(id);
	}
	
	@Override
	public List<BookCommentDO> list(Map<String, Object> map){
		return bookCommentDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return bookCommentDao.count(map);
	}
	
	@Override
	public int save(BookCommentDO bookComment){
		return bookCommentDao.save(bookComment);
	}
	
	@Override
	public int update(BookCommentDO bookComment){
		return bookCommentDao.update(bookComment);
	}
	
	@Override
	public int remove(Long id){
		return bookCommentDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return bookCommentDao.batchRemove(ids);
	}
	
}

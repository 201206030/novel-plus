package com.java2nb.novel.service.impl;

import com.java2nb.common.utils.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.java2nb.novel.dao.AuthorCodeDao;
import com.java2nb.novel.domain.AuthorCodeDO;
import com.java2nb.novel.service.AuthorCodeService;



@Service
public class AuthorCodeServiceImpl implements AuthorCodeService {
	@Autowired
	private AuthorCodeDao authorCodeDao;
	
	@Override
	public AuthorCodeDO get(Long id){
		return authorCodeDao.get(id);
	}
	
	@Override
	public List<AuthorCodeDO> list(Map<String, Object> map){
		return authorCodeDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return authorCodeDao.count(map);
	}
	
	@Override
	public int save(AuthorCodeDO authorCode){
		authorCode.setIsUse(0);
		authorCode.setCreateTime(new Date());
		authorCode.setCreateUserId(ShiroUtils.getUserId());
		return authorCodeDao.save(authorCode);
	}
	
	@Override
	public int update(AuthorCodeDO authorCode){
		return authorCodeDao.update(authorCode);
	}
	
	@Override
	public int remove(Long id){
		return authorCodeDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return authorCodeDao.batchRemove(ids);
	}
	
}

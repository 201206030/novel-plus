package com.java2nb.novel.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.java2nb.novel.dao.FriendLinkDao;
import com.java2nb.novel.domain.FriendLinkDO;
import com.java2nb.novel.service.FriendLinkService;



@Service
public class FriendLinkServiceImpl implements FriendLinkService {
	@Autowired
	private FriendLinkDao friendLinkDao;
	
	@Override
	public FriendLinkDO get(Integer id){
		return friendLinkDao.get(id);
	}
	
	@Override
	public List<FriendLinkDO> list(Map<String, Object> map){
		return friendLinkDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return friendLinkDao.count(map);
	}
	
	@Override
	public int save(FriendLinkDO friendLink){
		return friendLinkDao.save(friendLink);
	}
	
	@Override
	public int update(FriendLinkDO friendLink){
		return friendLinkDao.update(friendLink);
	}
	
	@Override
	public int remove(Integer id){
		return friendLinkDao.remove(id);
	}
	
	@Override
	public int batchRemove(Integer[] ids){
		return friendLinkDao.batchRemove(ids);
	}
	
}

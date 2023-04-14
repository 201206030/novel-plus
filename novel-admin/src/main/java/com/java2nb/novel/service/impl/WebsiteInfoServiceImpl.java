package com.java2nb.novel.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.java2nb.novel.dao.WebsiteInfoDao;
import com.java2nb.novel.domain.WebsiteInfoDO;
import com.java2nb.novel.service.WebsiteInfoService;



@Service
public class WebsiteInfoServiceImpl implements WebsiteInfoService {
	@Autowired
	private WebsiteInfoDao websiteInfoDao;
	
	@Override
	public WebsiteInfoDO get(Long id){
		return websiteInfoDao.get(id);
	}
	
	@Override
	public List<WebsiteInfoDO> list(Map<String, Object> map){
		return websiteInfoDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return websiteInfoDao.count(map);
	}
	
	@Override
	public int save(WebsiteInfoDO websiteInfo){
		return websiteInfoDao.save(websiteInfo);
	}
	
	@Override
	public int update(WebsiteInfoDO websiteInfo){
		return websiteInfoDao.update(websiteInfo);
	}
	
	@Override
	public int remove(Long id){
		return websiteInfoDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return websiteInfoDao.batchRemove(ids);
	}
	
}

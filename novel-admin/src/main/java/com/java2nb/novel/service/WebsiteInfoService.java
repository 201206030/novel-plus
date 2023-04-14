package com.java2nb.novel.service;

import com.java2nb.novel.domain.WebsiteInfoDO;

import java.util.List;
import java.util.Map;

/**
 * 网站信息表
 * 
 * @author xiongxy
 * @email 1179705413@qq.com
 * @date 2023-04-14 11:05:43
 */
public interface WebsiteInfoService {
	
	WebsiteInfoDO get(Long id);
	
	List<WebsiteInfoDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(WebsiteInfoDO websiteInfo);
	
	int update(WebsiteInfoDO websiteInfo);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}

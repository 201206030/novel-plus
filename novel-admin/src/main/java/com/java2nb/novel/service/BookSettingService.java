package com.java2nb.novel.service;

import com.java2nb.novel.domain.BookSettingDO;

import java.util.List;
import java.util.Map;

/**
 * 首页小说设置表
 * 
 * @author xiongxy
 * @email 1179705413@qq.com
 * @date 2023-04-18 10:01:13
 */
public interface BookSettingService {
	
	BookSettingDO get(Long id);
	
	List<BookSettingDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(BookSettingDO bookSetting);
	
	int update(BookSettingDO bookSetting);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}

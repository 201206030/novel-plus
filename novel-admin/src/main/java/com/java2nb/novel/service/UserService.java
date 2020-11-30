package com.java2nb.novel.service;

import com.java2nb.novel.domain.UserDO;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author xiongxy
 * @email 1179705413@qq.com
 * @date 2020-12-01 03:49:08
 */
public interface UserService {
	
	UserDO get(Long id);
	
	List<UserDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(UserDO user);
	
	int update(UserDO user);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

    Map<Object, Object> tableSta(Date minDate);
}

package com.java2nb.novel.service;

import com.java2nb.novel.domain.AuthorCodeDO;

import java.util.List;
import java.util.Map;

/**
 * 作家邀请码表
 * 
 * @author xiongxy
 * @email 1179705413@qq.com
 * @date 2020-05-13 11:29:15
 */
public interface AuthorCodeService {
	
	AuthorCodeDO get(Long id);
	
	List<AuthorCodeDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(AuthorCodeDO authorCode);
	
	int update(AuthorCodeDO authorCode);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}

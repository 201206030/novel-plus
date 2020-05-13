package com.java2nb.novel.dao;

import com.java2nb.novel.domain.AuthorDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 作者表
 * @author xiongxy
 * @email 1179705413@qq.com
 * @date 2020-05-13 11:16:51
 */
@Mapper
public interface AuthorDao {

	AuthorDO get(Long id);
	
	List<AuthorDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(AuthorDO author);
	
	int update(AuthorDO author);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}

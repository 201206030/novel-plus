package com.java2nb.system.dao;

import com.java2nb.system.domain.UserDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 
 * @author xiongxy
 * @email 1179705413@qq.com
 * @date 2019-10-03 09:45:11
 */
@Mapper
public interface UserDao {

	UserDO get(Long userId);
	
	List<UserDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(UserDO user);
	
	int update(UserDO user);
	
	int remove(Long userId);
	
	int batchRemove(Long[] userIds);
	
	Long[] listAllDept();

    List<UserDO> listByPerm(Map<String, Object> map);

	int countByPerm(Map<String,Object> map);
}

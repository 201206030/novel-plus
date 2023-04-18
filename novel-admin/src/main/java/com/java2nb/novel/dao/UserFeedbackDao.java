package com.java2nb.novel.dao;

import com.java2nb.novel.domain.UserFeedbackDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * @author xiongxy
 * @email 1179705413@qq.com
 * @date 2023-04-18 11:08:54
 */
@Mapper
public interface UserFeedbackDao {

	UserFeedbackDO get(Long id);
	
	List<UserFeedbackDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(UserFeedbackDO userFeedback);
	
	int update(UserFeedbackDO userFeedback);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}

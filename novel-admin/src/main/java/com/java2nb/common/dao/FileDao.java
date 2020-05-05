package com.java2nb.common.dao;

import com.java2nb.common.domain.FileDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 文件上传
 * @author xiongxy
 * @email 1179705413@qq.com
 * @date 2019-10-03 15:45:42
 */
@Mapper
public interface FileDao {

	FileDO get(Long id);
	
	List<FileDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(FileDO file);
	
	int update(FileDO file);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}

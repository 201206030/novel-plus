package com.java2nb.common.dao;

import java.util.List;
import java.util.Map;

import com.java2nb.common.domain.GenColumnsDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 
 * @author xiongxy
 * @email 1179705413@qq.com
 * @date 2019-11-22 10:39:29
 */
@Mapper
public interface GenColumnsDao {


	GenColumnsDO get(Long id);

	List<GenColumnsDO> list(Map<String,Object> map);

	int count(Map<String,Object> map);

	int save(GenColumnsDO genColumns);

	int update(GenColumnsDO genColumns);

	int remove(Long id);

	int batchRemove(Long[] ids);


	void saveBatch(List<GenColumnsDO> list);

	void deleteByTableName(@Param("tableName") String tableName);
}

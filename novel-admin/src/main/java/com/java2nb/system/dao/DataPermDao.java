package com.java2nb.system.dao;

import com.java2nb.system.domain.DataPermDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 数据权限管理
 * @author xiongxy
 * @email 1179705413@qq.com
 * @date 2019-11-25 11:40:03
 */
@Mapper
public interface DataPermDao {

	DataPermDO get(Long id);
	
	List<DataPermDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(DataPermDO dataPerm);
	
	int update(DataPermDO dataPerm);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

    List<DataPermDO> listModuleName();

    List<DataPermDO> selectDataPermsByUserId(@Param("userId") Long userId);
}

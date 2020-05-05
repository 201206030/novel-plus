package com.java2nb.system.dao;

import com.java2nb.system.domain.DeptDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 部门管理
 * @author xiongxy
 * @email 1179705413@qq.com
 * @date 2019-10-03 15:35:39
 */
@Mapper
public interface DeptDao {

	DeptDO get(Long deptId);
	
	List<DeptDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(DeptDO dept);
	
	int update(DeptDO dept);
	
	int remove(Long deptId);
	
	int batchRemove(Long[] deptIds);
	
	Long[] listParentDept();
	
	int getDeptUserNumber(Long deptId);

    String getDeptIdsByParentId(@Param("deptId") Long deptId);
}

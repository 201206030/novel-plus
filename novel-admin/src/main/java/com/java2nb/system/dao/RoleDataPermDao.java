package com.java2nb.system.dao;

import com.java2nb.system.domain.RoleDataPermDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 角色与数据权限对应关系
 * @author xiongxy
 * @email 1179705413@qq.com
 * @date 2019-11-25 11:32:49
 */
@Mapper
public interface RoleDataPermDao {

	RoleDataPermDO get(Long id);
	
	List<RoleDataPermDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(RoleDataPermDO roleDataPerm);
	
	int update(RoleDataPermDO roleDataPerm);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

    void removeByRoleId(Long roleId);

	void batchSave(List<RoleDataPermDO> rps);

	List<Long> listPermIdByRoleId(Long roleId);
}

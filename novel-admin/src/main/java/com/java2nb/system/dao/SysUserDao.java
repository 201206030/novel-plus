package com.java2nb.system.dao;

import com.java2nb.common.annotation.SanitizeMap;

import com.java2nb.system.domain.UserDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @author xiongxy
 * @email 1179705413@qq.com
 * @date 2019-10-03 09:45:11
 */
@Mapper
public interface SysUserDao {

    UserDO get(Long userId);

    List<UserDO> list(@SanitizeMap Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(UserDO user);

    int update(UserDO user);

    int remove(Long userId);

    int batchRemove(Long[] userIds);

    Long[] listAllDept();

    List<UserDO> listByPerm(@SanitizeMap Map<String, Object> map);

    int countByPerm(Map<String, Object> map);
}

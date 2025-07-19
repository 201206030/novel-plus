package com.java2nb.novel.dao;

import com.java2nb.common.annotation.SanitizeMap;

import com.java2nb.novel.domain.UserDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author xiongxy
 * @email 1179705413@qq.com
 * @date 2020-12-01 03:49:08
 */
@Mapper
public interface UserDao {

    UserDO get(Long id);

    List<UserDO> list(@SanitizeMap Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(UserDO user);

    int update(UserDO user);

    int remove(Long id);

    int batchRemove(Long[] ids);

    List<Map<Object, Object>> tableSta(Date minDate);

    List<UserDO> batchGet(List<Long> userIds);
}

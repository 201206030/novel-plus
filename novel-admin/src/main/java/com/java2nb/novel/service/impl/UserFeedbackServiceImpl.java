package com.java2nb.novel.service.impl;

import com.java2nb.novel.dao.UserDao;
import com.java2nb.novel.dao.UserFeedbackDao;
import com.java2nb.novel.domain.UserDO;
import com.java2nb.novel.domain.UserFeedbackDO;
import com.java2nb.novel.service.UserFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class UserFeedbackServiceImpl implements UserFeedbackService {

    @Autowired
    private UserFeedbackDao userFeedbackDao;
    @Autowired
    private UserDao userDao;

    @Override
    public UserFeedbackDO get(Long id) {
        return userFeedbackDao.get(id);
    }

    @Override
    public List<UserFeedbackDO> list(Map<String, Object> map) {
        List<UserFeedbackDO> list = userFeedbackDao.list(map);
        if (!CollectionUtils.isEmpty(list)) {
            List<Long> userIds = list.stream().map(UserFeedbackDO::getUserId).collect(Collectors.toList());
            Map<Long, String> userNameMap = userDao.batchGet(userIds).stream()
                .collect(Collectors.toMap(UserDO::getId, UserDO::getUsername));
            list.forEach(v -> v.setUserName(userNameMap.get(v.getUserId())));
        }
        return list;
    }

    @Override
    public int count(Map<String, Object> map) {
        return userFeedbackDao.count(map);
    }

    @Override
    public int save(UserFeedbackDO userFeedback) {
        return userFeedbackDao.save(userFeedback);
    }

    @Override
    public int update(UserFeedbackDO userFeedback) {
        return userFeedbackDao.update(userFeedback);
    }

    @Override
    public int remove(Long id) {
        return userFeedbackDao.remove(id);
    }

    @Override
    public int batchRemove(Long[] ids) {
        return userFeedbackDao.batchRemove(ids);
    }

}

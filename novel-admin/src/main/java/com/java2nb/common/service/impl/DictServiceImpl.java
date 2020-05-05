package com.java2nb.common.service.impl;

import com.java2nb.common.utils.StringUtils;
import com.java2nb.system.domain.UserDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.java2nb.common.dao.DictDao;
import com.java2nb.common.domain.DictDO;
import com.java2nb.common.service.DictService;


@Service
public class DictServiceImpl implements DictService {
    @Autowired
    private DictDao dictDao;

    @Override
    public DictDO get(Long id) {
        return dictDao.get(id);
    }

    @Override
    public List<DictDO> list(Map<String, Object> map) {
        return dictDao.list(map);
    }

    @Override
    public int count(Map<String, Object> map) {
        return dictDao.count(map);
    }

    @Override
    public int save(DictDO dict) {
        return dictDao.save(dict);
    }

    @Override
    public int update(DictDO dict) {
        return dictDao.update(dict);
    }

    @Override
    public int remove(Long id) {
        return dictDao.remove(id);
    }

    @Override
    public int batchRemove(Long[] ids) {
        return dictDao.batchRemove(ids);
    }

    @Override

    public List<DictDO> listType() {
        return dictDao.listType();
    }

    @Override
    public String getName(String type, String value) {
        Map<String, Object> param = new HashMap<String, Object>(16);
        param.put("type", type);
        param.put("value", value);
        String rString = dictDao.list(param).get(0).getName();
        return rString;
    }

    @Override
    public List<DictDO> getHobbyList(UserDO userDO) {
        Map<String, Object> param = new HashMap<>(16);
        param.put("type", "hobby");
        List<DictDO> hobbyList = dictDao.list(param);

        if (StringUtils.isNotEmpty(userDO.getHobby())) {
            String userHobbys[] = userDO.getHobby().split(";");
            for (String userHobby : userHobbys) {
                for (DictDO hobby : hobbyList) {
                    if (!Objects.equals(userHobby, hobby.getId().toString())) {
                        continue;
                    }
                    hobby.setRemarks("true");
                    break;
                }
            }
        }

        return hobbyList;
    }

    @Override
    public List<DictDO> getSexList() {
        Map<String, Object> param = new HashMap<>(16);
        param.put("type", "sex");
        return dictDao.list(param);
    }

    @Override
    public List<DictDO> listByType(String type) {
        Map<String, Object> param = new HashMap<>(16);
        param.put("type", type);
        return dictDao.list(param);
    }

}

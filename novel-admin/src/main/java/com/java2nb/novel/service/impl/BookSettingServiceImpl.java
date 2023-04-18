package com.java2nb.novel.service.impl;

import com.java2nb.novel.dao.BookDao;
import com.java2nb.novel.dao.BookSettingDao;
import com.java2nb.novel.domain.BookDO;
import com.java2nb.novel.domain.BookSettingDO;
import com.java2nb.novel.service.BookSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class BookSettingServiceImpl implements BookSettingService {

    @Autowired
    private BookSettingDao bookSettingDao;
    @Autowired
    private BookDao bookDao;

    @Override
    public BookSettingDO get(Long id) {
        return bookSettingDao.get(id);
    }

    @Override
    public List<BookSettingDO> list(Map<String, Object> map) {
        List<BookSettingDO> list = bookSettingDao.list(map);
        if (!CollectionUtils.isEmpty(list)) {
            List<Long> bookIds = list.stream().map(BookSettingDO::getBookId).collect(Collectors.toList());
            Map<Long, String> bookNameMap = bookDao.batchGet(bookIds).stream()
                .collect(Collectors.toMap(BookDO::getId, BookDO::getBookName));
            list = list.stream().filter(v -> bookNameMap.containsKey(v.getBookId())).collect(Collectors.toList());
            list.forEach(v -> v.setBookName(bookNameMap.get(v.getBookId())));
        }
        return list;
    }

    @Override
    public int count(Map<String, Object> map) {
        return bookSettingDao.count(map);
    }

    @Override
    public int save(BookSettingDO bookSetting) {
        return bookSettingDao.save(bookSetting);
    }

    @Override
    public int update(BookSettingDO bookSetting) {
        return bookSettingDao.update(bookSetting);
    }

    @Override
    public int remove(Long id) {
        return bookSettingDao.remove(id);
    }

    @Override
    public int batchRemove(Long[] ids) {
        return bookSettingDao.batchRemove(ids);
    }

}

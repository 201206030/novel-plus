package com.java2nb.novel.service.impl;

import com.java2nb.novel.dao.BookContentDao;
import com.java2nb.novel.dao.BookDao;
import com.java2nb.novel.dao.BookIndexDao;
import com.java2nb.novel.domain.BookDO;
import com.java2nb.novel.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookDao bookDao;
    @Autowired
    private BookIndexDao bookIndexDao;
    @Autowired
    private BookContentDao bookContentDao;

    @Override
    public BookDO get(Long id) {
        return bookDao.get(id);
    }

    @Override
    public List<BookDO> list(Map<String, Object> map) {
        return bookDao.list(map);
    }

    @Override
    public int count(Map<String, Object> map) {
        return bookDao.count(map);
    }

    @Override
    public int save(BookDO book) {
        return bookDao.save(book);
    }

    @Override
    public int update(BookDO book) {
        return bookDao.update(book);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int remove(Long id) {
        List<Long> indexIds = bookIndexDao.getIdsByBookId(id);
        if (!CollectionUtils.isEmpty(indexIds)) {
            Long[] indexIdArray = indexIds.toArray(new Long[0]);
            bookIndexDao.batchRemove(indexIdArray);
            bookContentDao.removeByIndexIds(indexIdArray);
        }
        return bookDao.remove(id);
    }

    @Override
    public int batchRemove(Long[] ids) {
        return bookDao.batchRemove(ids);
    }

    @Override
    public Map<Object, Object> tableSta(Date minDate) {
        List<Map<Object, Object>> maps = bookDao.tableSta(minDate);

        return maps.stream().collect(Collectors.toMap(x -> x.get("staDate"), x -> x.get("bookCount")));

    }

}

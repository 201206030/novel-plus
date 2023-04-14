package com.java2nb.novel.service.impl;

import com.java2nb.novel.dao.BookCommentDao;
import com.java2nb.novel.dao.BookDao;
import com.java2nb.novel.dao.UserDao;
import com.java2nb.novel.domain.BookCommentDO;
import com.java2nb.novel.domain.BookDO;
import com.java2nb.novel.domain.UserDO;
import com.java2nb.novel.service.BookCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class BookCommentServiceImpl implements BookCommentService {

    @Autowired
    private BookCommentDao bookCommentDao;
    @Autowired
    private BookDao bookDao;
    @Autowired
    private UserDao userDao;

    @Override
    public BookCommentDO get(Long id) {
        return bookCommentDao.get(id);
    }

    @Override
    public List<BookCommentDO> list(Map<String, Object> map) {
        List<BookCommentDO> list = bookCommentDao.list(map);
        if (!CollectionUtils.isEmpty(list)) {
            List<Long> bookIds = list.stream().map(BookCommentDO::getBookId).collect(Collectors.toList());
            Map<Long, String> bookNameMap = bookDao.batchGet(bookIds).stream()
                .collect(Collectors.toMap(BookDO::getId, BookDO::getBookName));
            List<Long> userIds = list.stream().map(BookCommentDO::getCreateUserId).collect(Collectors.toList());
            Map<Long, String> userNameMap = userDao.batchGet(userIds).stream()
                .collect(Collectors.toMap(UserDO::getId, UserDO::getUsername));
            list.forEach(v -> {
                v.setBookName(bookNameMap.get(v.getBookId()));
                v.setUserName(userNameMap.get(v.getCreateUserId()));
            });
        }
        return list;
    }

    @Override
    public int count(Map<String, Object> map) {
        return bookCommentDao.count(map);
    }

    @Override
    public int save(BookCommentDO bookComment) {
        return bookCommentDao.save(bookComment);
    }

    @Override
    public int update(BookCommentDO bookComment) {
        return bookCommentDao.update(bookComment);
    }

    @Override
    public int remove(Long id) {
        return bookCommentDao.remove(id);
    }

    @Override
    public int batchRemove(Long[] ids) {
        return bookCommentDao.batchRemove(ids);
    }

}

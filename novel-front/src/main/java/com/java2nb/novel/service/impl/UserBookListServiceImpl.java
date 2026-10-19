package com.java2nb.novel.service.impl;

import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import com.github.pagehelper.PageHelper;
import com.java2nb.novel.core.enums.ResponseStatus;
import com.java2nb.novel.entity.UserBookList;
import com.java2nb.novel.entity.UserBookListItem;
import com.java2nb.novel.mapper.BookMapper;
import com.java2nb.novel.mapper.FrontUserBookListMapper;
import com.java2nb.novel.service.UserBookListService;
import com.java2nb.novel.vo.BookListVO;
import io.github.xxyopen.model.page.PageBean;
import io.github.xxyopen.model.page.builder.pagehelper.PageBuilder;
import io.github.xxyopen.web.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 用户书单服务实现
 *
 * @author 11797
 */
@Service
@RequiredArgsConstructor
public class UserBookListServiceImpl implements UserBookListService {

    private final FrontUserBookListMapper userBookListMapper;

    private final BookMapper bookMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long createBookList(Long userId, String listName, String description) {
        listName = StringUtils.trim(listName);
        if (StringUtils.isBlank(listName) || listName.length() > 50) {
            throw new BusinessException(ResponseStatus.LIST_NAME_INVALID);
        }
        if (userBookListMapper.countByName(userId, listName, null) > 0) {
            throw new BusinessException(ResponseStatus.LIST_NAME_EXISTS);
        }
        Date now = new Date();
        UserBookList bookList = new UserBookList();
        bookList.setUserId(userId);
        bookList.setListName(SensitiveWordHelper.replace(listName));
        bookList.setDescription(replaceSensitive(description));
        bookList.setBookCount(0);
        bookList.setStatus((byte) 1);
        bookList.setCreateTime(now);
        bookList.setUpdateTime(now);
        userBookListMapper.insertBookList(bookList);
        return bookList.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateBookList(Long userId, Long listId, String listName, String description) {
        checkOwner(userId, listId);
        listName = StringUtils.trim(listName);
        if (StringUtils.isBlank(listName) || listName.length() > 50) {
            throw new BusinessException(ResponseStatus.LIST_NAME_INVALID);
        }
        if (userBookListMapper.countByName(userId, listName, listId) > 0) {
            throw new BusinessException(ResponseStatus.LIST_NAME_EXISTS);
        }
        UserBookList bookList = new UserBookList();
        bookList.setId(listId);
        bookList.setListName(SensitiveWordHelper.replace(listName));
        bookList.setDescription(replaceSensitive(description));
        bookList.setUpdateTime(new Date());
        userBookListMapper.updateBookList(bookList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteBookList(Long userId, Long listId) {
        checkOwner(userId, listId);
        userBookListMapper.softDeleteById(listId);
    }

    @Override
    public PageBean<BookListVO> listMinePage(Long userId, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return PageBuilder.build(userBookListMapper.listMine(userId));
    }

    @Override
    public PageBean<BookListVO> listPlazaPage(int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return PageBuilder.build(userBookListMapper.listPlaza());
    }

    @Override
    public BookListVO detail(Long listId, Long userId) {
        BookListVO bookList = userBookListMapper.getById(listId);
        if (bookList == null || bookList.getStatus() == null || bookList.getStatus() != 1) {
            throw new BusinessException(ResponseStatus.LIST_NOT_EXISTS);
        }
        bookList.setItems(userBookListMapper.listItems(listId));
        if (userId != null) {
            bookList.setIsMyList(userId.equals(bookList.getUserId()));
        }
        return bookList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addBook(Long userId, Long listId, Long bookId, String remark) {
        checkOwner(userId, listId);
        if (bookMapper.selectByPrimaryKey(bookId).isEmpty()) {
            throw new BusinessException(ResponseStatus.BOOK_NOT_EXISTS);
        }
        if (userBookListMapper.existsItem(listId, bookId) > 0) {
            throw new BusinessException(ResponseStatus.BOOK_IN_LIST);
        }
        UserBookListItem item = new UserBookListItem();
        item.setListId(listId);
        item.setBookId(bookId);
        if (StringUtils.isNotBlank(remark)) {
            item.setRemark(SensitiveWordHelper.replace(StringUtils.trim(remark)));
        }
        item.setCreateTime(new Date());
        userBookListMapper.insertItem(item);
        userBookListMapper.addBookCount(listId, 1);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeBook(Long userId, Long listId, Long bookId) {
        checkOwner(userId, listId);
        if (userBookListMapper.deleteItem(listId, bookId) > 0) {
            userBookListMapper.addBookCount(listId, -1);
        }
    }

    /**
     * 校验书单存在且归属当前用户
     */
    private void checkOwner(Long userId, Long listId) {
        BookListVO bookList = userBookListMapper.getById(listId);
        if (bookList == null || bookList.getStatus() == null || bookList.getStatus() != 1) {
            throw new BusinessException(ResponseStatus.LIST_NOT_EXISTS);
        }
        if (!userId.equals(bookList.getUserId())) {
            throw new BusinessException(ResponseStatus.NOT_MY_LIST);
        }
    }

    private String replaceSensitive(String content) {
        if (StringUtils.isBlank(content)) {
            return null;
        }
        if (content.length() > 300) {
            throw new BusinessException(ResponseStatus.LIST_DESC_TOO_LONG);
        }
        return SensitiveWordHelper.replace(StringUtils.trim(content));
    }
}

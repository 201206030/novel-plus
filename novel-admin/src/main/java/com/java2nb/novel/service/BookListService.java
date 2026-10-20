package com.java2nb.novel.service;

import com.github.pagehelper.PageHelper;
import com.java2nb.novel.entity.Book;
import com.java2nb.novel.entity.User;
import com.java2nb.novel.entity.UserBookList;
import com.java2nb.novel.entity.UserBookListItem;
import com.java2nb.novel.mapper.AdminUserBookListItemMapper;
import com.java2nb.novel.mapper.AdminUserBookListMapper;
import com.java2nb.novel.mapper.BookMapper;
import com.java2nb.novel.mapper.UserDynamicSqlSupport;
import com.java2nb.novel.mapper.UserMapper;
import com.java2nb.novel.vo.AdminUserBookListItemVO;
import com.java2nb.novel.vo.AdminUserBookListVO;
import io.github.xxyopen.model.page.PageBean;
import io.github.xxyopen.model.page.builder.pagehelper.PageBuilder;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static com.java2nb.novel.core.utils.AdminUtil.biz;
import static com.java2nb.novel.core.utils.AdminUtil.like;
import static com.java2nb.novel.core.utils.AdminUtil.parseIds;
import static org.mybatis.dynamic.sql.SqlBuilder.isLike;

/**
 * 书单管理Service：书单列表/书单明细
 *
 * @author Administrator
 */
@Service
@RequiredArgsConstructor
public class BookListService {

    private final AdminUserBookListMapper userBookListMapper;

    private final AdminUserBookListItemMapper userBookListItemMapper;

    private final BookMapper bookMapper;

    private final UserMapper userMapper;

    /**
     * 创建书单时搜索选择用户：用户名或昵称任一匹配，默认返回最近注册的50个
     */
    public List<User> userOptions(String keyword) {
        PageHelper.startPage(1, 50);
        List<User> list = userMapper.select(c -> {
            if (StringUtils.isNotBlank(keyword)) {
                String likeVal = like(keyword);
                return c.where(UserDynamicSqlSupport.username, isLike(likeVal))
                        .or(UserDynamicSqlSupport.nickName, isLike(likeVal))
                        .orderBy(UserDynamicSqlSupport.createTime.descending());
            }
            return c.orderBy(UserDynamicSqlSupport.createTime.descending());
        });
        list.forEach(user -> user.setPassword(null));
        return list;
    }

    public PageBean<AdminUserBookListVO> list(String listName, Long userId, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        List<AdminUserBookListVO> list = userBookListMapper.listAll(listName, userId);
        return PageBuilder.build(list);
    }

    public UserBookList get(Long id) {
        UserBookList userBookList = userBookListMapper.getById(id);
        if (userBookList == null) {
            throw biz("书单不存在");
        }
        return userBookList;
    }

    public void save(UserBookList userBookList) {
        if (userBookList.getUserId() == null) {
            throw biz("创建用户ID不能为空");
        }
        if (userBookList.getListName() == null || userBookList.getListName().isBlank()
                || userBookList.getListName().length() > 50) {
            throw biz("书单名不能为空且不能超过50个字");
        }
        Date now = new Date();
        userBookList.setBookCount(0);
        userBookList.setStatus((byte) 1);
        userBookList.setCreateTime(now);
        userBookList.setUpdateTime(now);
        userBookListMapper.insert(userBookList);
    }

    public void update(UserBookList userBookList) {
        get(userBookList.getId());
        userBookList.setUpdateTime(new Date());
        userBookListMapper.update(userBookList);
    }

    public void remove(Long id) {
        userBookListMapper.softDelete(id);
    }

    public void batchRemove(String ids) {
        List<Long> idList = parseIds(ids);
        if (!idList.isEmpty()) {
            userBookListMapper.batchSoftDelete(idList);
        }
    }

    public PageBean<AdminUserBookListItemVO> itemList(Long listId, int page, int pageSize) {
        get(listId);
        PageHelper.startPage(page, pageSize);
        List<AdminUserBookListItemVO> list = userBookListItemMapper.listByListId(listId);
        return PageBuilder.build(list);
    }

    public void itemAdd(Long listId, Long bookId, String remark) {
        get(listId);
        Book book = bookMapper.selectByPrimaryKey(bookId).orElse(null);
        if (book == null) {
            throw biz("书籍不存在！");
        }
        if (userBookListItemMapper.getByListIdAndBookId(listId, bookId) != null) {
            throw biz("该书籍已在书单中！");
        }
        UserBookListItem item = new UserBookListItem();
        item.setListId(listId);
        item.setBookId(bookId);
        item.setRemark(remark);
        item.setCreateTime(new Date());
        userBookListItemMapper.insert(item);
        userBookListMapper.refreshBookCount(listId);
    }

    public void itemUpdate(Long id, String remark) {
        UserBookListItem item = new UserBookListItem();
        item.setId(id);
        item.setRemark(remark);
        userBookListItemMapper.update(item);
    }

    public void itemRemove(Long id) {
        UserBookListItem item = userBookListItemMapper.getById(id);
        if (item == null) {
            throw biz("书单明细不存在");
        }
        userBookListItemMapper.deleteById(id);
        userBookListMapper.refreshBookCount(item.getListId());
    }

}

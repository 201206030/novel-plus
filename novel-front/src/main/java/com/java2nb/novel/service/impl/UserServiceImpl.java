package com.java2nb.novel.service.impl;

import com.github.pagehelper.PageHelper;
import io.github.xxyopen.model.page.PageBean;
import com.java2nb.novel.core.bean.UserDetails;
import com.java2nb.novel.entity.*;
import com.java2nb.novel.entity.User;
import com.java2nb.novel.service.UserService;
import com.java2nb.novel.core.enums.ResponseStatus;
import io.github.xxyopen.model.page.builder.pagehelper.PageBuilder;
import io.github.xxyopen.util.IdWorker;
import io.github.xxyopen.util.MD5Util;
import io.github.xxyopen.web.exception.BusinessException;
import com.java2nb.novel.mapper.*;
import com.java2nb.novel.vo.BookReadHistoryVO;
import com.java2nb.novel.vo.BookShelfVO;
import com.java2nb.novel.vo.UserFeedbackVO;
import io.github.xxyopen.web.util.BeanUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.Charsets;
import org.mybatis.dynamic.sql.delete.render.DeleteStatementProvider;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.update.render.UpdateStatementProvider;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static com.java2nb.novel.mapper.BookDynamicSqlSupport.id;
import static com.java2nb.novel.mapper.UserBookshelfDynamicSqlSupport.userBookshelf;
import static com.java2nb.novel.mapper.UserDynamicSqlSupport.*;
import static com.java2nb.novel.mapper.UserFeedbackDynamicSqlSupport.userFeedback;
import static com.java2nb.novel.mapper.UserReadHistoryDynamicSqlSupport.userReadHistory;
import static org.mybatis.dynamic.sql.SqlBuilder.*;
import static org.mybatis.dynamic.sql.select.SelectDSL.select;

/**
 * @author 11797
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    private final FrontUserBookshelfMapper userBookshelfMapper;

    private final FrontUserReadHistoryMapper userReadHistoryMapper;

    private final UserFeedbackMapper userFeedbackMapper;

    private final UserBuyRecordMapper userBuyRecordMapper;

    private final IdWorker idWorker = IdWorker.INSTANCE;


    @Override
    public UserDetails register(User user) {
        //查询用户名是否已注册
        SelectStatementProvider selectStatement = select(count(id))
                .from(UserDynamicSqlSupport.user)
                .where(username, isEqualTo(user.getUsername()))
                .build()
                .render(RenderingStrategies.MYBATIS3);
        long count = userMapper.count(selectStatement);
        if (count > 0) {
            //用户名已注册
            throw new BusinessException(ResponseStatus.USERNAME_EXIST);
        }
        User entity = new User();
        BeanUtils.copyProperties(user,entity);
        //数据库生成注册记录
        Long id = idWorker.nextId();
        entity.setId(id);
        entity.setNickName(entity.getUsername());
        Date currentDate = new Date();
        entity.setCreateTime(currentDate);
        entity.setUpdateTime(currentDate);
        entity.setPassword(MD5Util.MD5Encode(entity.getPassword(), Charsets.UTF_8.name()));
        userMapper.insertSelective(entity);
        //生成UserDetail对象并返回
        UserDetails userDetails = new UserDetails();
        userDetails.setId(id);
        userDetails.setUsername(entity.getUsername());
        userDetails.setNickName(entity.getNickName());
        return userDetails;
    }

    @Override
    public UserDetails login(User user) {
        //根据用户名密码查询记录
        SelectStatementProvider selectStatement = select(id, username,nickName)
                .from(UserDynamicSqlSupport.user)
                .where(username, isEqualTo(user.getUsername()))
                .and(password, isEqualTo(MD5Util.MD5Encode(user.getPassword(), Charsets.UTF_8.name())))
                .build()
                .render(RenderingStrategies.MYBATIS3);
        List<User> users = userMapper.selectMany(selectStatement);
        if (users.size() == 0) {
            throw new BusinessException(ResponseStatus.USERNAME_PASS_ERROR);
        }
        //生成UserDetail对象并返回
        UserDetails userDetails = new UserDetails();
        user = users.get(0);
        userDetails.setId(user.getId());
        userDetails.setNickName(user.getNickName());
        userDetails.setUsername(user.getUsername());
        return userDetails;
    }

    @Override
    public Boolean queryIsInShelf(Long userId, Long bookId) {
        SelectStatementProvider selectStatement = select(count(UserBookshelfDynamicSqlSupport.id))
                .from(userBookshelf)
                .where(UserBookshelfDynamicSqlSupport.userId, isEqualTo(userId))
                .and(UserBookshelfDynamicSqlSupport.bookId, isEqualTo(bookId))
                .build()
                .render(RenderingStrategies.MYBATIS3);

        return userBookshelfMapper.count(selectStatement) > 0;
    }

    @Override
    public void addToBookShelf(Long userId, Long bookId, Long preContentId) {
        if (!queryIsInShelf(userId, bookId)) {
            UserBookshelf shelf = new UserBookshelf();
            shelf.setUserId(userId);
            shelf.setBookId(bookId);
            shelf.setPreContentId(preContentId);
            shelf.setCreateTime(new Date());
            userBookshelfMapper.insert(shelf);
        }

    }

    @Override
    public void removeFromBookShelf(Long userId, Long bookId) {
        DeleteStatementProvider deleteStatement = deleteFrom(userBookshelf)
                .where(UserBookshelfDynamicSqlSupport.userId, isEqualTo(userId))
                .and(UserBookshelfDynamicSqlSupport.bookId, isEqualTo(bookId))
                .build()
                .render(RenderingStrategies.MYBATIS3);
        userBookshelfMapper.delete(deleteStatement);

    }

    @Override
    public PageBean<BookShelfVO> listBookShelfByPage(Long userId, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return PageBuilder.build(userBookshelfMapper.listBookShelf(userId));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addReadHistory(Long userId, Long bookId, Long preContentId) {

        Date currentDate = new Date();
        //删除该书以前的历史记录
        DeleteStatementProvider deleteStatement = deleteFrom(userReadHistory)
                .where(UserReadHistoryDynamicSqlSupport.bookId, isEqualTo(bookId))
                .and(UserReadHistoryDynamicSqlSupport.userId, isEqualTo(userId))
                .build()
                .render(RenderingStrategies.MYBATIS3);
        userReadHistoryMapper.delete(deleteStatement);

        //插入该书新的历史记录
        UserReadHistory userReadHistory = new UserReadHistory();
        userReadHistory.setBookId(bookId);
        userReadHistory.setUserId(userId);
        userReadHistory.setPreContentId(preContentId);
        userReadHistory.setCreateTime(currentDate);
        userReadHistory.setUpdateTime(currentDate);
        userReadHistoryMapper.insertSelective(userReadHistory);


        //更新书架的阅读历史
        UpdateStatementProvider updateStatement = update(userBookshelf)
                .set(UserBookshelfDynamicSqlSupport.preContentId)
                .equalTo(preContentId)
                .set(UserBookshelfDynamicSqlSupport.updateTime)
                .equalTo(currentDate)
                .where(UserBookshelfDynamicSqlSupport.userId, isEqualTo(userId))
                .and(UserBookshelfDynamicSqlSupport.bookId, isEqualTo(bookId))
                .build()
                .render(RenderingStrategies.MYBATIS3);

        userBookshelfMapper.update(updateStatement);


    }

    @Override
    public void addFeedBack(Long userId, String content) {
        UserFeedback feedback = new UserFeedback();
        feedback.setUserId(userId);
        feedback.setContent(content);
        feedback.setCreateTime(new Date());
        userFeedbackMapper.insertSelective(feedback);
    }

    @Override
    public PageBean<UserFeedback> listUserFeedBackByPage(Long userId, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        SelectStatementProvider selectStatement = select(UserFeedbackDynamicSqlSupport.content, UserFeedbackDynamicSqlSupport.createTime)
                .from(userFeedback)
                .where(UserFeedbackDynamicSqlSupport.userId, isEqualTo(userId))
                .orderBy(UserFeedbackDynamicSqlSupport.id.descending())
                .build()
                .render(RenderingStrategies.MYBATIS3);
        List<UserFeedback> userFeedbacks = userFeedbackMapper.selectMany(selectStatement);
        PageBean<UserFeedback> pageBean = PageBuilder.build(userFeedbacks);
        pageBean.setList(BeanUtil.copyList(userFeedbacks,UserFeedbackVO.class));
        return pageBean;
    }

    @Override
    public User userInfo(Long userId) {
        SelectStatementProvider selectStatement = select(username, nickName, userPhoto,userSex,accountBalance)
                .from(user)
                .where(id, isEqualTo(userId))
                .build()
                .render(RenderingStrategies.MYBATIS3);
        return userMapper.selectMany(selectStatement).get(0);
    }

    @Override
    public PageBean<BookReadHistoryVO> listReadHistoryByPage(Long userId, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return PageBuilder.build(userReadHistoryMapper.listReadHistory(userId));
    }

    @Override
    public void updateUserInfo(Long userId, User user) {
        user.setId(userId);
        user.setUpdateTime(new Date());
        userMapper.updateByPrimaryKeySelective(user);

    }

    @Override
    public void updatePassword(Long userId, String oldPassword, String newPassword) {
        SelectStatementProvider selectStatement = select(password)
                .from(user)
                .where(id,isEqualTo(userId))
                .build()
                .render(RenderingStrategies.MYBATIS3);
        if(!userMapper.selectMany(selectStatement).get(0).getPassword().equals(MD5Util.MD5Encode(oldPassword, Charsets.UTF_8.name()))){
            throw new BusinessException(ResponseStatus.OLD_PASSWORD_ERROR);
        }
        UpdateStatementProvider updateStatement = update(user)
                .set(password)
                .equalTo(MD5Util.MD5Encode(newPassword, Charsets.UTF_8.name()))
                .where(id,isEqualTo(userId))
                .build()
                .render(RenderingStrategies.MYBATIS3);
        userMapper.update(updateStatement);

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addAmount(Long userId, int amount) {
        User user = this.userInfo(userId);
        user.setId(userId);
        user.setAccountBalance(user.getAccountBalance()+amount);
        userMapper.updateByPrimaryKeySelective(user);

    }

    @Override
    public boolean queryIsBuyBookIndex(Long userId, Long bookIndexId) {

        return userBuyRecordMapper.count(c ->
                c.where(UserBuyRecordDynamicSqlSupport.userId, isEqualTo(userId))
                .and(UserBuyRecordDynamicSqlSupport.bookIndexId,isEqualTo(bookIndexId))) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void buyBookIndex(Long userId, UserBuyRecord buyRecord) {
        //查询用户余额
        long balance = userInfo(userId).getAccountBalance();
        if(balance<buyRecord.getBuyAmount()){
            //余额不足
            throw new BusinessException(ResponseStatus.USER_NO_BALANCE);
        }
        buyRecord.setUserId(userId);
        buyRecord.setCreateTime(new Date());
        //生成购买记录
        userBuyRecordMapper.insertSelective(buyRecord);

        //减少用户余额
        userMapper.update(update(user)
                .set(UserDynamicSqlSupport.accountBalance)
                .equalTo(balance-buyRecord.getBuyAmount())
                .where(id,isEqualTo(userId))
                .build()
                .render(RenderingStrategies.MYBATIS3));
    }

    @Override
    public int queryBuyMember(Long bookId, Date startTime, Date endTime) {
        return userMapper.selectStatistic(select(countDistinct(UserBuyRecordDynamicSqlSupport.userId))
                .from(UserBuyRecordDynamicSqlSupport.userBuyRecord)
                .where(UserBuyRecordDynamicSqlSupport.bookId,isEqualTo(bookId))
                .and(UserBuyRecordDynamicSqlSupport.createTime,isGreaterThanOrEqualTo(startTime))
                .and(UserBuyRecordDynamicSqlSupport.createTime,isLessThanOrEqualTo(endTime))
                .build()
                .render(RenderingStrategies.MYBATIS3));
    }

    @Override
    public int queryBuyCount(Long bookId, Date startTime, Date endTime) {
        return userMapper.selectStatistic(select(count(UserBuyRecordDynamicSqlSupport.id))
                .from(UserBuyRecordDynamicSqlSupport.userBuyRecord)
                .where(UserBuyRecordDynamicSqlSupport.bookId,isEqualTo(bookId))
                .and(UserBuyRecordDynamicSqlSupport.createTime,isGreaterThanOrEqualTo(startTime))
                .and(UserBuyRecordDynamicSqlSupport.createTime,isLessThanOrEqualTo(endTime))
                .build()
                .render(RenderingStrategies.MYBATIS3));
    }

    @Override
    public int queryBuyAccount(Long bookId, Date startTime, Date endTime) {
        return userMapper.selectStatistic(select(sum(UserBuyRecordDynamicSqlSupport.buyAmount))
                .from(UserBuyRecordDynamicSqlSupport.userBuyRecord)
                .where(UserBuyRecordDynamicSqlSupport.bookId,isEqualTo(bookId))
                .and(UserBuyRecordDynamicSqlSupport.createTime,isGreaterThanOrEqualTo(startTime))
                .and(UserBuyRecordDynamicSqlSupport.createTime,isLessThanOrEqualTo(endTime))
                .build()
                .render(RenderingStrategies.MYBATIS3));
    }

    @Override
    public int queryBuyTotalMember(List<Long> bookIds, Date startTime, Date endTime) {
        return userMapper.selectStatistic(select(countDistinct(UserBuyRecordDynamicSqlSupport.userId))
                .from(UserBuyRecordDynamicSqlSupport.userBuyRecord)
                .where(UserBuyRecordDynamicSqlSupport.bookId,isIn(bookIds))
                .and(UserBuyRecordDynamicSqlSupport.createTime,isGreaterThanOrEqualTo(startTime))
                .and(UserBuyRecordDynamicSqlSupport.createTime,isLessThanOrEqualTo(endTime))
                .build()
                .render(RenderingStrategies.MYBATIS3));
    }






}

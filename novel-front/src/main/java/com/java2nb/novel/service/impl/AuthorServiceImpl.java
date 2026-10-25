package com.java2nb.novel.service.impl;

import com.github.pagehelper.PageHelper;
import com.java2nb.novel.core.config.AuthorIncomeProperties;
import com.java2nb.novel.entity.Author;
import com.java2nb.novel.entity.AuthorIncome;
import com.java2nb.novel.entity.AuthorIncomeDetail;
import com.java2nb.novel.mapper.*;
import com.java2nb.novel.service.AuthorService;
import com.java2nb.novel.vo.BookIncomeSummaryVO;
import com.java2nb.novel.vo.IncomeRuleVO;
import io.github.xxyopen.model.page.PageBean;
import io.github.xxyopen.model.page.builder.pagehelper.PageBuilder;
import lombok.RequiredArgsConstructor;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static com.java2nb.novel.mapper.AuthorCodeDynamicSqlSupport.authorCode;
import static org.mybatis.dynamic.sql.SqlBuilder.*;
import static org.mybatis.dynamic.sql.select.SelectDSL.select;

/**
 * @author 11797
 */
@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorMapper authorMapper;

    private final AuthorCodeMapper authorCodeMapper;

    private final AuthorIncomeDetailMapper authorIncomeDetailMapper;


    private final AuthorIncomeMapper authorIncomeMapper;

    private final FrontAuthorIncomeDetailMapper frontAuthorIncomeDetailMapper;

    private final AuthorIncomeProperties authorIncomeProperties;


    @Override
    public Boolean checkPenName(String penName) {
        return authorMapper.count(c ->
            c.where(AuthorDynamicSqlSupport.penName, isEqualTo(penName))) > 0;
    }

    @Override
    public Boolean checkPenName(String penName, Long excludeAuthorId) {
        if (excludeAuthorId == null) {
            return checkPenName(penName);
        }
        return authorMapper.count(c ->
            c.where(AuthorDynamicSqlSupport.penName, isEqualTo(penName))
                .and(AuthorDynamicSqlSupport.id, isNotEqualTo(excludeAuthorId))) > 0;
    }

    @Override
    public Author queryAuthorAll(Long userId) {
        return authorMapper.selectOne(c ->
            c.where(AuthorDynamicSqlSupport.userId, isEqualTo(userId))).orElse(null);
    }

    @Override
    public Author queryAuthorById(Long authorId) {
        if (authorId == null) {
            return null;
        }
        return authorMapper.selectByPrimaryKey(authorId).orElse(null);
    }

    @Override
    public void updateAuthorInfo(Long userId, Author form) {
        // 仅更新资料页可编辑字段：笔名/头像/简介/手机号/QQ微信/邮箱；作品方向与状态不碰
        authorMapper.update(u -> u
            .set(AuthorDynamicSqlSupport.penName).equalTo(form.getPenName())
            .set(AuthorDynamicSqlSupport.picUrl).equalTo(form.getPicUrl())
            .set(AuthorDynamicSqlSupport.intro).equalTo(form.getIntro())
            .set(AuthorDynamicSqlSupport.telPhone).equalTo(form.getTelPhone())
            .set(AuthorDynamicSqlSupport.chatAccount).equalTo(form.getChatAccount())
            .set(AuthorDynamicSqlSupport.email).equalTo(form.getEmail())
            .where(AuthorDynamicSqlSupport.userId, isEqualTo(userId)));
    }

    @Override
    public IncomeRuleVO incomeRule() {
        IncomeRuleVO vo = new IncomeRuleVO();
        vo.setShareProportion(authorIncomeProperties.getShareProportion());
        vo.setExchangeProportion(authorIncomeProperties.getExchangeProportion());
        vo.setTaxRate(authorIncomeProperties.getTaxRate());
        return vo;
    }

    @Override
    public List<BookIncomeSummaryVO> bookIncomeSummary(Long userId, Long bookId, Date startTime, Date endTime) {
        return frontAuthorIncomeDetailMapper.bookIncomeSummary(userId, bookId, startTime, endTime);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String register(Long userId, Author author) {
        Date currentDate = new Date();
        //判断邀请码是否有效
        if (authorCodeMapper.count(c ->
            c.where(AuthorCodeDynamicSqlSupport.inviteCode, isEqualTo(author.getInviteCode()))
                .and(AuthorCodeDynamicSqlSupport.isUse, isEqualTo((byte) 0))
                .and(AuthorCodeDynamicSqlSupport.validityTime, isGreaterThan(currentDate))) > 0) {
            //邀请码有效
            //保存作家信息
            author.setUserId(userId);
            author.setCreateTime(currentDate);
            authorMapper.insertSelective(author);
            //设置邀请码状态为已使用
            authorCodeMapper.update(update(authorCode)
                .set(AuthorCodeDynamicSqlSupport.isUse)
                .equalTo((byte) 1)
                .where(AuthorCodeDynamicSqlSupport.inviteCode, isEqualTo(author.getInviteCode()))
                .build()
                .render(RenderingStrategies.MYBATIS3));
            return "";
        } else {
            //邀请码无效
            return "邀请码无效！";
        }

    }

    @Override
    public Boolean isAuthor(Long userId) {
        return authorMapper.count(c ->
            c.where(AuthorDynamicSqlSupport.userId, isEqualTo(userId))) > 0;
    }

    @Override
    public Author queryAuthor(Long userId) {
        return authorMapper.selectMany(
            select(AuthorDynamicSqlSupport.id, AuthorDynamicSqlSupport.penName, AuthorDynamicSqlSupport.status)
                .from(AuthorDynamicSqlSupport.author)
                .where(AuthorDynamicSqlSupport.userId, isEqualTo(userId))
                .build()
                .render(RenderingStrategies.MYBATIS3)).get(0);
    }

    @Override
    public List<Author> queryAuthorList(int needAuthorNumber, Date maxAuthorCreateTime) {
        return authorMapper.selectMany(select(AuthorDynamicSqlSupport.id, AuthorDynamicSqlSupport.userId)
            .from(AuthorDynamicSqlSupport.author)
            .where(AuthorDynamicSqlSupport.createTime, isLessThan(maxAuthorCreateTime))
            .orderBy(AuthorDynamicSqlSupport.createTime.descending())
            .limit(needAuthorNumber)
            .build()
            .render(RenderingStrategies.MYBATIS3));
    }


    @Override
    public boolean queryIsStatisticsDaily(Long bookId, Date date) {

        return authorIncomeDetailMapper.selectMany(select(AuthorIncomeDetailDynamicSqlSupport.id)
            .from(AuthorIncomeDetailDynamicSqlSupport.authorIncomeDetail)
            .where(AuthorIncomeDetailDynamicSqlSupport.bookId, isEqualTo(bookId))
            .and(AuthorIncomeDetailDynamicSqlSupport.incomeDate, isEqualTo(date))
            .build()
            .render(RenderingStrategies.MYBATIS3)).size() > 0;

    }

    @Override
    public void saveDailyIncomeSta(AuthorIncomeDetail authorIncomeDetail) {
        authorIncomeDetailMapper.insertSelective(authorIncomeDetail);
    }


    @Override
    public boolean queryIsStatisticsMonth(Long bookId, Date incomeDate) {
        return authorIncomeMapper.selectMany(select(AuthorIncomeDynamicSqlSupport.id)
            .from(AuthorIncomeDynamicSqlSupport.authorIncome)
            .where(AuthorIncomeDynamicSqlSupport.bookId, isEqualTo(bookId))
            .and(AuthorIncomeDynamicSqlSupport.incomeMonth, isEqualTo(incomeDate))
            .build()
            .render(RenderingStrategies.MYBATIS3)).size() > 0;
    }

    @Override
    public boolean queryIsStatisticsMonth(Long authorId, Long bookId, Date incomeDate) {
        return authorIncomeMapper.selectMany(select(AuthorIncomeDynamicSqlSupport.id)
            .from(AuthorIncomeDynamicSqlSupport.authorIncome)
            .where(AuthorIncomeDynamicSqlSupport.bookId, isEqualTo(bookId))
            .and(AuthorIncomeDynamicSqlSupport.authorId, isEqualTo(authorId))
            .and(AuthorIncomeDynamicSqlSupport.incomeMonth, isEqualTo(incomeDate))
            .build()
            .render(RenderingStrategies.MYBATIS3)).size() > 0;
    }

    @Override
    public Long queryTotalAccount(Long userId, Long bookId, Date startTime, Date endTime) {

        return authorIncomeDetailMapper.selectStatistic(select(sum(AuthorIncomeDetailDynamicSqlSupport.incomeAccount))
            .from(AuthorIncomeDetailDynamicSqlSupport.authorIncomeDetail)
            .where(AuthorIncomeDetailDynamicSqlSupport.userId, isEqualTo(userId))
            .and(AuthorIncomeDetailDynamicSqlSupport.bookId, isEqualTo(bookId))
            .and(AuthorIncomeDetailDynamicSqlSupport.incomeDate, isGreaterThanOrEqualTo(startTime))
            .and(AuthorIncomeDetailDynamicSqlSupport.incomeDate, isLessThanOrEqualTo(endTime))
            .build()
            .render(RenderingStrategies.MYBATIS3));
    }


    @Override
    public void saveAuthorIncomeSta(AuthorIncome authorIncome) {
        authorIncomeMapper.insertSelective(authorIncome);
    }

    @Override
    public boolean queryIsStatisticsDaily(Long authorId, Long bookId, Date date) {
        return authorIncomeDetailMapper.selectMany(select(AuthorIncomeDetailDynamicSqlSupport.id)
            .from(AuthorIncomeDetailDynamicSqlSupport.authorIncomeDetail)
            .where(AuthorIncomeDetailDynamicSqlSupport.authorId, isEqualTo(authorId))
            .and(AuthorIncomeDetailDynamicSqlSupport.bookId, isEqualTo(bookId))
            .and(AuthorIncomeDetailDynamicSqlSupport.incomeDate, isEqualTo(date))
            .build()
            .render(RenderingStrategies.MYBATIS3)).size() > 0;
    }


    @Override
    public PageBean<AuthorIncomeDetail> listIncomeDailyByPage(int page, int pageSize, Long userId, Long bookId,
        Date startTime, Date endTime) {
        PageHelper.startPage(page, pageSize);
        return PageBuilder.build(authorIncomeDetailMapper.selectMany(
            select(AuthorIncomeDetailDynamicSqlSupport.incomeDate, AuthorIncomeDetailDynamicSqlSupport.incomeAccount
                , AuthorIncomeDetailDynamicSqlSupport.incomeCount, AuthorIncomeDetailDynamicSqlSupport.incomeNumber)
                .from(AuthorIncomeDetailDynamicSqlSupport.authorIncomeDetail)
                .where(AuthorIncomeDetailDynamicSqlSupport.userId, isEqualTo(userId))
                .and(AuthorIncomeDetailDynamicSqlSupport.bookId, isEqualTo(bookId))
                .and(AuthorIncomeDetailDynamicSqlSupport.incomeDate, isGreaterThanOrEqualTo(startTime))
                .and(AuthorIncomeDetailDynamicSqlSupport.incomeDate, isLessThanOrEqualTo(endTime))
                .orderBy(AuthorIncomeDetailDynamicSqlSupport.incomeDate.descending())
                .build()
                .render(RenderingStrategies.MYBATIS3)));
    }


    @Override
    public PageBean<AuthorIncome> listIncomeMonthByPage(int page, int pageSize, Long userId, Long bookId) {
        PageHelper.startPage(page, pageSize);
        return PageBuilder.build(authorIncomeMapper.selectMany(select(AuthorIncomeDynamicSqlSupport.incomeMonth
            , AuthorIncomeDynamicSqlSupport.preTaxIncome
            , AuthorIncomeDynamicSqlSupport.afterTaxIncome
            , AuthorIncomeDynamicSqlSupport.payStatus
            , AuthorIncomeDynamicSqlSupport.confirmStatus)
            .from(AuthorIncomeDynamicSqlSupport.authorIncome)
            .where(AuthorIncomeDynamicSqlSupport.userId, isEqualTo(userId))
            .and(AuthorIncomeDynamicSqlSupport.bookId, isEqualTo(bookId))
            .orderBy(AuthorIncomeDynamicSqlSupport.incomeMonth.descending())
            .build()
            .render(RenderingStrategies.MYBATIS3)));
    }
}

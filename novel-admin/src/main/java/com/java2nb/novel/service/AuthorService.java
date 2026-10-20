package com.java2nb.novel.service;

import com.github.pagehelper.PageHelper;
import com.java2nb.novel.entity.Author;
import com.java2nb.novel.entity.AuthorCode;
import com.java2nb.novel.mapper.AuthorCodeDynamicSqlSupport;
import com.java2nb.novel.mapper.AuthorCodeMapper;
import com.java2nb.novel.mapper.AuthorDynamicSqlSupport;
import com.java2nb.novel.mapper.AuthorMapper;
import io.github.xxyopen.model.page.PageBean;
import io.github.xxyopen.model.page.builder.pagehelper.PageBuilder;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static com.java2nb.novel.core.utils.AdminUtil.biz;
import static com.java2nb.novel.core.utils.AdminUtil.like;
import static com.java2nb.novel.core.utils.AdminUtil.parseIds;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;
import static org.mybatis.dynamic.sql.SqlBuilder.isIn;
import static org.mybatis.dynamic.sql.SqlBuilder.isLike;
import static org.mybatis.dynamic.sql.SqlBuilder.isLikeWhenPresent;

/**
 * 作家管理Service：作者列表/邀请码管理
 *
 * @author Administrator
 */
@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorMapper authorMapper;

    private final AuthorCodeMapper authorCodeMapper;

    public PageBean<Author> authorList(String keyword, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        List<Author> list = authorMapper.select(c -> {
            if (StringUtils.isNotBlank(keyword)) {
                // 笔名或联系手机任一匹配
                String likeVal = like(keyword);
                return c.where(AuthorDynamicSqlSupport.penName, isLike(likeVal))
                        .or(AuthorDynamicSqlSupport.telPhone, isLike(likeVal))
                        .orderBy(AuthorDynamicSqlSupport.createTime.descending());
            }
            return c.orderBy(AuthorDynamicSqlSupport.createTime.descending());
        });
        return PageBuilder.build(list);
    }

    public Author authorGet(Long id) {
        return authorMapper.selectByPrimaryKey(id).orElseThrow(() -> biz("作者不存在"));
    }

    public void authorSave(Author author) {
        throw biz("作者列表不支持新增");
    }

    public void authorUpdate(Author author) {
        throw biz("作者列表不支持修改，仅可封禁或解禁");
    }

    public void authorRemove(Long id) {
        throw biz("作者列表不支持删除");
    }

    public void authorBatchRemove(String ids) {
        throw biz("作者列表不支持删除");
    }

    /**
     * 封禁/解禁作者（status：0正常 1封禁）
     */
    public void authorStatus(Long id, Byte status) {
        if (status == null || (status != 0 && status != 1)) {
            throw biz("状态值不合法");
        }
        authorMapper.selectByPrimaryKey(id).orElseThrow(() -> biz("作者不存在"));
        authorMapper.update(u -> u.set(AuthorDynamicSqlSupport.status).equalTo(status)
                .where(AuthorDynamicSqlSupport.id, isEqualTo(id)));
    }

    public PageBean<AuthorCode> codeList(String keyword, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        List<AuthorCode> list = authorCodeMapper.select(c -> {
            if (StringUtils.isNotBlank(keyword)) {
                return c.where(AuthorCodeDynamicSqlSupport.inviteCode, isLikeWhenPresent(like(keyword)))
                        .orderBy(AuthorCodeDynamicSqlSupport.createTime.descending());
            }
            return c.orderBy(AuthorCodeDynamicSqlSupport.createTime.descending());
        });
        return PageBuilder.build(list);
    }

    public AuthorCode codeGet(Long id) {
        return authorCodeMapper.selectByPrimaryKey(id).orElseThrow(() -> biz("邀请码不存在"));
    }

    public void codeSave(AuthorCode authorCode) {
        if (StringUtils.isBlank(authorCode.getInviteCode())) {
            authorCode.setInviteCode(RandomStringUtils.randomAlphanumeric(8));
        }
        if (authorCode.getValidityTime() == null) {
            authorCode.setValidityTime(new Date(System.currentTimeMillis() + 30L * 24 * 3600 * 1000));
        }
        // 新增邀请码固定为未使用
        authorCode.setIsUse((byte) 0);
        authorCode.setCreateTime(new Date());
        authorCodeMapper.insertSelective(authorCode);
    }

    public void codeUpdate(AuthorCode authorCode) {
        throw biz("邀请码不支持修改，如需调整请删除后重新新增");
    }

    public void codeRemove(Long id) {
        authorCodeMapper.deleteByPrimaryKey(id);
    }

    public void codeBatchRemove(String ids) {
        List<Long> idList = parseIds(ids);
        if (!idList.isEmpty()) {
            authorCodeMapper.delete(c -> c.where(AuthorCodeDynamicSqlSupport.id, isIn(idList)));
        }
    }

}

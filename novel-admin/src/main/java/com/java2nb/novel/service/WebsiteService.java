package com.java2nb.novel.service;

import com.github.pagehelper.PageHelper;
import com.java2nb.novel.core.cache.CacheKey;
import com.java2nb.novel.core.cache.CacheService;
import com.java2nb.novel.entity.Book;
import com.java2nb.novel.entity.BookSetting;
import com.java2nb.novel.entity.FriendLink;
import com.java2nb.novel.entity.WebsiteInfo;
import com.java2nb.novel.mapper.AdminBookSettingQueryMapper;
import com.java2nb.novel.mapper.BookDynamicSqlSupport;
import com.java2nb.novel.mapper.BookMapper;
import com.java2nb.novel.mapper.BookSettingDynamicSqlSupport;
import com.java2nb.novel.mapper.BookSettingMapper;
import com.java2nb.novel.mapper.FriendLinkDynamicSqlSupport;
import com.java2nb.novel.mapper.FriendLinkMapper;
import com.java2nb.novel.mapper.WebsiteInfoMapper;
import com.java2nb.novel.vo.AdminBookSettingVO;
import io.github.xxyopen.model.page.PageBean;
import io.github.xxyopen.model.page.builder.pagehelper.PageBuilder;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.java2nb.novel.core.utils.AdminUtil.biz;
import static com.java2nb.novel.core.utils.AdminUtil.like;
import static com.java2nb.novel.core.utils.AdminUtil.parseIds;
import static org.mybatis.dynamic.sql.SqlBuilder.isIn;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;
import static org.mybatis.dynamic.sql.SqlBuilder.isLikeWhenPresent;

/**
 * 网站管理Service：网站信息/友情链接/小说推荐
 *
 * @author Administrator
 */
@Service
@RequiredArgsConstructor
public class WebsiteService {

    private final WebsiteInfoMapper websiteInfoMapper;

    private final FriendLinkMapper friendLinkMapper;

    private final BookSettingMapper bookSettingMapper;

    private final AdminBookSettingQueryMapper bookSettingQueryMapper;

    private final BookMapper bookMapper;

    private final CacheService cacheService;

    /**
     * 查询网站信息（单条记录）
     *
     * @return 网站信息
     */
    public WebsiteInfo getWebsiteInfo() {
        return websiteInfoMapper.selectByPrimaryKey(1L).orElse(null);
    }

    /**
     * 修改网站信息
     *
     * @param websiteInfo 网站信息
     */
    public void updateWebsiteInfo(WebsiteInfo websiteInfo) {
        if (websiteInfo.getId() == null) {
            throw biz("网站信息ID不能为空");
        }
        Map<String, String> requiredFields = new LinkedHashMap<>();
        requiredFields.put("网站名称", websiteInfo.getName());
        requiredFields.put("网站域名", websiteInfo.getDomain());
        requiredFields.put("SEO关键字", websiteInfo.getKeyword());
        requiredFields.put("SEO描述", websiteInfo.getDescription());
        requiredFields.put("QQ客服", websiteInfo.getQq());
        requiredFields.put("网站LOGO", websiteInfo.getLogo());
        requiredFields.put("LOGO(深色)", websiteInfo.getLogoDark());
        for (Map.Entry<String, String> entry : requiredFields.entrySet()) {
            if (StringUtils.isBlank(entry.getValue())) {
                throw biz(entry.getKey() + "不能为空");
            }
        }
        // appDownUrl 为选填：表单提交空串时会正常落库（覆盖旧值），前台按空值隐藏 APP 下载入口
        websiteInfo.setUpdateTime(new Date());
        websiteInfoMapper.updateByPrimaryKeySelective(websiteInfo);
    }

    public PageBean<FriendLink> friendLinkList(String keyword, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        List<FriendLink> list = friendLinkMapper.select(c -> {
            if (StringUtils.isNotBlank(keyword)) {
                return c.where(FriendLinkDynamicSqlSupport.linkName, isLikeWhenPresent(like(keyword)))
                        .orderBy(FriendLinkDynamicSqlSupport.sort);
            }
            return c.orderBy(FriendLinkDynamicSqlSupport.sort);
        });
        return PageBuilder.build(list);
    }

    public FriendLink friendLinkGet(Integer id) {
        return friendLinkMapper.selectByPrimaryKey(id).orElseThrow(() -> biz("友情链接不存在"));
    }

    public void friendLinkSave(FriendLink friendLink) {
        Date now = new Date();
        friendLink.setCreateTime(now);
        friendLink.setUpdateTime(now);
        friendLinkMapper.insertSelective(friendLink);
        cacheService.del(CacheKey.INDEX_LINK_KEY);
    }

    public void friendLinkUpdate(FriendLink friendLink) {
        friendLink.setUpdateTime(new Date());
        friendLinkMapper.updateByPrimaryKeySelective(friendLink);
        cacheService.del(CacheKey.INDEX_LINK_KEY);
    }

    public void friendLinkRemove(Integer id) {
        friendLinkMapper.deleteByPrimaryKey(id);
        cacheService.del(CacheKey.INDEX_LINK_KEY);
    }

    public void friendLinkBatchRemove(String ids) {
        List<Integer> idList = parseIds(ids).stream().map(Long::intValue).toList();
        if (!idList.isEmpty()) {
            friendLinkMapper.delete(c -> c.where(FriendLinkDynamicSqlSupport.id, isIn(idList)));
            cacheService.del(CacheKey.INDEX_LINK_KEY);
        }
    }

    public PageBean<AdminBookSettingVO> settingList(Byte type, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        List<AdminBookSettingVO> list = bookSettingQueryMapper.listAll(type);
        return PageBuilder.build(list);
    }

    public BookSetting settingGet(Long id) {
        return bookSettingMapper.selectByPrimaryKey(id).orElseThrow(() -> biz("小说推荐不存在"));
    }

    public void settingSave(BookSetting bookSetting) {
        throw biz("小说推荐不支持新增，请在推荐位内调整书籍和排序");
    }

    public void settingUpdate(BookSetting bookSetting) {
        BookSetting exist = bookSettingMapper.selectByPrimaryKey(bookSetting.getId())
                .orElseThrow(() -> biz("推荐记录不存在"));
        // 推荐类型不允许修改，强制保留原值
        bookSetting.setType(exist.getType());
        bookSetting.setUpdateTime(new Date());
        bookSettingMapper.updateByPrimaryKeySelective(bookSetting);
        cacheService.del(CacheKey.INDEX_BOOK_SETTINGS_KEY);
    }

    public void settingRemove(Long id) {
        throw biz("小说推荐不支持删除");
    }

    public void settingBatchRemove(String ids) {
        throw biz("小说推荐不支持删除");
    }

    /**
     * 查询已上架小说列表（供推荐选择书籍用）
     *
     * @param keyword 书名关键字
     * @return 小说列表
     */
    public List<Book> simpleBookList(String keyword) {
        PageHelper.startPage(1, 50);
        // 仅可选择已上架（status=1）小说，keyword 为空时 like() 返回 null，isLikeWhenPresent 自动跳过
        return bookMapper.select(c -> c
                .where(BookDynamicSqlSupport.status, isEqualTo((byte) 1))
                .and(BookDynamicSqlSupport.bookName, isLikeWhenPresent(like(keyword)))
                .orderBy(BookDynamicSqlSupport.updateTime.descending()));
    }

}

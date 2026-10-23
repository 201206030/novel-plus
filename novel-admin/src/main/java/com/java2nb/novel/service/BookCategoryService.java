package com.java2nb.novel.service;

import com.github.pagehelper.PageHelper;
import com.java2nb.novel.entity.BookCategory;
import com.java2nb.novel.mapper.BookCategoryDynamicSqlSupport;
import com.java2nb.novel.mapper.BookCategoryMapper;
import com.java2nb.novel.mapper.BookDynamicSqlSupport;
import com.java2nb.novel.mapper.BookMapper;
import io.github.xxyopen.model.page.PageBean;
import io.github.xxyopen.model.page.builder.pagehelper.PageBuilder;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.java2nb.novel.core.utils.AdminUtil.biz;
import static com.java2nb.novel.core.utils.AdminUtil.like;
import static com.java2nb.novel.core.utils.AdminUtil.parseIds;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;
import static org.mybatis.dynamic.sql.SqlBuilder.isIn;
import static org.mybatis.dynamic.sql.SqlBuilder.isLike;

/**
 * 小说分类管理Service：分类增删改查；内置分类（id &lt;= 7）只允许修改不允许删除
 *
 * @author Administrator
 */
@Service
@RequiredArgsConstructor
public class BookCategoryService {

    private final BookCategoryMapper bookCategoryMapper;

    private final BookMapper bookMapper;

    /**
     * 内置小说分类ID上限：id &lt;= 该值的分类为系统预置（玄幻奇幻~女生频道），不允许删除
     */
    private static final int BUILTIN_CATEGORY_MAX_ID = 7;

    public PageBean<BookCategory> categoryList(String keyword, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        // 注意：不能 stream().toList()，否则丢失 PageHelper 的 Page 类型导致分页总数失效
        List<BookCategory> list = bookCategoryMapper.select(c -> {
            if (StringUtils.isNotBlank(keyword)) {
                return c.where(BookCategoryDynamicSqlSupport.name, isLike(like(keyword)));
            }
            return c;
        });
        // 排序：男频在前女频在后，同频道内按 sort 升序（sort 为空排最前，与预置数据行为一致）
        list.sort((a, b) -> {
            int dir = Optional.ofNullable(a.getWorkDirection()).orElse((byte) 0)
                    .compareTo(Optional.ofNullable(b.getWorkDirection()).orElse((byte) 0));
            if (dir != 0) {
                return dir;
            }
            return Optional.ofNullable(a.getSort()).orElse((byte) 0)
                    .compareTo(Optional.ofNullable(b.getSort()).orElse((byte) 0));
        });
        return PageBuilder.build(list);
    }

    public BookCategory categoryGet(Integer id) {
        return bookCategoryMapper.selectByPrimaryKey(id).orElseThrow(() -> biz("分类不存在"));
    }

    public void categorySave(BookCategory category) {
        validate(category);
        Date now = new Date();
        if (category.getSort() == null) {
            // 未填排序号时排到本频道末尾
            byte maxSort = bookCategoryMapper.select(c -> c.where(BookCategoryDynamicSqlSupport.workDirection,
                            isEqualTo(category.getWorkDirection())))
                    .stream().map(BookCategory::getSort).filter(Objects::nonNull)
                    .max(Byte::compare).orElse((byte) 0);
            category.setSort((byte) (maxSort + 1));
        }
        category.setCreateTime(now);
        category.setUpdateTime(now);
        bookCategoryMapper.insertSelective(category);
    }

    /**
     * 修改分类。内置分类允许修改（改名/排序），频道创建后固定不可改；
     * 改名后同步刷新 book 表冗余的 cat_name。
     */
    @Transactional(rollbackFor = Exception.class)
    public void categoryUpdate(BookCategory category) {
        BookCategory exist = categoryGet(category.getId());
        // 频道固定：分类下小说的 cat_id 与前台频道筛选都依赖它，创建后不允许修改
        if (category.getWorkDirection() != null && !category.getWorkDirection().equals(exist.getWorkDirection())) {
            throw biz("分类所属频道创建后不可修改");
        }
        validateForUpdate(category, exist);
        category.setUpdateTime(new Date());
        bookCategoryMapper.updateByPrimaryKeySelective(category);
        // 改名后同步小说表冗余分类名，避免前台榜单/详情页展示的旧名称与新分类名不一致
        if (StringUtils.isNotBlank(category.getName()) && !category.getName().equals(exist.getName())) {
            bookMapper.update(u -> u.set(BookDynamicSqlSupport.catName).equalTo(category.getName())
                    .where(BookDynamicSqlSupport.catId, isEqualTo(category.getId())));
        }
    }

    /**
     * 删除分类。内置分类（0 &lt;= id &lt;= 7）禁止删除；已被小说引用的分类禁止删除，防止前台数据出现悬空分类。
     */
    public void categoryRemove(Integer id) {
        categoryGet(id);
        assertRemovable(id);
        bookCategoryMapper.deleteByPrimaryKey(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void categoryBatchRemove(String ids) {
        List<Integer> idList = parseIds(ids).stream().map(Long::intValue).toList();
        if (idList.isEmpty()) {
            return;
        }
        // 整批校验：任一内置分类混入即拒绝，避免"部分删除成功部分失败"的中间态
        for (Integer id : idList) {
            assertRemovable(id);
        }
        for (Integer id : idList) {
            long bookCount = bookMapper.count(c -> c.where(BookDynamicSqlSupport.catId, isEqualTo(id)));
            if (bookCount > 0) {
                throw biz("分类【" + categoryGet(id).getName() + "】下仍有 " + bookCount + " 本小说，无法删除");
            }
        }
        bookCategoryMapper.delete(c -> c.where(BookCategoryDynamicSqlSupport.id, isIn(idList)));
    }

    /**
     * 内置分类删除保护
     */
    private void assertRemovable(Integer id) {
        if (id != null && id <= BUILTIN_CATEGORY_MAX_ID) {
            throw biz("内置分类不允许删除，可以修改名称或排序");
        }
    }

    /**
     * 新增校验：名称必填且同频道内不重名，频道值合法，排序号合法
     */
    private void validate(BookCategory category) {
        checkBase(category);
        checkNameDuplicate(category.getWorkDirection(), category.getName(), null);
    }

    /**
     * 修改校验：基础字段合法；名称或频道有变动时才查重（频道已被锁定，实际仅在改名时触发）
     */
    private void validateForUpdate(BookCategory category, BookCategory exist) {
        checkBase(category);
        boolean nameChanged = StringUtils.isNotBlank(category.getName()) && !category.getName().equals(exist.getName());
        if (nameChanged) {
            checkNameDuplicate(category.getWorkDirection(), category.getName(), category.getId());
        }
    }

    /**
     * 新增/修改公共校验：名称必填且长度合法，频道值合法，排序号非负
     */
    private void checkBase(BookCategory category) {
        if (StringUtils.isBlank(category.getName())) {
            throw biz("分类名称不能为空");
        }
        if (category.getName().length() > 10) {
            throw biz("分类名称不能超过10个字符");
        }
        Byte workDirection = category.getWorkDirection();
        if (workDirection == null || (workDirection != 0 && workDirection != 1)) {
            throw biz("作品方向不合法，只能为男频或女频");
        }
        if (category.getSort() != null && category.getSort() < 0) {
            throw biz("排序号不能为负数");
        }
    }

    /**
     * 同频道内名称唯一校验，避免前台按 cat_name 展示时出现两个同名分类
     *
     * @param workDirection 频道
     * @param name        分类名称
     * @param excludeId   排除自身ID，新增传 null
     */
    private void checkNameDuplicate(Byte workDirection, String name, Integer excludeId) {
        boolean duplicate = bookCategoryMapper.select(c -> c
                        .where(BookCategoryDynamicSqlSupport.workDirection, isEqualTo(workDirection))
                        .and(BookCategoryDynamicSqlSupport.name, isEqualTo(name)))
                .stream().anyMatch(existing -> !existing.getId().equals(excludeId));
        if (duplicate) {
            throw biz("同频道下已存在名称为【" + name + "】的分类");
        }
    }

}

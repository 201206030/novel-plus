package com.java2nb.novel.service;

import com.github.pagehelper.PageHelper;
import com.java2nb.novel.entity.BookComment;
import com.java2nb.novel.mapper.AdminBookCommentQueryMapper;
import com.java2nb.novel.mapper.AdminBookParagraphCommentMapper;
import com.java2nb.novel.mapper.BookCommentDynamicSqlSupport;
import com.java2nb.novel.mapper.BookCommentMapper;
import com.java2nb.novel.vo.AdminBookCommentVO;
import com.java2nb.novel.vo.AdminBookParagraphCommentVO;
import io.github.xxyopen.model.page.PageBean;
import io.github.xxyopen.model.page.builder.pagehelper.PageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.java2nb.novel.core.utils.AdminUtil.biz;
import static com.java2nb.novel.core.utils.AdminUtil.parseIds;

import static org.mybatis.dynamic.sql.SqlBuilder.isIn;

/**
 * 评论管理Service：小说评论/段评
 *
 * @author Administrator
 */
@Service
@RequiredArgsConstructor
public class CommentService {

    private final BookCommentMapper bookCommentMapper;

    private final AdminBookCommentQueryMapper bookCommentQueryMapper;

    private final AdminBookParagraphCommentMapper paragraphCommentMapper;

    public PageBean<AdminBookCommentVO> commentList(Long bookId, String keyword, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        List<AdminBookCommentVO> list = bookCommentQueryMapper.listAll(bookId, keyword);
        return PageBuilder.build(list);
    }

    public AdminBookCommentVO commentGet(Long id) {
        return bookCommentQueryMapper.getById(id);
    }

    public void commentUpdate(BookComment bookComment) {
        throw biz("书评不支持修改，如需调整请删除后由用户重新发布");
    }

    public void commentRemove(Long id) {
        bookCommentMapper.deleteByPrimaryKey(id);
    }

    public void commentBatchRemove(String ids) {
        List<Long> idList = parseIds(ids);
        if (!idList.isEmpty()) {
            bookCommentMapper.delete(c -> c.where(BookCommentDynamicSqlSupport.id, isIn(idList)));
        }
    }

    public PageBean<AdminBookParagraphCommentVO> paragraphList(Long bookId, String keyword, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        List<AdminBookParagraphCommentVO> list = paragraphCommentMapper.listAll(bookId, keyword);
        return PageBuilder.build(list);
    }

    public AdminBookParagraphCommentVO paragraphGet(Long id) {
        return paragraphCommentMapper.getById(id);
    }

    public void paragraphUpdate(Long id, String commentContent) {
        throw biz("段评不支持修改，如需调整请删除后由用户重新发布");
    }

    public void paragraphRemove(Long id) {
        paragraphCommentMapper.deleteById(id);
    }

    public void paragraphBatchRemove(String ids) {
        List<Long> idList = parseIds(ids);
        if (!idList.isEmpty()) {
            paragraphCommentMapper.batchDeleteByIds(idList);
        }
    }

}

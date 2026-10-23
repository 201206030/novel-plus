package com.java2nb.novel.service;

import com.github.pagehelper.PageHelper;
import com.java2nb.novel.entity.BookComment;
import com.java2nb.novel.entity.BookCommentReply;
import com.java2nb.novel.mapper.AdminBookCommentQueryMapper;
import com.java2nb.novel.mapper.AdminBookCommentReplyQueryMapper;
import com.java2nb.novel.mapper.AdminBookParagraphCommentMapper;
import com.java2nb.novel.mapper.BookCommentDynamicSqlSupport;
import com.java2nb.novel.mapper.BookCommentMapper;
import com.java2nb.novel.mapper.BookCommentReplyDynamicSqlSupport;
import com.java2nb.novel.mapper.BookCommentReplyMapper;
import com.java2nb.novel.vo.AdminBookCommentReplyVO;
import com.java2nb.novel.vo.AdminBookCommentVO;
import com.java2nb.novel.vo.AdminBookParagraphCommentVO;
import io.github.xxyopen.model.page.PageBean;
import io.github.xxyopen.model.page.builder.pagehelper.PageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.java2nb.novel.core.utils.AdminUtil.biz;
import static com.java2nb.novel.core.utils.AdminUtil.parseIds;

import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;
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

    private final BookCommentReplyMapper bookCommentReplyMapper;

    private final AdminBookCommentReplyQueryMapper bookCommentReplyQueryMapper;

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

    @Transactional(rollbackFor = Exception.class)
    public void commentRemove(Long id) {
        // 级联删除该评论下的回复，避免产生孤儿回复数据
        bookCommentReplyMapper.delete(c -> c.where(BookCommentReplyDynamicSqlSupport.commentId, isEqualTo(id)));
        bookCommentMapper.deleteByPrimaryKey(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void commentBatchRemove(String ids) {
        List<Long> idList = parseIds(ids);
        if (!idList.isEmpty()) {
            bookCommentReplyMapper.delete(c -> c.where(BookCommentReplyDynamicSqlSupport.commentId, isIn(idList)));
            bookCommentMapper.delete(c -> c.where(BookCommentDynamicSqlSupport.id, isIn(idList)));
        }
    }

    /**
     * 分页查询某条评论下的回复列表（含回复用户昵称）
     */
    public PageBean<AdminBookCommentReplyVO> commentReplyList(Long commentId, String keyword, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        List<AdminBookCommentReplyVO> list = bookCommentReplyQueryMapper.listByComment(commentId, keyword);
        return PageBuilder.build(list);
    }

    /**
     * 删除单条回复，并回写所属评论的回复数
     */
    @Transactional(rollbackFor = Exception.class)
    public void commentReplyRemove(Long id) {
        BookCommentReply reply = bookCommentReplyMapper.selectByPrimaryKey(id).orElseThrow(() -> biz("回复不存在"));
        bookCommentReplyMapper.deleteByPrimaryKey(id);
        if (reply.getCommentId() != null) {
            decrementReplyCount(reply.getCommentId(), 1);
        }
    }

    /**
     * 批量删除回复，按所属评论分组回写回复数
     */
    @Transactional(rollbackFor = Exception.class)
    public void commentReplyBatchRemove(String ids) {
        List<Long> idList = parseIds(ids);
        if (idList.isEmpty()) {
            return;
        }
        // 先按所属评论统计待删除回复数，删除后逐评论回写，避免前台“回复数”与实际不一致
        Map<Long, Long> countByComment = bookCommentReplyMapper.select(c -> c
                        .where(BookCommentReplyDynamicSqlSupport.id, isIn(idList)))
                .stream().filter(r -> r.getCommentId() != null)
                .collect(Collectors.groupingBy(BookCommentReply::getCommentId, Collectors.counting()));
        bookCommentReplyMapper.delete(c -> c.where(BookCommentReplyDynamicSqlSupport.id, isIn(idList)));
        countByComment.forEach((commentId, cnt) -> decrementReplyCount(commentId, cnt.intValue()));
    }

    /**
     * 将指定评论的回复数减少 n（不低于0）
     */
    private void decrementReplyCount(Long commentId, int n) {
        bookCommentMapper.selectByPrimaryKey(commentId).ifPresent(comment -> {
            int current = comment.getReplyCount() == null ? 0 : comment.getReplyCount();
            bookCommentMapper.update(u -> u.set(BookCommentDynamicSqlSupport.replyCount)
                    .equalTo(Math.max(0, current - n))
                    .where(BookCommentDynamicSqlSupport.id, isEqualTo(commentId)));
        });
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

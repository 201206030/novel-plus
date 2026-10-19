package com.java2nb.novel.service.impl;

import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import com.github.pagehelper.PageHelper;
import com.java2nb.novel.core.enums.ResponseStatus;
import com.java2nb.novel.entity.BookParagraphComment;
import com.java2nb.novel.mapper.FrontBookParagraphCommentMapper;
import com.java2nb.novel.service.BookParagraphCommentService;
import com.java2nb.novel.vo.BookParagraphCommentVO;
import io.github.xxyopen.model.page.PageBean;
import io.github.xxyopen.model.page.builder.pagehelper.PageBuilder;
import io.github.xxyopen.web.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 书籍段落评论服务实现
 *
 * @author 11797
 */
@Service
@RequiredArgsConstructor
public class BookParagraphCommentServiceImpl implements BookParagraphCommentService {

    /**
     * 评论内容最大长度，与前端输入框 maxlength 保持一致
     */
    private static final int COMMENT_MAX_LENGTH = 200;

    /**
     * 顶层评论标识（root_id=0）
     */
    private static final long ROOT_COMMENT = 0L;

    private final FrontBookParagraphCommentMapper bookParagraphCommentMapper;

    @Override
    public Map<Integer, Integer> listParagraphCommentCounts(Long bookIndexId) {
        Map<Integer, Integer> counts = new LinkedHashMap<>();
        if (bookIndexId == null) {
            return counts;
        }
        List<Map<String, Object>> rows = bookParagraphCommentMapper.countGroupByParagraph(bookIndexId);
        for (Map<String, Object> row : rows) {
            Object idx = row.get("idx");
            Object num = row.get("num");
            if (idx != null && num != null) {
                counts.put(((Number) idx).intValue(), ((Number) num).intValue());
            }
        }
        return counts;
    }

    @Override
    public List<BookParagraphCommentVO> listParagraphComment(Long bookIndexId, Integer paragraphIdx) {
        if (bookIndexId == null || paragraphIdx == null || paragraphIdx < 0) {
            throw new BusinessException(ResponseStatus.PARA_COMMENT_PARAM_INVALID);
        }
        // 正序取回全部（顶层+回复），组装两级树：顶层按时间倒序（新在前），回复按时间正序
        List<BookParagraphCommentVO> all = bookParagraphCommentMapper.listByParagraph(bookIndexId, paragraphIdx);
        List<BookParagraphCommentVO> tops = new ArrayList<>();
        Map<Long, List<BookParagraphCommentVO>> replyMap = new LinkedHashMap<>();
        for (BookParagraphCommentVO vo : all) {
            Long rootId = vo.getRootId();
            if (rootId == null || rootId == ROOT_COMMENT) {
                tops.add(vo);
            } else {
                replyMap.computeIfAbsent(rootId, k -> new ArrayList<>()).add(vo);
            }
        }
        Collections.reverse(tops);
        for (BookParagraphCommentVO top : tops) {
            top.setReplies(replyMap.getOrDefault(top.getId(), Collections.emptyList()));
            top.setReplyCount(top.getReplies().size());
        }
        return tops;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addParagraphComment(Long userId, BookParagraphComment comment) {
        if (comment.getBookId() == null || comment.getBookIndexId() == null
            || comment.getParagraphIdx() == null || comment.getParagraphIdx() < 0) {
            throw new BusinessException(ResponseStatus.PARA_COMMENT_PARAM_INVALID);
        }
        String content = StringUtils.trim(comment.getCommentContent());
        if (StringUtils.isBlank(content) || content.length() > COMMENT_MAX_LENGTH) {
            throw new BusinessException(ResponseStatus.PARA_COMMENT_CONTENT_INVALID);
        }
        comment.setCommentContent(SensitiveWordHelper.replace(content));
        comment.setCreateUserId(userId);
        comment.setCreateTime(new Date());
        Long rootId = comment.getRootId();
        if (rootId == null || rootId == ROOT_COMMENT) {
            // 顶层评论
            comment.setRootId(ROOT_COMMENT);
            comment.setReplyUserName(null);
            comment.setReplyCount(0);
            bookParagraphCommentMapper.insert(comment);
            return;
        }
        // 回复：校验根评论存在且是同一段落的顶层评论，坐标以根评论为准防篡改
        BookParagraphCommentVO root = bookParagraphCommentMapper.getById(rootId);
        if (root == null || (root.getRootId() != null && root.getRootId() != ROOT_COMMENT)) {
            throw new BusinessException(ResponseStatus.PARA_COMMENT_PARAM_INVALID);
        }
        if (!root.getBookIndexId().equals(comment.getBookIndexId())
            || !root.getParagraphIdx().equals(comment.getParagraphIdx())) {
            throw new BusinessException(ResponseStatus.PARA_COMMENT_PARAM_INVALID);
        }
        String replyUserName = StringUtils.trim(comment.getReplyUserName());
        if (StringUtils.isNotBlank(replyUserName)) {
            if (replyUserName.length() > 50) {
                replyUserName = replyUserName.substring(0, 50);
            }
            comment.setReplyUserName(SensitiveWordHelper.replace(replyUserName));
        } else {
            comment.setReplyUserName(null);
        }
        comment.setBookId(root.getBookId());
        comment.setParagraphIdx(root.getParagraphIdx());
        bookParagraphCommentMapper.insert(comment);
        bookParagraphCommentMapper.addReplyCount(rootId);
    }

    @Override
    public PageBean<BookParagraphCommentVO> listMyParagraphCommentByPage(Long userId, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return PageBuilder.build(bookParagraphCommentMapper.listMyParagraphComment(userId));
    }
}

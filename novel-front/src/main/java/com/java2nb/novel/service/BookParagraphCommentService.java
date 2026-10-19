package com.java2nb.novel.service;

import com.java2nb.novel.entity.BookParagraphComment;
import com.java2nb.novel.vo.BookParagraphCommentVO;
import io.github.xxyopen.model.page.PageBean;

import java.util.List;
import java.util.Map;

/**
 * 书籍段落评论服务
 *
 * @author 11797
 */
public interface BookParagraphCommentService {

    /**
     * 查询章节内各段落评论数
     *
     * @return key：段落序号，value：评论数
     */
    Map<Integer, Integer> listParagraphCommentCounts(Long bookIndexId);

    /**
     * 查询某段落评论列表（按时间倒序）
     */
    List<BookParagraphCommentVO> listParagraphComment(Long bookIndexId, Integer paragraphIdx);

    /**
     * 新增段落评论
     */
    void addParagraphComment(Long userId, BookParagraphComment comment);

    /**
     * 分页查询我的段评（含回复，按时间倒序）
     */
    PageBean<BookParagraphCommentVO> listMyParagraphCommentByPage(Long userId, int page, int pageSize);
}

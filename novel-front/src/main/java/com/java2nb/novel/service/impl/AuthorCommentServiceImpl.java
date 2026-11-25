package com.java2nb.novel.service.impl;

import com.github.pagehelper.PageHelper;
import com.java2nb.novel.core.enums.ResponseStatus;
import com.java2nb.novel.entity.Book;
import com.java2nb.novel.entity.BookComment;
import com.java2nb.novel.entity.BookCommentReply;
import com.java2nb.novel.entity.BookParagraphComment;
import com.java2nb.novel.mapper.BookDynamicSqlSupport;
import com.java2nb.novel.mapper.FrontBookMapper;
import com.java2nb.novel.mapper.FrontBookParagraphCommentMapper;
import com.java2nb.novel.service.AuthorCommentService;
import com.java2nb.novel.service.BookParagraphCommentService;
import com.java2nb.novel.service.BookService;
import com.java2nb.novel.vo.BookCommentReplyVO;
import com.java2nb.novel.vo.BookCommentVO;
import com.java2nb.novel.vo.BookParagraphCommentVO;
import io.github.xxyopen.model.page.PageBean;
import io.github.xxyopen.model.page.builder.pagehelper.PageBuilder;
import io.github.xxyopen.web.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.java2nb.novel.mapper.BookDynamicSqlSupport.book;
import static com.java2nb.novel.mapper.BookDynamicSqlSupport.id;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;
import static org.mybatis.dynamic.sql.select.SelectDSL.select;

/**
 * 作家后台作品评论管理服务实现
 *
 * @author 11797
 */
@Service
@RequiredArgsConstructor
public class AuthorCommentServiceImpl implements AuthorCommentService {

    /**
     * 回复内容最大长度，与前台评论保持一致
     */
    private static final int REPLY_MAX_LENGTH = 200;

    /**
     * 顶层段评标识
     */
    private static final long ROOT_COMMENT = 0L;

    private final FrontBookMapper bookMapper;

    private final FrontBookParagraphCommentMapper paragraphCommentMapper;

    private final BookService bookService;

    private final BookParagraphCommentService paragraphCommentService;

    @Override
    public List<Book> listMyBooks(Long authorId) {
        SelectStatementProvider stmt = select(book.allColumns())
            .from(book)
            .where(BookDynamicSqlSupport.authorId, isEqualTo(authorId))
            .build()
            .render(RenderingStrategies.MYBATIS3);
        return bookMapper.selectMany(stmt);
    }

    @Override
    public Book checkBookOwned(Long bookId, Long authorId) {
        SelectStatementProvider stmt = select(book.allColumns())
            .from(book)
            .where(id, isEqualTo(bookId))
            .and(BookDynamicSqlSupport.authorId, isEqualTo(authorId))
            .build()
            .render(RenderingStrategies.MYBATIS3);
        List<Book> books = bookMapper.selectMany(stmt);
        if (books.isEmpty()) {
            throw new BusinessException(ResponseStatus.BOOK_NOT_OWNED);
        }
        return books.get(0);
    }

    @Override
    public PageBean<BookCommentReplyVO> listBookReplies(Long authorId, Long commentId, int page, int pageSize) {
        BookComment comment = bookService.getBookComment(commentId);
        if (comment == null) {
            throw new BusinessException(ResponseStatus.COMMENT_NOT_EXISTS);
        }
        checkBookOwned(comment.getBookId(), authorId);
        return bookService.listCommentReplyByPage(null, commentId, page, pageSize);
    }

    @Override
    public PageBean<BookCommentVO> listBookComments(Long authorId, Long bookId, int page, int pageSize) {
        checkBookOwned(bookId, authorId);
        return bookService.listCommentByPage(null, bookId, page, pageSize);
    }

    @Override
    public PageBean<BookParagraphCommentVO> listParagraphComments(Long authorId, Long bookId, int page, int pageSize) {
        checkBookOwned(bookId, authorId);
        PageHelper.startPage(page, pageSize);
        PageBean<BookParagraphCommentVO> pageBean = PageBuilder.build(paragraphCommentMapper.listTopByBook(bookId));
        List<? extends BookParagraphCommentVO> tops = pageBean.getList();
        if (tops.isEmpty()) {
            return pageBean;
        }
        // 分页查顶层评论后，再批量补齐当前页的回复（此时 PageHelper 分页上下文已消费，不会干扰本查询）
        List<Long> rootIds = tops.stream().map(BookParagraphCommentVO::getId).collect(Collectors.toList());
        List<BookParagraphCommentVO> replies = paragraphCommentMapper.listRepliesByRootIds(rootIds);
        Map<Long, List<BookParagraphCommentVO>> replyMap = replies.stream()
            .collect(Collectors.groupingBy(BookParagraphCommentVO::getRootId));
        tops.forEach(top -> top.setReplies(replyMap.getOrDefault(top.getId(), Collections.emptyList())));
        return pageBean;
    }

    @Override
    public void replyBookComment(Long userId, Long authorId, Long commentId, String content) {
        BookComment comment = bookService.getBookComment(commentId);
        if (comment == null) {
            throw new BusinessException(ResponseStatus.COMMENT_NOT_EXISTS);
        }
        checkBookOwned(comment.getBookId(), authorId);
        validateContent(content);

        BookCommentReply reply = new BookCommentReply();
        reply.setCommentId(commentId);
        reply.setReplyContent(StringUtils.trim(content));
        // 作家身份由 create_user_id 关联 author 表统一识别，不再写 location，避免前台拼出"作家回复读者"
        bookService.addBookCommentReply(userId, reply);
    }

    @Override
    public void replyParagraphComment(Long userId, Long authorId, Long rootId, String content,
        Long replyUserId, String replyUserName) {
        BookParagraphCommentVO root = paragraphCommentMapper.getById(rootId);
        if (root == null || (root.getRootId() != null && root.getRootId() != ROOT_COMMENT)) {
            throw new BusinessException(ResponseStatus.COMMENT_NOT_EXISTS);
        }
        checkBookOwned(root.getBookId(), authorId);
        validateContent(content);

        BookParagraphComment reply = new BookParagraphComment();
        // 坐标一律以根评论为准，防止前端篡改
        reply.setBookId(root.getBookId());
        reply.setBookIndexId(root.getBookIndexId());
        reply.setParagraphIdx(root.getParagraphIdx());
        reply.setRootId(rootId);
        reply.setReplyUserId(replyUserId);
        reply.setReplyUserName(replyUserName);
        reply.setCommentContent(StringUtils.trim(content));
        paragraphCommentService.addParagraphComment(userId, reply);
    }

    private void validateContent(String content) {
        String text = StringUtils.trim(content);
        if (StringUtils.isBlank(text) || text.length() > REPLY_MAX_LENGTH) {
            throw new BusinessException(ResponseStatus.COMMENT_REPLY_CONTENT_INVALID);
        }
    }
}

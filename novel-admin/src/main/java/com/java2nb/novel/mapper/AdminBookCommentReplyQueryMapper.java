package com.java2nb.novel.mapper;

import com.java2nb.novel.vo.AdminBookCommentReplyVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 书评回复管理查询Mapper（列表联表查询，删除复用公共BookCommentReplyMapper）
 *
 * @author Administrator
 */
@Mapper
public interface AdminBookCommentReplyQueryMapper {

    /**
     * 分页查询某条评论下的回复列表（含回复用户昵称）
     *
     * @param commentId 所属评论ID（必填）
     * @param keyword   回复内容关键字（可空）
     * @return 回复列表
     */
    List<AdminBookCommentReplyVO> listByComment(@Param("commentId") Long commentId, @Param("keyword") String keyword);

}

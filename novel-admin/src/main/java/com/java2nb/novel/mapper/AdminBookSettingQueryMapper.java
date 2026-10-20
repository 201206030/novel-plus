package com.java2nb.novel.mapper;

import com.java2nb.novel.vo.AdminBookSettingVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 小说推荐管理查询Mapper（列表联表查询，增删改复用公共BookSettingMapper）
 *
 * @author Administrator
 */
@Mapper
public interface AdminBookSettingQueryMapper {

    /**
     * 分页查询推荐列表（含书名/作者名）
     *
     * @param type 推荐类型（可空）
     * @return 推荐列表
     */
    List<AdminBookSettingVO> listAll(@Param("type") Byte type);

}

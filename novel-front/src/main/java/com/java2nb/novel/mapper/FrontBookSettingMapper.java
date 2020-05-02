package com.java2nb.novel.mapper;

import com.java2nb.novel.vo.BookSettingVO;

import java.util.List;

/**
 * @author Administrator
 */
public interface FrontBookSettingMapper extends BookSettingMapper {

    List<BookSettingVO> listVO();
}

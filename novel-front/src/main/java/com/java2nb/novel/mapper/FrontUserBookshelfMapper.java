package com.java2nb.novel.mapper;

import com.java2nb.novel.vo.BookShelfVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Administrator
 */
public interface FrontUserBookshelfMapper extends UserBookshelfMapper {

    List<BookShelfVO> listBookShelf(@Param("userId") Long userId);

}

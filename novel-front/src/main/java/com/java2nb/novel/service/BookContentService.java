package com.java2nb.novel.service;

import com.java2nb.novel.entity.BookContent;

import java.util.List;

public interface BookContentService {

    BookContent queryBookContent(Long bookId, Long bookIndexId);

}

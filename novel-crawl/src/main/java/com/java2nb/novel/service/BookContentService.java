package com.java2nb.novel.service;

import com.java2nb.novel.entity.BookContent;

import java.util.List;

public interface BookContentService {

    void saveBookContent(List<BookContent> bookContentList,Long bookId);

    void saveBookContent(BookContent bookContent,Long bookId);

    void updateBookContent(BookContent bookContent,Long bookId);


}

package com.java2nb.novel.service.impl;

import com.java2nb.novel.core.utils.FileUtil;
import com.java2nb.novel.entity.BookContent;
import com.java2nb.novel.service.BookContentService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "txt.save", name = "storage", havingValue = "file")
public class FileBookContentServiceImpl implements BookContentService {

    @Value("${txt.save.path}")
    private String fileSavePath;

    @SneakyThrows
    @Override
    public BookContent queryBookContent(Long bookId, Long bookIndexId) {
        BufferedReader in = new BufferedReader(new FileReader(fileSavePath + "/" + bookId + "/" + bookIndexId + ".txt"));
        StringBuffer sb = new StringBuffer();
        String str;
        while ((str = in.readLine()) != null) {
            sb.append(str);
        }
        in.close();
        return new BookContent() {{
            setIndexId(bookIndexId);
            setContent(sb.toString());
        }};
    }
}

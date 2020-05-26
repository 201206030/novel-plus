package com.java2nb.novel.service.impl;

import com.java2nb.novel.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * @author 11797
 */
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "pic.save", name = "storage", havingValue = "fastDfs")
public class FastDfsFileServiceImpl implements FileService {

    @Override
    public String transFile(String picSrc, String picSavePath) {
        return null;
    }
}

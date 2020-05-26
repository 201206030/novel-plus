package com.java2nb.novel.service;


/**
 * @author 11797
 */
public interface FileService {

    /**
     * 将爬取的网络图片转存为自己的存储介质（本地、OSS、fastDfs）
     * @param picSrc 爬取的网络图片路径
     * @param picSavePath 保存路径
     * @return 新图片地址
     * */
    String transFile(String picSrc, String picSavePath);

}

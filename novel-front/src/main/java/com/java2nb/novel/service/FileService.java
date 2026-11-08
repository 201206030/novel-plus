package com.java2nb.novel.service;


import org.springframework.web.multipart.MultipartFile;

/**
 * 文件存储服务接口。
 * <p>
 * 根据配置文件 {@code pic.save.storage} 自动选择实现：
 * <ul>
 *   <li>{@code local} — {@link com.java2nb.novel.service.impl.LocalFileServiceImpl}</li>
 *   <li>{@code OSS} — {@link com.java2nb.novel.service.impl.OssFileServiceImpl}</li>
 * </ul>
 * 无论是图片上传（FileController）还是爬取图片转存（CrawlPicTransSchedule），
 * 统一通过此接口走配置驱动的存储策略。
 *
 * @author 11797
 */
public interface FileService {

    /**
     * 将爬取的网络图片转存为自己的存储介质（本地、OSS、fastDfs）
     *
     * @param picSrc      爬取的网络图片路径
     * @param picSavePath 保存路径
     * @return 新图片地址
     */
    String transFile(String picSrc, String picSavePath);

    /**
     * 用户上传的图片文件存储到指定介质（本地、OSS）。
     * <p>
     * 与 {@link #transFile} 共享同一套存储策略，根据配置自动选择本地或 OSS。
     *
     * @param file         用户上传的 MultipartFile
     * @param picSavePath  本地保存路径根目录（OSS 模式下作为临时中转目录）
     * @return 存储后的图片访问路径
     */
    String uploadFile(MultipartFile file, String picSavePath);

}

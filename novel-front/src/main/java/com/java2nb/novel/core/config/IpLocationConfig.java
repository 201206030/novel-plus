package com.java2nb.novel.core.config;

import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * IP 地址定位配置类
 *
 * @author xiongxiaoyang
 * @date 2025/6/30
 */
@Slf4j
@Configuration
public class IpLocationConfig {

    /**
     * 使用 {@link Searcher} 实现高效的本地 IP 查询服务， 创建基于内存的 IP 地址查询对象，支持并发访问且仅需初始化一次。
     *
     * <p>该方法会将 ip2region.xdb 数据库文件加载到内存中，
     * 并构建一个线程安全的 {@link Searcher} 实例，可用于高效、并发的 IP 地址定位查询。</p>
     *
     * <p>{@link Searcher} 实例是线程安全的，可以作为全局单例在整个应用中跨线程使用。</p>
     *
     * <p>通过配置 destroyMethod="close"，确保在 Spring 容器关闭时自动释放底层资源。</p>
     */
    @Bean(destroyMethod = "close")
    public Searcher searcher() throws IOException {
        // 1、从 classpath 加载整个 xdb 到内存。
        try (InputStream inputStream = new ClassPathResource("ip2region.xdb").getInputStream()) {
            File tempDbFile = File.createTempFile("ip2region", ".xdb");
            try (FileOutputStream outputStream = new FileOutputStream(tempDbFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
            // 确保程序退出时删除临时文件
            tempDbFile.deleteOnExit();
            byte[] cBuff = Searcher.loadContentFromFile(tempDbFile.getPath());

            // 2、使用上述的 cBuff 创建一个完全基于内存的查询对象。
            return Searcher.newWithBuffer(cBuff);
        }
    }

}

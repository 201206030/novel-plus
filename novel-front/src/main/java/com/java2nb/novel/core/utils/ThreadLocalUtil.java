package com.java2nb.novel.core.utils;

import com.java2nb.novel.core.cache.CacheKey;
import com.java2nb.novel.core.cache.CacheService;
import io.github.xxyopen.web.util.SpringUtil;

/**
 * 模板操作工具类
 * @author Administrator
 */
public class ThreadLocalUtil {

    /**
     * 存储当前线程访问的模板目录
     * */
    private static final ThreadLocal<String> templateDir = new ThreadLocal<>();

    /**
     * 存储当前会话的sessionID
     * */
    private static final ThreadLocal<String> clientId = new ThreadLocal<>();

    /**
     * 设置当前应该访问的模板目录
     * */
    public static void setTemplateDir(String dir){
        templateDir.set(dir);
    }

    /**
     * 获取当前应该访问的模板路径前缀
     * */
    public static String getTemplateDir(){
        CacheService cacheService = SpringUtil.getBean(CacheService.class);
        String prefix = cacheService.get(CacheKey.TEMPLATE_DIR_KEY+clientId.get());
        if(prefix != null){
            return prefix;
        }
        return templateDir.get();
    }
    
    /**
     * 设置当前访问线程的客户端ID
     * */
    public static void setClientId(String id){
        clientId.set(id);
    }



}

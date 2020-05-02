package com.java2nb.novel.core.utils;

import com.java2nb.novel.core.cache.CacheKey;
import com.java2nb.novel.core.cache.CacheService;

/**
 * 模板操作工具类
 * @author Administrator
 */
public class TemplateUtil {

    /**
     * 存储当前线程访问的模板目录
     * */
    private static ThreadLocal<String> templateDir = new ThreadLocal<>();

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
        String prefix = cacheService.get(CacheKey.TEMPLATE_DIR_KEY);
        if(prefix != null){
            return prefix;
        }
        return templateDir.get();
    }


}

package com.java2nb.novel.core.listener;

import com.java2nb.novel.core.config.WebsiteProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * 启动监听器
 * @author xiongxiaoyang
 */
@WebListener
@Slf4j
@RequiredArgsConstructor
public class StarterListener implements ServletContextListener {

    private final WebsiteProperties websiteConfig;


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        sce.getServletContext().setAttribute("website",websiteConfig);

    }
}

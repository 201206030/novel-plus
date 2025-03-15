package com.java2nb.novel.core.listener;

import com.java2nb.novel.core.config.WebsiteProperties;
import com.java2nb.novel.entity.WebsiteInfo;
import com.java2nb.novel.mapper.WebsiteInfoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;

/**
 * 启动监听器
 *
 * @author xiongxiaoyang
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class StarterListener implements ServletContextInitializer {

    private final WebsiteProperties websiteProperties;
    private final WebsiteInfoMapper websiteInfoMapper;

    @Override
    public void onStartup(ServletContext servletContext) {
        servletContext.setAttribute("website", websiteInfoMapper.selectByPrimaryKey(1L).orElse(new WebsiteInfo() {{
            setName(websiteProperties.getName());
            setDomain(websiteProperties.getDomain());
            setKeyword(websiteProperties.getKeyword());
            setDescription(websiteProperties.getDescription());
            setQq(websiteProperties.getQq());
            setLogo("/images/logo.png");
            setLogoDark("/images/logo_white.png");
        }}));
    }
}

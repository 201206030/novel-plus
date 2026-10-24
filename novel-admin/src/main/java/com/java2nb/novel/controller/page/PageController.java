package com.java2nb.novel.controller.page;

import com.java2nb.novel.entity.WebsiteInfo;
import com.java2nb.novel.service.AdminAuthService;
import com.java2nb.novel.service.WebsiteService;
import com.java2nb.novel.vo.SysMenuVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 后台页面路由
 *
 * @author 11797
 */
@Slf4j
@RequiredArgsConstructor
@Controller
public class PageController {

    private final AdminAuthService adminAuthService;

    private final WebsiteService websiteService;

    /**
     * 侧栏菜单：所有后台页面都 th:replace 了 admin/menu 片段，在这里统一注入，
     * 保证左侧菜单与「角色管理-菜单授权」用的是同一份 sys_menu；
     * 菜单不再写死在模板里，新增/改名/调序只需改数据。
     */
    @ModelAttribute("sideMenus")
    public List<SysMenuVO> sideMenus() {
        return adminAuthService.currentSideMenus();
    }

    /**
     * 公共头部站点信息：服务端直接渲染站点名与前台地址，避免异步 fetch 导致
     * 每页先闪默认文案再跳变；域名未配置时 url 为 null，头部不渲染「前台站点」按钮；
     * 查询失败不阻断页面，降级为默认文案
     */
    @ModelAttribute("headerSite")
    public Map<String, String> headerSite() {
        Map<String, String> site = new HashMap<>(4);
        site.put("name", "小说精品屋后台管理");
        site.put("url", null);
        try {
            WebsiteInfo info = websiteService.getWebsiteInfo();
            if (info != null) {
                if (StringUtils.isNotBlank(info.getName())) {
                    site.put("name", info.getName().trim() + "后台管理");
                }
                site.put("url", normalizeDomain(info.getDomain()));
            }
        } catch (Exception e) {
            log.warn("查询站点信息失败，头部使用默认文案", e);
        }
        return site;
    }

    /**
     * 域名补齐协议前缀，与前台站点跳转规则一致；未配置返回 null
     */
    private String normalizeDomain(String domain) {
        if (StringUtils.isBlank(domain)) {
            return null;
        }
        String url = domain.trim();
        if (url.startsWith("//")) {
            return "http:" + url;
        }
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            return "http://" + url;
        }
        return url;
    }

    @RequestMapping("{url}.html")
    public String module(@PathVariable("url") String url) {
        return url;
    }

    @RequestMapping("{module}/{url}.html")
    public String module2(@PathVariable("module") String module, @PathVariable("url") String url) {
        return module + "/" + url;
    }

    /**
     * 首页
     */
    @RequestMapping(path = {"/", "/index", "/index.html"})
    public String index() {
        return "admin/index";
    }

}

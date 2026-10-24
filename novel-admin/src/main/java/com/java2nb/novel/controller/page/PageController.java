package com.java2nb.novel.controller.page;

import com.java2nb.novel.service.AdminAuthService;
import com.java2nb.novel.vo.SysMenuVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

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

    /**
     * 侧栏菜单：所有后台页面都 th:replace 了 admin/menu 片段，在这里统一注入，
     * 保证左侧菜单与「角色管理-菜单授权」用的是同一份 sys_menu；
     * 菜单不再写死在模板里，新增/改名/调序只需改数据。
     */
    @ModelAttribute("sideMenus")
    public List<SysMenuVO> sideMenus() {
        return adminAuthService.currentSideMenus();
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

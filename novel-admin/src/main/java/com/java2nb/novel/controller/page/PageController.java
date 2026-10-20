package com.java2nb.novel.controller.page;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 后台页面路由
 *
 * @author 11797
 */
@Slf4j
@RequiredArgsConstructor
@Controller
public class PageController {

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

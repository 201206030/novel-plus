package com.java2nb.novel.controller;

import com.java2nb.novel.service.StatService;
import io.github.xxyopen.model.resp.RestResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 后台统计Controller：首页趋势图
 *
 * @author Administrator
 */
@RestController
@RequestMapping("stat")
@RequiredArgsConstructor
public class StatController {

    private final StatService statService;

    @GetMapping("trend")
    public RestResult<Map<String, Object>> trend(@RequestParam(required = false, defaultValue = "30") String days) {
        return RestResult.ok(statService.trend(days));
    }

    @GetMapping("overview")
    public RestResult<Map<String, Object>> overview() {
        return RestResult.ok(statService.overview());
    }

    @GetMapping("extra")
    public RestResult<Map<String, Object>> extra(@RequestParam(required = false, defaultValue = "30") String days) {
        return RestResult.ok(statService.extra(days));
    }

}

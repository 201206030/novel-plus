package com.java2nb.novel.controller;

import com.java2nb.common.utils.DateUtils;
import com.java2nb.common.utils.PageBean;
import com.java2nb.common.utils.Query;
import com.java2nb.common.utils.R;
import com.java2nb.novel.domain.AuthorCodeDO;
import com.java2nb.novel.service.*;
import com.java2nb.test.service.OrderService;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 统计
 *
 * @author xiongxy
 * @email 1179705413@qq.com
 * @date 2020-12-01 03:40:12
 */

@Controller
@RequestMapping("/novel/stat")
public class StatController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private BookService bookService;

    @Autowired
    private PayService orderPayService;


    @ResponseBody
    @GetMapping("/countSta")
    public R countUser() {
        Map map = new HashMap<>(0);
        int userCount = userService.count(map);
        int authorCount = authorService.count(map);
        int bookCount = bookService.count(map);
        int orderCount = orderPayService.count(map);
        return R.ok().put("userCount",userCount)
                .put("authorCount",authorCount)
                .put("bookCount",bookCount)
                .put("orderCount",orderCount);
    }

    @ResponseBody
    @GetMapping("/tableSta")
    @SneakyThrows
    public R tableSta() {
        Date currentDate = new Date();
        List<String> dateList = DateUtils.getDateList(7,currentDate);
        Date minDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateList.get(0));
        Map<Object,Object> userTableSta = userService.tableSta(minDate);
        Map<Object,Object> bookTableSta = bookService.tableSta(minDate);
        Map<Object,Object> authorTableSta = authorService.tableSta(minDate);
        Map<Object,Object> orderTableSta = orderPayService.tableSta(minDate);
        return R.ok().put("dateList",dateList)
                .put("userTableSta",userTableSta)
                .put("bookTableSta",bookTableSta)
                .put("authorTableSta",authorTableSta)
                .put("orderTableSta",orderTableSta)
                ;
    }



}

package com.java2nb.novel.controller;


import com.java2nb.novel.core.bean.UserDetails;
import com.java2nb.novel.core.cache.CacheService;
import com.java2nb.novel.core.enums.ResponseStatus;
import com.java2nb.novel.core.utils.RandomValidateCodeUtil;
import com.java2nb.novel.entity.User;
import com.java2nb.novel.entity.UserBuyRecord;
import com.java2nb.novel.service.BookService;
import com.java2nb.novel.service.UserService;
import io.github.xxyopen.model.resp.RestResult;
import io.github.xxyopen.web.valid.AddGroup;
import io.github.xxyopen.web.valid.UpdateGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 11797
 */
@RestController
@RequestMapping("user")
@RequiredArgsConstructor
@Slf4j
public class UserController extends BaseController {


    private final CacheService cacheService;

    private final UserService userService;

    private final BookService bookService;

    /**
     * 登陆
     */
    @PostMapping("login")
    public RestResult<Map<String, Object>> login(User user) {

        //登陆
        UserDetails userDetails = userService.login(user);

        Map<String, Object> data = new HashMap<>(1);
        data.put("token", jwtTokenUtil.generateToken(userDetails));

        return RestResult.ok(data);


    }

    /**
     * 注册
     */
    @PostMapping("register")
    public RestResult<?> register(@Validated({AddGroup.class}) User user, @RequestParam(value = "velCode", defaultValue = "") String velCode) {


        //判断验证码是否正确
        if (!velCode.equals(cacheService.get(RandomValidateCodeUtil.RANDOM_CODE_KEY))) {
            return RestResult.fail(ResponseStatus.VEL_CODE_ERROR);
        }

        //注册
        UserDetails userDetails = userService.register(user);
        Map<String, Object> data = new HashMap<>(1);
        data.put("token", jwtTokenUtil.generateToken(userDetails));

        return RestResult.ok(data);


    }


    /**
     * 刷新token
     */
    @PostMapping("refreshToken")
    public RestResult<?> refreshToken(HttpServletRequest request) {
        String token = getToken(request);
        if (jwtTokenUtil.canRefresh(token)) {
            token = jwtTokenUtil.refreshToken(token);
            Map<String, Object> data = new HashMap<>(2);
            data.put("token", token);
            UserDetails userDetail = jwtTokenUtil.getUserDetailsFromToken(token);
            data.put("username", userDetail.getUsername());
            data.put("nickName", userDetail.getNickName());
            return RestResult.ok(data);

        } else {
            return RestResult.fail(ResponseStatus.NO_LOGIN);
        }

    }

    /**
     * 查询小说是否已加入书架
     */
    @GetMapping("queryIsInShelf")
    public RestResult<?> queryIsInShelf(Long bookId, HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(request);
        if (userDetails == null) {
            return RestResult.fail(ResponseStatus.NO_LOGIN);
        }
        return RestResult.ok(userService.queryIsInShelf(userDetails.getId(), bookId));
    }

    /**
     * 加入书架
     * */
    @PostMapping("addToBookShelf")
    public RestResult<Void> addToBookShelf(Long bookId,Long preContentId, HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(request);
        if (userDetails == null) {
            return RestResult.fail(ResponseStatus.NO_LOGIN);
        }
        userService.addToBookShelf(userDetails.getId(),bookId,preContentId);
        return RestResult.ok();
    }

    /**
     * 移出书架
     * */
    @DeleteMapping("removeFromBookShelf/{bookId}")
    public RestResult<?> removeFromBookShelf(@PathVariable("bookId") Long bookId, HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(request);
        if (userDetails == null) {
            return RestResult.fail(ResponseStatus.NO_LOGIN);
        }
        userService.removeFromBookShelf(userDetails.getId(),bookId);
        return RestResult.ok();
    }

    /**
     * 分页查询书架
     * */
    @GetMapping("listBookShelfByPage")
    public RestResult<?> listBookShelfByPage(@RequestParam(value = "curr", defaultValue = "1") int page, @RequestParam(value = "limit", defaultValue = "10") int pageSize,HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(request);
        if (userDetails == null) {
            return RestResult.fail(ResponseStatus.NO_LOGIN);
        }
        return RestResult.ok(userService.listBookShelfByPage(userDetails.getId(),page,pageSize));
    }

    /**
     * 分页查询阅读记录
     * */
    @GetMapping("listReadHistoryByPage")
    public RestResult<?> listReadHistoryByPage(@RequestParam(value = "curr", defaultValue = "1") int page, @RequestParam(value = "limit", defaultValue = "10") int pageSize,HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(request);
        if (userDetails == null) {
            return RestResult.fail(ResponseStatus.NO_LOGIN);
        }
        return RestResult.ok(userService.listReadHistoryByPage(userDetails.getId(),page,pageSize));
    }

    /**
     * 添加阅读记录
     * */
    @PostMapping("addReadHistory")
    public RestResult<?> addReadHistory(Long bookId,Long preContentId, HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(request);
        if (userDetails == null) {
            return RestResult.fail(ResponseStatus.NO_LOGIN);
        }
        userService.addReadHistory(userDetails.getId(),bookId,preContentId);
        return RestResult.ok();
    }

    /**
     * 添加反馈
     * */
    @PostMapping("addFeedBack")
    public RestResult<?> addFeedBack(String content, HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(request);
        if (userDetails == null) {
            return RestResult.fail(ResponseStatus.NO_LOGIN);
        }
        userService.addFeedBack(userDetails.getId(),content);
        return RestResult.ok();
    }

    /**
     * 分页查询我的反馈列表
     * */
    @GetMapping("listUserFeedBackByPage")
    public RestResult<?> listUserFeedBackByPage(@RequestParam(value = "curr", defaultValue = "1") int page, @RequestParam(value = "limit", defaultValue = "5") int pageSize, HttpServletRequest request){
        UserDetails userDetails = getUserDetails(request);
        if (userDetails == null) {
            return RestResult.fail(ResponseStatus.NO_LOGIN);
        }
        return RestResult.ok(userService.listUserFeedBackByPage(userDetails.getId(),page,pageSize));
    }

    /**
     * 查询个人信息
     * */
    @GetMapping("userInfo")
    public RestResult<?> userInfo(HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(request);
        if (userDetails == null) {
            return RestResult.fail(ResponseStatus.NO_LOGIN);
        }
        return RestResult.ok(userService.userInfo(userDetails.getId()));
    }

    /**
     * 更新个人信息
     * */
    @PostMapping("updateUserInfo")
    public RestResult<?> updateUserInfo(@Validated({UpdateGroup.class}) User user, HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(request);
        if (userDetails == null) {
            return RestResult.fail(ResponseStatus.NO_LOGIN);
        }
        userService.updateUserInfo(userDetails.getId(),user);
        if(user.getNickName() != null){
            userDetails.setNickName(user.getNickName());
            Map<String, Object> data = new HashMap<>(1);
            data.put("token", jwtTokenUtil.generateToken(userDetails));
            return RestResult.ok(data);
        }
        return RestResult.ok();
    }


    /**
     * 更新密码
     * */
    @PostMapping("updatePassword")
    public RestResult<?> updatePassword(String oldPassword,String newPassword1,String newPassword2,HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(request);
        if (userDetails == null) {
            return RestResult.fail(ResponseStatus.NO_LOGIN);
        }
        if(!(StringUtils.isNotBlank(newPassword1) && newPassword1.equals(newPassword2))){
            RestResult.fail(ResponseStatus.TWO_PASSWORD_DIFF);
        }
        userService.updatePassword(userDetails.getId(),oldPassword,newPassword1);
        return RestResult.ok();
    }

    /**
     * 分页查询用户书评
     * */
    @GetMapping("listCommentByPage")
    public RestResult<?> listCommentByPage(@RequestParam(value = "curr", defaultValue = "1") int page, @RequestParam(value = "limit", defaultValue = "5") int pageSize,HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(request);
        if (userDetails == null) {
            return RestResult.fail(ResponseStatus.NO_LOGIN);
        }
        return RestResult.ok(bookService.listCommentByPage(userDetails.getId(),null,page,pageSize));
    }


    /**
     * 购买小说章节
     * */
    @PostMapping("buyBookIndex")
    public RestResult<?> buyBookIndex(UserBuyRecord buyRecord, HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(request);
        if (userDetails == null) {
            return RestResult.fail(ResponseStatus.NO_LOGIN);
        }
        buyRecord.setBuyAmount(bookService.queryBookIndex(buyRecord.getBookIndexId()).getBookPrice());
        userService.buyBookIndex(userDetails.getId(),buyRecord);
        return RestResult.ok();
    }





}

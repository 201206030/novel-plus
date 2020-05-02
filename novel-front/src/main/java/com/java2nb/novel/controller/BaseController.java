package com.java2nb.novel.controller;

import com.java2nb.novel.core.bean.UserDetails;
import com.java2nb.novel.core.utils.CookieUtil;
import com.java2nb.novel.core.utils.JwtTokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author 11797
 */
public class BaseController {

    protected JwtTokenUtil jwtTokenUtil;


    protected String getToken(HttpServletRequest request){
        String token = CookieUtil.getCookie(request,"Authorization");
        if(token != null){
            return token;
        }
        return request.getHeader("Authorization");
    }

    protected UserDetails getUserDetails(HttpServletRequest request) {
        String token = getToken(request);
        if(StringUtils.isBlank(token)){
            return null;
        }else{
            return jwtTokenUtil.getUserDetailsFromToken(token);
        }
    }

    @Autowired
    public void setJwtTokenUtil(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }
}

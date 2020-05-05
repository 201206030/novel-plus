package com.java2nb.common.controller;

import com.java2nb.system.domain.UserToken;
import org.springframework.stereotype.Controller;
import com.java2nb.common.utils.ShiroUtils;
import com.java2nb.system.domain.UserDO;

@Controller
public class BaseController {
	public UserDO getUser() {
		return ShiroUtils.getUser();
	}

	public Long getUserId() {
		return getUser().getUserId();
	}

	public String getUsername() {
		return getUser().getUsername();
	}
}
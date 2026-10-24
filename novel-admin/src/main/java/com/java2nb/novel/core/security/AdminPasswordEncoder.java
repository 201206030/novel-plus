package com.java2nb.novel.core.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

/**
 * 后台密码编码器：sys_user.password 为 varchar(50)，存不下 60 位的 bcrypt 串，
 * 故统一以 MD5(hex32) 存储；比对时同时兼容 bcrypt 串（以 {@code $2} 开头），
 * 便于历史数据或手工写入的 bcrypt 口令平滑过渡。
 *
 * @author Administrator
 */
public class AdminPasswordEncoder implements PasswordEncoder {

    private final BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

    @Override
    public String encode(CharSequence rawPassword) {
        return md5(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null || encodedPassword.isEmpty()) {
            return false;
        }
        if (encodedPassword.startsWith("$2")) {
            return bcrypt.matches(rawPassword, encodedPassword);
        }
        return md5(rawPassword).equalsIgnoreCase(encodedPassword);
    }

    private String md5(CharSequence rawPassword) {
        return DigestUtils.md5DigestAsHex(rawPassword.toString().getBytes(StandardCharsets.UTF_8));
    }

}

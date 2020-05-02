package com.java2nb.novel.form;

import lombok.Data;

import javax.annotation.Generated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class UserForm {
    @NotBlank(message="手机号不能为空！")
    @Pattern(regexp="^1[3|4|5|6|7|8|9][0-9]{9}$",message="手机号格式不正确！")
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String username;

    @NotBlank(message="密码不能为空！")
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String password;

}

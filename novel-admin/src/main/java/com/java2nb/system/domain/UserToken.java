package com.java2nb.system.domain;

import java.io.Serializable;

/**
 * @author xiongxy
 * @version V1.0
 */
public class UserToken implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long userId;
    private String username;
    private String name;
    private String password;
    private Long deptId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }
}

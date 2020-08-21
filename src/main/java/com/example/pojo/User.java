package com.example.pojo;

import java.io.Serializable;

public class User implements Serializable {
    private Integer id;
    private String username;
    private String password;
    private String email;
    private String status;
    private String code;

    /**
     * 无参构造
     */
    public User() {
    }

    /**
     * 含参构造
     * @param username 用户名
     * @param password 用户密码
     * @param email 用户邮箱
     * @param status 用户激活状态
     * @param code 用户激活码
     */
    public User(String username, String password, String email, String status, String code) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.status = status;
        this.code = code;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

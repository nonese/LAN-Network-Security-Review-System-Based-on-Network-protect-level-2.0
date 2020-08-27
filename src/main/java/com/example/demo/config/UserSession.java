package com.example.demo.config;

public class UserSession {
    private String uuid;

    private String username;


    private Integer role;


    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getUserId() {
        return uuid;
    }

    public void setUserId(String userId) {
        this.uuid = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}

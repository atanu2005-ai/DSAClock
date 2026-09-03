package com.dsaclock.dto;

public class LoginResponse {

    private String token; //for jwt token

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

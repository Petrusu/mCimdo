package com.example.mcimdo;

public class LoginResponse {
    private String token;
    private int statusCode;

    public LoginResponse() {
        token = "";
        statusCode = -1;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}

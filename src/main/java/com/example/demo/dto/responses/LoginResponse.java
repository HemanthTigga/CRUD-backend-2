package com.example.demo.dto.responses;

public class LoginResponse {
    private String token;
    private String loggedIn;

    public LoginResponse(String token, String loggedIn) {
        this.token = token;
        this.loggedIn = loggedIn;
    }

    // Getters and setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(String loggedIn) {
        this.loggedIn = loggedIn;
    }
}

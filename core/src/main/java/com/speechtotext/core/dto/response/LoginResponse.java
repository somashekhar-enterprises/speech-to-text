package com.speechtotext.core.dto.response;

public class LoginResponse {

    private String username;

    private String message;

    private boolean isSuccess;

    public LoginResponse(String username, String loginFailed, boolean b) {
        this.username = username;
        this.message = loginFailed;
        this.isSuccess = b;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }
}

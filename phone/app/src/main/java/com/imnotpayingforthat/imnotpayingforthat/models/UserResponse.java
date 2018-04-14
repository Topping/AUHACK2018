package com.imnotpayingforthat.imnotpayingforthat.models;

public class UserResponse {
    private boolean success;
    private User user;

    public UserResponse(boolean success, User user) {
        this.success = success;
        this.user = user;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

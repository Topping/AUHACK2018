package com.imnotpayingforthat.imnotpayingforthat.callbacks;

import com.imnotpayingforthat.imnotpayingforthat.models.UserResponse;

@FunctionalInterface
public interface UserResponseHandler {
    void setUserResponse(UserResponse user);
}

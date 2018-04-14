package com.imnotpayingforthat.imnotpayingforthat.services.register;

import com.imnotpayingforthat.imnotpayingforthat.models.RegisterUserResponse;

@FunctionalInterface
public interface RegisterResponseHandler {
    void setUserResponse(RegisterUserResponse response);
}

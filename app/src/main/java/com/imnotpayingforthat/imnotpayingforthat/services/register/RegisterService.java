package com.imnotpayingforthat.imnotpayingforthat.services.register;

public interface RegisterService {
    void registerWithEmail(String firstName, String lastName, String email, String password, String confirmPassword, RegisterResponseHandler callback);
}

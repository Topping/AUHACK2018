package com.imnotpayingforthat.imnotpayingforthat.viewModels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.imnotpayingforthat.imnotpayingforthat.models.RegisterUserResponse;
import com.imnotpayingforthat.imnotpayingforthat.services.register.RegisterService;
import com.imnotpayingforthat.imnotpayingforthat.services.register.RegisterServiceImpl;


public class RegisterViewModel extends ViewModel {
    private MutableLiveData<RegisterUserResponse> userResponse;
    private RegisterService registerService;

    public RegisterViewModel() {
        userResponse = new MutableLiveData<>();
        registerService = new RegisterServiceImpl();
    }

    public MutableLiveData<RegisterUserResponse> getUserResponse() {
        return userResponse;
    }

    public void registerUser(String firstName, String lastName,String email, String password, String confirmPassword) {
        registerService.registerWithEmail(firstName, lastName, email, password, confirmPassword, response -> userResponse.setValue(response));
    }
}

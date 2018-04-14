package com.imnotpayingforthat.imnotpayingforthat.models;

import java.util.ArrayList;
import java.util.List;

public class RegisterUserResponse {
    private Boolean success;
    private List<String> errors;
    
    public RegisterUserResponse() {
        errors = new ArrayList<>();
        success = false;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<String> getErrors() {
        return errors;
    }
}

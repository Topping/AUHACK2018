package com.imnotpayingforthat.imnotpayingforthat.services.register;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.imnotpayingforthat.imnotpayingforthat.models.RegisterUserResponse;
import com.imnotpayingforthat.imnotpayingforthat.models.User;

import java.util.ArrayList;
import java.util.List;

import static com.imnotpayingforthat.imnotpayingforthat.util.ErrorCodes.*;
import static com.imnotpayingforthat.imnotpayingforthat.util.Globals.dbCollection_user;


public class RegisterServiceImpl implements RegisterService {

    // TODO: 24-03-2018 Anders - Validation of email still very simple. Validating of password still very simple

    @Override
    public void registerWithEmail(String firstName, String lastName, String email, String password, String confirmPassword, RegisterResponseHandler callback) {
        RegisterUserResponse registerUserResponse = new RegisterUserResponse();

        List<String> errors = validateUserData(firstName, lastName, email, password, confirmPassword);

        if (errors.isEmpty()) {
            // TODO: 11/04/2018 Lidt det samme med user repository til firestore her
            // TODO: 11/04/2018 Evt. functional interface halløj så man kan give sit eget callback med til fx. onsuccess/onfailure
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener(task -> {
                        User user = new User(firstName, lastName);
                        FirebaseFirestore instance = FirebaseFirestore.getInstance();
                        instance.collection(dbCollection_user)
                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .set(user)
                                .addOnCompleteListener(taskComplete -> {
                                    if (taskComplete.isSuccessful()) {
                                        registerUserResponse.setSuccess(true);
                                    }
                                    callback.setUserResponse(registerUserResponse);
                                });
                    })
                    .addOnFailureListener( e -> {
                           Log.d(this.getClass().getSimpleName(), e.getMessage());
                    });
        } else {
            registerUserResponse.setErrors(errors);
            callback.setUserResponse(registerUserResponse);
        }
    }

    private List<String> validateUserData(String firstName, String lastName, String email, String password, String confirmPassword) {
        List<String> errors = new ArrayList<>();

        if (firstName.isEmpty()) {
            errors.add(REGISTRATION_FIRSTNAME_MUST_BE_FILLED);
        }

        if (lastName.isEmpty()) {
            errors.add(REGISTRATION_LASTNAME_MUST_BE_FILLED);
        }

        if (email.isEmpty()) {
            errors.add(REGISTRATION_EMAIL_MUST_BE_FILLED);
        }

        if (password.isEmpty()) {
            errors.add(REGISTRATION_PASSWORD_MUST_BE_FILLED);
        }

        if (confirmPassword.isEmpty()) {
            errors.add(REGISTRATION_CONFIRMPASSWORD_MUST_BE_FILLED);
        }

        if (!password.equals(confirmPassword)) {
            errors.add(REGISTRATION_PASSWORD_NOT_EQUAL);
        }
        return errors;
    }
}


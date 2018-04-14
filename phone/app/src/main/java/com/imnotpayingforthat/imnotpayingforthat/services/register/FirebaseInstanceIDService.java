package com.imnotpayingforthat.imnotpayingforthat.services.register;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.imnotpayingforthat.imnotpayingforthat.repositories.UserRepository;

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    private UserRepository userRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        userRepository = new UserRepository();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String refreshToken = FirebaseInstanceId.getInstance().getToken();
            userRepository.updateFcmToken(currentUser.getUid(), refreshToken);
        }
    }

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String refreshToken = FirebaseInstanceId.getInstance().getToken();
            userRepository.updateFcmToken(currentUser.getUid(), refreshToken);
        }
    }
}

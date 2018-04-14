package com.imnotpayingforthat.imnotpayingforthat.repositories;

import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.imnotpayingforthat.imnotpayingforthat.callbacks.UserResponseHandler;
import com.imnotpayingforthat.imnotpayingforthat.models.UserResponse;
import com.imnotpayingforthat.imnotpayingforthat.models.User;
import com.imnotpayingforthat.imnotpayingforthat.util.Globals;

public class UserRepository {
    private final FirebaseFirestore db;

    public UserRepository(FirebaseFirestore db) {
        this.db = db;
    }

    public UserRepository() {
        this.db = FirebaseFirestore.getInstance();
    }

    public void createUser(){

    }

    public void getUser(String userUid, OnSuccessListener<DocumentSnapshot> successListener, OnFailureListener failureListener) {
        db.collection(Globals.dbCollection_user)
                .document(userUid)
                .get()
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }

    public void updateUser(String userUid, User user, OnSuccessListener successListener, OnFailureListener failureListener) {
        db.collection(Globals.dbCollection_user)
                .document(userUid)
                .set(user)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }

    public void updateFcmToken(String userUid, String fcmToken){
        getUser(userUid, l -> {
            User u = l.toObject(User.class);
            u.setFcm_Token(fcmToken);
            db.collection(Globals.dbCollection_user)
                    .document(userUid)
                    .set(u);
        }, f -> {
            Log.w("UserRepository", "Update token failed");
        });
    }
}

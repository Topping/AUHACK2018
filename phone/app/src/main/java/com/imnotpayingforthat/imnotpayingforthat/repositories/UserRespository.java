package com.imnotpayingforthat.imnotpayingforthat.repositories;

import com.google.firebase.firestore.FirebaseFirestore;

public class UserRespository {
    private final FirebaseFirestore db;

    public UserRespository(FirebaseFirestore db) {
        this.db = db;
    }

    public void createUser(){

    }

}

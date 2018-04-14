package com.domoroapp.domoro.repositories;

import android.util.Log;

import com.domoroapp.domoro.models.Team;
import com.google.firebase.firestore.FirebaseFirestore;

public class TeamRepository {
    private final String TAG = this.getClass().getSimpleName();
    private final FirebaseFirestore db;

    public TeamRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public TeamRepository(FirebaseFirestore db) {
        this.db = db;
    }

    public void createTeam(Team team) {
        db.collection("teams")
                .add(team)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Added team with ID " + documentReference.getId());
                })
                .addOnFailureListener(e -> Log.w(TAG, "Failed adding document", e));
    }

    public void joinTeam(Team team) {

    }
}

package com.imnotpayingforthat.imnotpayingforthat.repositories;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.imnotpayingforthat.imnotpayingforthat.callbacks.TeamResponseHandler;
import com.imnotpayingforthat.imnotpayingforthat.models.Team;
import com.google.firebase.firestore.FirebaseFirestore;
import com.imnotpayingforthat.imnotpayingforthat.models.TeamResponse;

public class TeamRepository {
    private final String TAG = this.getClass().getSimpleName();
    private final FirebaseFirestore db;

    public TeamRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public TeamRepository(FirebaseFirestore db) {
        this.db = db;
    }

    // TODO: 14-04-2018 fix callbacks 
    public void createTeam(Team team, OnSuccessListener<DocumentReference> successListener, OnFailureListener failureListener) {
        String ownerUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        team.setOwnerUid(ownerUID);
        db.collection("teams")
                .add(team)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }

    public void deleteTeam(String teamId, OnSuccessListener successListener, OnFailureListener failureListener) {
        db.collection("teams")
                .document(teamId)
                .delete()
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }

    public void getTeam(String teamId, @NonNull OnSuccessListener<DocumentSnapshot> successListener) {
        db.document("/teams/" + teamId)
                .get()
                .addOnSuccessListener(successListener);
    }

    public void addMoneyToTeam(String teamid, double amount) {

    }

    public void joinTeam(Team team) {

    }
}

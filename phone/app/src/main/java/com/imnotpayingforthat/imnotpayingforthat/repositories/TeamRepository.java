package com.imnotpayingforthat.imnotpayingforthat.repositories;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.imnotpayingforthat.imnotpayingforthat.callbacks.TeamRepositoryCallback;
import com.imnotpayingforthat.imnotpayingforthat.models.Team;
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

    // TODO: 14-04-2018 fix callbacks 
    public void createTeam(Team team, TeamRepositoryCallback successCallback, TeamRepositoryCallback failureCallback) {
        String ownerUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        team.setOwnerUid(ownerUID);
        db.collection("teams")
                .add(team)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Added team with ID " + documentReference.getId());
                    successCallback.createTeamResponse();
                })
                .addOnFailureListener(e -> {
                            Log.w(TAG, "Failed adding document", e);
                            failureCallback.createTeamResponse();
                        }
                );
    }

    public void joinTeam(Team team) {

    }
}

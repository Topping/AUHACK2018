package com.domoroapp.domoro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.domoroapp.domoro.models.Team;
import com.domoroapp.domoro.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestQueryActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText teamId, subcollection, ownerid, memberid;
    private Button myTeams, user, teams;
    private String currentUserUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_query);
        teamId = findViewById(R.id.test_teamId);
        subcollection = findViewById(R.id.test_subcollection);
        ownerid = findViewById(R.id.test_ownerid);
        memberid = findViewById(R.id.test_memberid);
        findViewById(R.id.test_myteams_button).setOnClickListener(this);
        findViewById(R.id.test_user_button).setOnClickListener(this);
        findViewById(R.id.test_teams_button).setOnClickListener(this);
        findViewById(R.id.test_specificteam_button).setOnClickListener(this);
        findViewById(R.id.test_specificUser_button).setOnClickListener(this);
        currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.test_myteams_button:
                FirebaseFirestore.getInstance()
                        .collection("teams")
                        .whereEqualTo("ownerUid", currentUserUid)
                        .get().addOnSuccessListener(l -> {
                            List<Team> x = l.toObjects(Team.class);
                            Log.d("TEST", l.toString());
                        })
                        .addOnFailureListener(l -> {
                            Log.d("TEST", l.toString());
                        });
                break;
            case R.id.test_user_button:
                Team t = new Team();
                List<String> m = new ArrayList<>();
                m.add("77v2jYWHaGgIi2WFh7paO7vcVYl1");
                t.setOwnerUid(FirebaseAuth.getInstance().getCurrentUser().getUid());
                t.setMembers(m);
                t.setTeamName("Team 1");
                t.setTeamDescription("Team 1 Description");
                FirebaseFirestore.getInstance()
                        .collection("teams")
                        .document()
                        .set(t)
                        .addOnSuccessListener(l -> {
                            //Log.d("TEST", l.toString());
                        })
                        .addOnFailureListener(l -> {
                            Log.d("TEST", l.toString());
                        });
                break;
            case R.id.test_teams_button:
                String o = ownerid.getText().toString();
                FirebaseFirestore.getInstance()
                        .collection("teams")
                        .whereEqualTo("ownerUid", ownerid.getText().toString())
                        .get().addOnSuccessListener(l -> {
                            List<Team> x = l.toObjects(Team.class);
                            Log.d("TEST", l.toString());
                        })
                        .addOnFailureListener(l -> {
                            Log.d("TEST", l.toString());
                        });
                break;

            case R.id.test_specificteam_button:
                DocumentReference docref = FirebaseFirestore.getInstance()
                        .collection("teams")
                        .document(teamId.getText().toString());
                docref.get()
                        .addOnSuccessListener(l -> {
                            Team x = l.toObject(Team.class);
                            Log.d("TEST", l.toString());
                        })
                        .addOnFailureListener(l -> {
                            Log.d("TEST", l.toString());
                        });
                break;
            case R.id.test_specificUser_button:
                DocumentReference docref1 = FirebaseFirestore.getInstance()
                        .collection("users")
                        .document(memberid.getText().toString());
                docref1.get()
                        .addOnSuccessListener(l -> {
                            User x = l.toObject(User.class);
                            Log.d("TEST", l.toString());
                        })
                        .addOnFailureListener(l -> {
                            Log.d("TEST", l.toString());
                        });
                break;
        }
    }
}

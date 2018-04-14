package com.imnotpayingforthat.imnotpayingforthat.repositories;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.imnotpayingforthat.imnotpayingforthat.callbacks.TeamResponseHandler;
import com.imnotpayingforthat.imnotpayingforthat.models.Team;
import com.imnotpayingforthat.imnotpayingforthat.models.User;

import java.util.HashMap;

public class TeamCreationFacade {
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    public TeamCreationFacade() {
        teamRepository = new TeamRepository();
        userRepository = new UserRepository();
    }

    public void createTeam(Team team, TeamResponseHandler responseHandler) {
        String currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        team.setOwnerUid(currentUserUid);

        teamRepository.createTeam(team, t -> {
            String teamId = t.getId();
            userRepository.getUser(currentUserUid, u -> {
                User user = u.toObject(User.class);
                if(user.getTeams() == null) {
                    user.setTeams(new HashMap<>());
                }
                user.addTeam(teamId);
                userRepository.updateUser(currentUserUid, user, s -> {
                    responseHandler.createTeamResponse(true);
                }, f -> {
                    Log.w("TEST", f);
                    teamRepository.deleteTeam(t.getId(), ts -> {
                        //Deleted team. Yay
                        responseHandler.createTeamResponse(false);
                    }, tf -> {
                        responseHandler.createTeamResponse(false);
                    });
                });
            }, f -> {
                teamRepository.deleteTeam(t.getId(), ts -> {
                    //Deleted team. Yay
                    responseHandler.createTeamResponse(false);
                }, tf -> {
                    responseHandler.createTeamResponse(false);
                });
            });
        }, f -> {
                responseHandler.createTeamResponse(false);
        });
    }
}

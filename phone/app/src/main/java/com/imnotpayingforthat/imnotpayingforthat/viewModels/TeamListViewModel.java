package com.imnotpayingforthat.imnotpayingforthat.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.imnotpayingforthat.imnotpayingforthat.models.Team;

import java.util.ArrayList;
import java.util.List;

public class TeamListViewModel extends AndroidViewModel {
    private MutableLiveData<List<Team>> teams;

    public TeamListViewModel(@NonNull Application application) {
        super(application);
        List<Team> t = new ArrayList<>();
        teams = new MutableLiveData<>();
        teams.setValue(t);
    }

    public LiveData<List<Team>> getTeams() {
        if(teams == null) {
            teams = new MutableLiveData<>();
        }
        return teams;
    }

    public void addTeam(@NonNull Team team) {
        if(teams == null) {
            teams = new MutableLiveData<>();
        }
        teams.getValue().add(team);
    }
}

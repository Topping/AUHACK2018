package com.imnotpayingforthat.imnotpayingforthat.models;

import android.net.Uri;

import java.util.HashMap;
import java.util.List;

public class Team {
    private String teamName;
    private String teamDescription;
    private String ownerUid;
    private List<String> members;

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public Team() {}

    public Team(String teamName, String teamDescription, String ownerUid, Uri teamIconUri) {
        this.teamName = teamName;
        this.teamDescription = teamDescription;
        this.ownerUid = ownerUid;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamDescription() {
        return teamDescription;
    }

    public void setTeamDescription(String teamDescription) {
        this.teamDescription = teamDescription;
    }

    public String getOwnerUid() {
        return ownerUid;
    }

    public void setOwnerUid(String uid) {
        ownerUid = uid;
    }
}

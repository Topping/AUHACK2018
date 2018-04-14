package com.imnotpayingforthat.imnotpayingforthat.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
    private String firstName;
    private String lastName;
    private String profile_Image;
    private String fcm_Token;
    private Map<String, Boolean> teams;


    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        teams = new HashMap<>();
    }

    public User() {
    }


    public String getFcm_Token() {
        return fcm_Token;
    }

    public void setFcm_Token(String FCM_Token) {
        this.fcm_Token = FCM_Token;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfile_Image() {
        return profile_Image;
    }

    public void setProfile_Image(String profile_Image) {
        this.profile_Image = profile_Image;
    }

    public Map<String, Boolean> getTeams() {
        return teams;
    }

    public void setTeams(Map<String, Boolean> teams) {
        this.teams = teams;
    }

    public void addTeam(String teamId) {
        teams.put(teamId, true);
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}

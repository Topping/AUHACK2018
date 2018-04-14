package com.imnotpayingforthat.imnotpayingforthat.models;

public class TeamResponse {
    private boolean success;
    private String teamId;

    public TeamResponse(boolean success, String teamId) {
        this.success = success;
        this.teamId = teamId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }
}

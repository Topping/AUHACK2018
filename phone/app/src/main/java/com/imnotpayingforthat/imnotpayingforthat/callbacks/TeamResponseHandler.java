package com.imnotpayingforthat.imnotpayingforthat.callbacks;

import com.imnotpayingforthat.imnotpayingforthat.models.TeamResponse;

@FunctionalInterface
public interface TeamResponseHandler {
    void createTeamResponse(boolean success);
}

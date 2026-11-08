package com.ffa.tournaments.team.application.exception;

public class TeamApplicationNotFoundException extends RuntimeException {

    public TeamApplicationNotFoundException() {
        super("Team application not found");
    }
}

package com.ffa.tournaments.team.application.exception;

public class TeamNotFoundException extends RuntimeException {

    public TeamNotFoundException() {
        super("Team not found");
    }
}

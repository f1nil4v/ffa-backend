package com.ffa.tournaments.team.application.exception;

public class TeamMemberNotFoundException extends RuntimeException {

    public TeamMemberNotFoundException() {
        super("Team member not found");
    }
}

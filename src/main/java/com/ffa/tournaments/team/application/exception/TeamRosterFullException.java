package com.ffa.tournaments.team.application.exception;

public class TeamRosterFullException extends RuntimeException {

    public TeamRosterFullException() {
        super("Team roster is full");
    }
}

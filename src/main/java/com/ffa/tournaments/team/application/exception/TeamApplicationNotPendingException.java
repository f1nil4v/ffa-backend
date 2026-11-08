package com.ffa.tournaments.team.application.exception;

public class TeamApplicationNotPendingException extends RuntimeException {

    public TeamApplicationNotPendingException() {
        super("Team application is not pending");
    }
}

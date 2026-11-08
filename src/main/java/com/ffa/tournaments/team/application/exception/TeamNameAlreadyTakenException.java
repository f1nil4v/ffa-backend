package com.ffa.tournaments.team.application.exception;

public class TeamNameAlreadyTakenException extends RuntimeException {

    public TeamNameAlreadyTakenException() {
        super("Team name is already taken");
    }
}

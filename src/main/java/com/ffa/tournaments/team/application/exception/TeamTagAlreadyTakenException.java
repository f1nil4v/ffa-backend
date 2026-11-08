package com.ffa.tournaments.team.application.exception;

public class TeamTagAlreadyTakenException extends RuntimeException {

    public TeamTagAlreadyTakenException() {
        super("Team tag is already taken");
    }
}

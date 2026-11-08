package com.ffa.tournaments.team.application.exception;

public class CannotKickCaptainException extends RuntimeException {

    public CannotKickCaptainException() {
        super("Captain cannot be kicked from the team");
    }
}

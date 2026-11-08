package com.ffa.tournaments.team.application.exception;

public class NotTeamCaptainException extends RuntimeException {

    public NotTeamCaptainException() {
        super("Only the team captain can perform this action");
    }
}

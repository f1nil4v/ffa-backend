package com.ffa.tournaments.tournament.application.exception;

public class TournamentRegistrationNotPendingException extends RuntimeException {

    public TournamentRegistrationNotPendingException() {
        super("Tournament registration is not pending");
    }
}

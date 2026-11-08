package com.ffa.tournaments.tournament.application.exception;

public class TournamentRegistrationNotActiveException extends RuntimeException {

    public TournamentRegistrationNotActiveException() {
        super("Tournament registration is not active");
    }
}

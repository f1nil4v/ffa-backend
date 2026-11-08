package com.ffa.tournaments.tournament.application.exception;

public class TournamentNotOpenForRegistrationException extends RuntimeException {

    public TournamentNotOpenForRegistrationException() {
        super("Tournament registration is not open");
    }
}

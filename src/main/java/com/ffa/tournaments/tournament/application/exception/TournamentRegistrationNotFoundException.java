package com.ffa.tournaments.tournament.application.exception;

public class TournamentRegistrationNotFoundException extends RuntimeException {

    public TournamentRegistrationNotFoundException() {
        super("Tournament registration not found");
    }
}

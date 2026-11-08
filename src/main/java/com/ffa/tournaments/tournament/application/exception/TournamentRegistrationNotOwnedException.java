package com.ffa.tournaments.tournament.application.exception;

public class TournamentRegistrationNotOwnedException extends RuntimeException {

    public TournamentRegistrationNotOwnedException() {
        super("Tournament registration does not belong to the current team");
    }
}

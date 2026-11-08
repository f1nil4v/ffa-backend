package com.ffa.tournaments.tournament.application.exception;

public class TournamentRegistrationAlreadyExistsException extends RuntimeException {

    public TournamentRegistrationAlreadyExistsException() {
        super("An active tournament registration for this team already exists");
    }
}

package com.ffa.tournaments.tournament.application.exception;

public class InvalidTournamentStatusException extends RuntimeException {

    public InvalidTournamentStatusException(String message) {
        super(message);
    }
}

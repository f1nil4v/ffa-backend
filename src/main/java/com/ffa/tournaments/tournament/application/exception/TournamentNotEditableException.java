package com.ffa.tournaments.tournament.application.exception;

public class TournamentNotEditableException extends RuntimeException {

    public TournamentNotEditableException() {
        super("Tournament can only be modified in DRAFT status");
    }
}

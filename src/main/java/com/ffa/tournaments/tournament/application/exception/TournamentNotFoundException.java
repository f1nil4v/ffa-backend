package com.ffa.tournaments.tournament.application.exception;

public class TournamentNotFoundException extends RuntimeException {

    public TournamentNotFoundException() {
        super("Tournament not found");
    }
}

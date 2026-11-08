package com.ffa.tournaments.tournament.application.exception;

public class TournamentCapacityExceededException extends RuntimeException {

    public TournamentCapacityExceededException() {
        super("Tournament team limit has been reached");
    }
}

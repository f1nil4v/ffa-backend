package com.ffa.tournaments.team.application.exception;

public class TeamApplicationNotOwnedException extends RuntimeException {

    public TeamApplicationNotOwnedException() {
        super("Team application does not belong to the current player");
    }
}

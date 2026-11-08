package com.ffa.tournaments.team.application.exception;

public class PlayerAlreadyInTeamException extends RuntimeException {

    public PlayerAlreadyInTeamException() {
        super("Player is already in a team");
    }
}

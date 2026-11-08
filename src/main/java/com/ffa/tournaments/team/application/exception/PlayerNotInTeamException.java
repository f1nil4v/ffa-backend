package com.ffa.tournaments.team.application.exception;

public class PlayerNotInTeamException extends RuntimeException {

    public PlayerNotInTeamException() {
        super("Player is not in a team");
    }
}

package com.ffa.tournaments.player.application.exception;

public class PlayerProfileNotFoundException extends RuntimeException {

    public PlayerProfileNotFoundException() {
        super("Player profile not found");
    }
}

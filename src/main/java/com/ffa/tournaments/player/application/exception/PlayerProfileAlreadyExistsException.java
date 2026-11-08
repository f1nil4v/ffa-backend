package com.ffa.tournaments.player.application.exception;

public class PlayerProfileAlreadyExistsException extends RuntimeException {

    public PlayerProfileAlreadyExistsException() {
        super("Player profile already exists");
    }
}
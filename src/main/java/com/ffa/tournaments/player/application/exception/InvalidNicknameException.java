package com.ffa.tournaments.player.application.exception;

public class InvalidNicknameException extends RuntimeException {

    public InvalidNicknameException() {
        super("Nickname must contain 3-32 characters: Latin letters, numbers, _ or -");
    }
}
package com.ffa.tournaments.player.application.exception;

public class NicknameAlreadyTakenException extends RuntimeException {

    public NicknameAlreadyTakenException() {
        super("Nickname is already taken");
    }
}
package com.ffa.tournaments.player.infrastructure.persistence.exception;

public class DuplicateNicknameException extends RuntimeException {

    public DuplicateNicknameException(Throwable cause) {
        super("Nickname is already taken", cause);
    }
}
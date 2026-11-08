package com.ffa.tournaments.player.application.exception;

public class PendingNicknameChangeRequestExistsException extends RuntimeException {

    public PendingNicknameChangeRequestExistsException() {
        super("A pending nickname change request already exists");
    }
}

package com.ffa.tournaments.player.application.exception;

public class NicknameChangeRequestNotPendingException extends RuntimeException {

    public NicknameChangeRequestNotPendingException() {
        super("Nickname change request is not pending");
    }
}

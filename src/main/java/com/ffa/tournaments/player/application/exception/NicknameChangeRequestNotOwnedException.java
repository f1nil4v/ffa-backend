package com.ffa.tournaments.player.application.exception;

public class NicknameChangeRequestNotOwnedException extends RuntimeException {

    public NicknameChangeRequestNotOwnedException() {
        super("Nickname change request does not belong to the current player");
    }
}

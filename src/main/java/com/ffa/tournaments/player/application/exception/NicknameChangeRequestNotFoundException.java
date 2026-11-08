package com.ffa.tournaments.player.application.exception;

public class NicknameChangeRequestNotFoundException extends RuntimeException {

    public NicknameChangeRequestNotFoundException() {
        super("Nickname change request not found");
    }
}

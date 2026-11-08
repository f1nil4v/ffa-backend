package com.ffa.tournaments.player.application.exception;

public class NicknameUnchangedException extends RuntimeException {

    public NicknameUnchangedException() {
        super("New nickname must differ from the current one");
    }
}

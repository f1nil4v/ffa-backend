package com.ffa.tournaments.player.application.exception;

public class InvalidSteamOpenIdResponseException extends RuntimeException {

    public InvalidSteamOpenIdResponseException() {
        super("Invalid Steam OpenID response");
    }
}

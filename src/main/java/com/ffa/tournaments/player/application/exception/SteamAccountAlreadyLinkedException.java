package com.ffa.tournaments.player.application.exception;

public class SteamAccountAlreadyLinkedException extends RuntimeException {

    public SteamAccountAlreadyLinkedException() {
        super("Steam account is already linked");
    }
}

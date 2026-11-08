package com.ffa.tournaments.player.application.exception;

public class SteamLinkStateNotFoundException extends RuntimeException {

    public SteamLinkStateNotFoundException() {
        super("Steam link state not found or expired");
    }
}

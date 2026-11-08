package com.ffa.tournaments.identity.application.exception;

public class EmailUnchangedException extends RuntimeException {

    public EmailUnchangedException() {
        super("New email must differ from the current one");
    }
}

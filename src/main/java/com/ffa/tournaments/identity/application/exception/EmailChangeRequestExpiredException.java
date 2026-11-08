package com.ffa.tournaments.identity.application.exception;

public class EmailChangeRequestExpiredException extends RuntimeException {

    public EmailChangeRequestExpiredException() {
        super("Email change request has expired");
    }
}

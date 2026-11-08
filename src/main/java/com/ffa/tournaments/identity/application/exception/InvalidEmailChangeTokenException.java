package com.ffa.tournaments.identity.application.exception;

public class InvalidEmailChangeTokenException extends RuntimeException {

    public InvalidEmailChangeTokenException() {
        super("Invalid email change token");
    }
}

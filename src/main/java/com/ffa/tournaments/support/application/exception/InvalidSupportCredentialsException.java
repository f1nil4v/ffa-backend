package com.ffa.tournaments.support.application.exception;

public class InvalidSupportCredentialsException extends RuntimeException {

    public InvalidSupportCredentialsException() {
        super("Invalid email or password");
    }
}

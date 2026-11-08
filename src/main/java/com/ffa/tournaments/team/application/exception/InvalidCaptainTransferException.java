package com.ffa.tournaments.team.application.exception;

public class InvalidCaptainTransferException extends RuntimeException {

    public InvalidCaptainTransferException() {
        super("Captaincy can only be transferred to another team member");
    }
}

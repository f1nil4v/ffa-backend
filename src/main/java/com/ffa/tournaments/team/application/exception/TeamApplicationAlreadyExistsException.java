package com.ffa.tournaments.team.application.exception;

public class TeamApplicationAlreadyExistsException extends RuntimeException {

    public TeamApplicationAlreadyExistsException() {
        super("A pending application to this team already exists");
    }
}

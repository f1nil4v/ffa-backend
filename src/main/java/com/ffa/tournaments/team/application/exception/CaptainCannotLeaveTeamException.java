package com.ffa.tournaments.team.application.exception;

public class CaptainCannotLeaveTeamException extends RuntimeException {

    public CaptainCannotLeaveTeamException() {
        super("Captain cannot leave the team, transfer captaincy or disband the team");
    }
}

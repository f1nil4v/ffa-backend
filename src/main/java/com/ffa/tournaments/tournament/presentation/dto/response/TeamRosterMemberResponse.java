package com.ffa.tournaments.tournament.presentation.dto.response;

import com.ffa.tournaments.tournament.application.result.TeamRosterMemberResult;

import java.util.UUID;

public record TeamRosterMemberResponse(
        UUID playerProfileId,
        String nickname,
        String steamId,
        String role
) {

    public static TeamRosterMemberResponse from(TeamRosterMemberResult result) {
        return new TeamRosterMemberResponse(
                result.playerProfileId(),
                result.nickname(),
                result.steamId(),
                result.role()
        );
    }
}

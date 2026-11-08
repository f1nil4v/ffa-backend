package com.ffa.tournaments.team.presentation.dto.response;

import com.ffa.tournaments.team.application.result.TeamMemberResult;

import java.time.Instant;
import java.util.UUID;

public record TeamMemberResponse(
        UUID playerProfileId,
        String nickname,
        String steamId,
        String role,
        Instant joinedAt
) {

    public static TeamMemberResponse from(TeamMemberResult result) {
        return new TeamMemberResponse(
                result.playerProfileId(),
                result.nickname(),
                result.steamId(),
                result.role(),
                result.joinedAt()
        );
    }
}

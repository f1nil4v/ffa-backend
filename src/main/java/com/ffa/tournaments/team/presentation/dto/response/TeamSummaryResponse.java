package com.ffa.tournaments.team.presentation.dto.response;

import com.ffa.tournaments.team.application.result.TeamSummaryResult;

import java.time.Instant;
import java.util.UUID;

public record TeamSummaryResponse(
        UUID id,
        String name,
        String tag,
        UUID captainProfileId,
        String description,
        Instant createdAt,
        long memberCount
) {

    public static TeamSummaryResponse from(TeamSummaryResult result) {
        return new TeamSummaryResponse(
                result.id(),
                result.name(),
                result.tag(),
                result.captainProfileId(),
                result.description(),
                result.createdAt(),
                result.memberCount()
        );
    }
}

package com.ffa.tournaments.team.presentation.dto.response;

import com.ffa.tournaments.team.application.result.TeamApplicationResult;

import java.time.Instant;
import java.util.UUID;

public record TeamApplicationResponse(
        UUID id,
        UUID teamId,
        UUID playerProfileId,
        String message,
        String status,
        UUID reviewedBy,
        Instant createdAt,
        Instant reviewedAt
) {

    public static TeamApplicationResponse from(TeamApplicationResult result) {
        return new TeamApplicationResponse(
                result.id(),
                result.teamId(),
                result.playerProfileId(),
                result.message(),
                result.status(),
                result.reviewedBy(),
                result.createdAt(),
                result.reviewedAt()
        );
    }
}

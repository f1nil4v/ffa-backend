package com.ffa.tournaments.team.application.result;

import com.ffa.tournaments.team.domain.model.TeamApplication;

import java.time.Instant;
import java.util.UUID;

public record TeamApplicationResult(
        UUID id,
        UUID teamId,
        UUID playerProfileId,
        String message,
        String status,
        UUID reviewedBy,
        Instant createdAt,
        Instant reviewedAt
) {

    public static TeamApplicationResult from(TeamApplication application) {
        return new TeamApplicationResult(
                application.getId(),
                application.getTeamId(),
                application.getPlayerProfileId().value(),
                application.getMessage(),
                application.getStatus().name(),
                application.getReviewedBy() == null
                        ? null
                        : application.getReviewedBy().value(),
                application.getCreatedAt(),
                application.getReviewedAt()
        );
    }
}

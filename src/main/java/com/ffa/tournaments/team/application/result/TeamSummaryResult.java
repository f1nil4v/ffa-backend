package com.ffa.tournaments.team.application.result;

import java.time.Instant;
import java.util.UUID;

public record TeamSummaryResult(
        UUID id,
        String name,
        String tag,
        UUID captainProfileId,
        String description,
        Instant createdAt,
        long memberCount
) {
}

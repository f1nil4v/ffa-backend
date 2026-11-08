package com.ffa.tournaments.team.application.result;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record TeamResult(
        UUID id,
        String name,
        String tag,
        UUID captainProfileId,
        String description,
        Instant createdAt,
        List<TeamMemberResult> members
) {
}

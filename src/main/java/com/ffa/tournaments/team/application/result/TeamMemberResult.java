package com.ffa.tournaments.team.application.result;

import java.time.Instant;
import java.util.UUID;

public record TeamMemberResult(
        UUID playerProfileId,
        String nickname,
        String steamId,
        String role,
        Instant joinedAt
) {
}

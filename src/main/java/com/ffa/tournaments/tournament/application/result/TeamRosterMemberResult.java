package com.ffa.tournaments.tournament.application.result;

import java.util.UUID;

public record TeamRosterMemberResult(
        UUID playerProfileId,
        String nickname,
        String steamId,
        String role
) {
}

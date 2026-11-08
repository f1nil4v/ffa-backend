package com.ffa.tournaments.support.application.result;

import java.util.UUID;

public record SupportLoginResult(
        UUID id,
        String email,
        String displayName,
        String accessToken,
        String tokenType
) {
}

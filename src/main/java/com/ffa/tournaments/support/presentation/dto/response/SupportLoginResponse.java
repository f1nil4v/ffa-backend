package com.ffa.tournaments.support.presentation.dto.response;

import java.util.UUID;

public record SupportLoginResponse(
        UUID id,
        String email,
        String displayName,
        String accessToken,
        String tokenType
) {
}

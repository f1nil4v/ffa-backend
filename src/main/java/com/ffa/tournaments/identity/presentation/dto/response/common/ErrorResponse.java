package com.ffa.tournaments.identity.presentation.dto.response.common;

import java.time.Instant;

public record ErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String message
) {}
package com.ffa.tournaments.identity.presentation.dto.response.email;

import com.ffa.tournaments.identity.application.result.EmailChangeRequestResult;

import java.time.Instant;
import java.util.UUID;

public record EmailChangeRequestResponse(
        UUID id,
        String currentEmail,
        String newEmail,
        String status,
        Instant expiresAt
) {

    public static EmailChangeRequestResponse from(EmailChangeRequestResult result) {
        return new EmailChangeRequestResponse(
                result.id(),
                result.currentEmail(),
                result.newEmail(),
                result.status(),
                result.expiresAt()
        );
    }
}

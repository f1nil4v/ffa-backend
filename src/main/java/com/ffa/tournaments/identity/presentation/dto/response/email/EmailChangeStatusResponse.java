package com.ffa.tournaments.identity.presentation.dto.response.email;

import com.ffa.tournaments.identity.application.result.EmailChangeStatusResult;

import java.time.Instant;
import java.util.UUID;

public record EmailChangeStatusResponse(
        boolean pending,
        UUID id,
        String newEmail,
        Instant expiresAt
) {

    public static EmailChangeStatusResponse from(EmailChangeStatusResult result) {
        return new EmailChangeStatusResponse(
                result.pending(),
                result.id(),
                result.newEmail(),
                result.expiresAt()
        );
    }
}

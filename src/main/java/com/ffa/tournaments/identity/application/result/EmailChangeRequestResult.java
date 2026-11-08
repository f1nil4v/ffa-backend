package com.ffa.tournaments.identity.application.result;

import com.ffa.tournaments.identity.domain.model.EmailChangeRequest;

import java.time.Instant;
import java.util.UUID;

public record EmailChangeRequestResult(
        UUID id,
        String currentEmail,
        String newEmail,
        String status,
        Instant expiresAt
) {

    public static EmailChangeRequestResult from(EmailChangeRequest request) {
        return new EmailChangeRequestResult(
                request.getId(),
                request.getCurrentEmail().value(),
                request.getNewEmail().value(),
                request.getStatus().name(),
                request.getExpiresAt()
        );
    }
}

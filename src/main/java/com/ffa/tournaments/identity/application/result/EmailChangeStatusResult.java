package com.ffa.tournaments.identity.application.result;

import com.ffa.tournaments.identity.domain.model.EmailChangeRequest;

import java.time.Instant;
import java.util.UUID;

public record EmailChangeStatusResult(
        boolean pending,
        UUID id,
        String newEmail,
        Instant expiresAt
) {

    public static EmailChangeStatusResult none() {
        return new EmailChangeStatusResult(false, null, null, null);
    }

    public static EmailChangeStatusResult from(EmailChangeRequest request) {
        return new EmailChangeStatusResult(
                true,
                request.getId(),
                request.getNewEmail().value(),
                request.getExpiresAt()
        );
    }
}

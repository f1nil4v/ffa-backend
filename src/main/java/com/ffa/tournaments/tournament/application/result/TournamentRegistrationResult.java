package com.ffa.tournaments.tournament.application.result;

import com.ffa.tournaments.tournament.domain.model.TournamentRegistration;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record TournamentRegistrationResult(
        UUID id,
        UUID tournamentId,
        UUID teamId,
        UUID registeredBy,
        String status,
        Integer seed,
        Instant createdAt,
        UUID reviewedBy,
        Instant reviewedAt,
        List<TeamRosterMemberResult> roster
) {

    public static TournamentRegistrationResult from(
            TournamentRegistration registration,
            List<TeamRosterMemberResult> roster
    ) {
        return new TournamentRegistrationResult(
                registration.getId(),
                registration.getTournamentId(),
                registration.getTeamId(),
                registration.getRegisteredBy().value(),
                registration.getStatus().name(),
                registration.getSeed(),
                registration.getCreatedAt(),
                registration.getReviewedBy() == null
                        ? null
                        : registration.getReviewedBy().value(),
                registration.getReviewedAt(),
                roster
        );
    }
}

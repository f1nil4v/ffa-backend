package com.ffa.tournaments.tournament.application.result;

import com.ffa.tournaments.tournament.domain.model.Tournament;

import java.time.Instant;
import java.util.UUID;

public record TournamentResult(
        UUID id,
        String name,
        String description,
        String format,
        String status,
        int maxTeams,
        Instant registrationOpensAt,
        Instant registrationClosesAt,
        Instant startsAt,
        UUID createdBy,
        Instant createdAt,
        Instant updatedAt
) {

    public static TournamentResult from(Tournament tournament) {
        return new TournamentResult(
                tournament.getId(),
                tournament.getName(),
                tournament.getDescription(),
                tournament.getFormat().name(),
                tournament.getStatus().name(),
                tournament.getMaxTeams(),
                tournament.getRegistrationOpensAt(),
                tournament.getRegistrationClosesAt(),
                tournament.getStartsAt(),
                tournament.getCreatedBy().value(),
                tournament.getCreatedAt(),
                tournament.getUpdatedAt()
        );
    }
}

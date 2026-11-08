package com.ffa.tournaments.tournament.presentation.dto.response;

import com.ffa.tournaments.tournament.application.result.TournamentResult;

import java.time.Instant;
import java.util.UUID;

public record TournamentResponse(
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

    public static TournamentResponse from(TournamentResult result) {
        return new TournamentResponse(
                result.id(),
                result.name(),
                result.description(),
                result.format(),
                result.status(),
                result.maxTeams(),
                result.registrationOpensAt(),
                result.registrationClosesAt(),
                result.startsAt(),
                result.createdBy(),
                result.createdAt(),
                result.updatedAt()
        );
    }
}

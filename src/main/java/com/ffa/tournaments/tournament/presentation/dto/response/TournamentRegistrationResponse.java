package com.ffa.tournaments.tournament.presentation.dto.response;

import com.ffa.tournaments.tournament.application.result.TournamentRegistrationResult;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record TournamentRegistrationResponse(
        UUID id,
        UUID tournamentId,
        UUID teamId,
        UUID registeredBy,
        String status,
        Integer seed,
        Instant createdAt,
        UUID reviewedBy,
        Instant reviewedAt,
        List<TeamRosterMemberResponse> roster
) {

    public static TournamentRegistrationResponse from(
            TournamentRegistrationResult result
    ) {
        return new TournamentRegistrationResponse(
                result.id(),
                result.tournamentId(),
                result.teamId(),
                result.registeredBy(),
                result.status(),
                result.seed(),
                result.createdAt(),
                result.reviewedBy(),
                result.reviewedAt(),
                result.roster()
                        .stream()
                        .map(TeamRosterMemberResponse::from)
                        .toList()
        );
    }
}

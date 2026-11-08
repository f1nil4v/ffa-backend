package com.ffa.tournaments.tournament.domain.repository;

import com.ffa.tournaments.tournament.domain.model.TournamentRegistration;
import com.ffa.tournaments.tournament.domain.model.TournamentRegistrationStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TournamentRegistrationRepository {

    TournamentRegistration save(TournamentRegistration registration);

    Optional<TournamentRegistration> findById(UUID id);

    List<TournamentRegistration> findAllByTournamentId(UUID tournamentId);

    List<TournamentRegistration> findAllByTournamentIdAndStatus(
            UUID tournamentId,
            TournamentRegistrationStatus status
    );

    List<TournamentRegistration> findAllByTeamId(UUID teamId);

    Optional<TournamentRegistration> findActiveByTournamentIdAndTeamId(
            UUID tournamentId,
            UUID teamId
    );

    boolean existsActive(UUID tournamentId, UUID teamId);

    long countActiveByTournamentId(UUID tournamentId);

    long countByTournamentIdAndStatus(
            UUID tournamentId,
            TournamentRegistrationStatus status
    );
}

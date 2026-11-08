package com.ffa.tournaments.tournament.domain.repository;

import com.ffa.tournaments.tournament.domain.model.Tournament;
import com.ffa.tournaments.tournament.domain.model.TournamentStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TournamentRepository {

    Tournament save(Tournament tournament);

    Optional<Tournament> findById(UUID id);

    Optional<Tournament> findByIdForUpdate(UUID id);

    List<Tournament> findAll();

    List<Tournament> findAllByStatus(TournamentStatus status);
}

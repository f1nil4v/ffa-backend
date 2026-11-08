package com.ffa.tournaments.team.domain.repository;

import com.ffa.tournaments.team.domain.model.Team;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TeamRepository {

    Team save(Team team);

    Optional<Team> findById(UUID id);

    List<Team> findAll();

    boolean existsByNameIgnoreCase(String name);

    boolean existsByTagIgnoreCase(String tag);

    void delete(Team team);
}

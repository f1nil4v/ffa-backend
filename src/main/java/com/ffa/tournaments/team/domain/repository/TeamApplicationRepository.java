package com.ffa.tournaments.team.domain.repository;

import com.ffa.tournaments.player.domain.valueobject.PlayerProfileId;
import com.ffa.tournaments.team.domain.model.TeamApplication;
import com.ffa.tournaments.team.domain.model.TeamApplicationStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TeamApplicationRepository {

    TeamApplication save(TeamApplication application);

    Optional<TeamApplication> findById(UUID id);

    List<TeamApplication> findAllByTeamIdAndStatus(
            UUID teamId,
            TeamApplicationStatus status
    );

    boolean existsPending(UUID teamId, PlayerProfileId playerProfileId);
}

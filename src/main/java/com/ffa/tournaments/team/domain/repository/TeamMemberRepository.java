package com.ffa.tournaments.team.domain.repository;

import com.ffa.tournaments.player.domain.valueobject.PlayerProfileId;
import com.ffa.tournaments.team.domain.model.TeamMember;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TeamMemberRepository {

    TeamMember save(TeamMember member);

    Optional<TeamMember> findByTeamIdAndPlayerProfileId(
            UUID teamId,
            PlayerProfileId playerProfileId
    );

    Optional<TeamMember> findByPlayerProfileId(PlayerProfileId playerProfileId);

    List<TeamMember> findAllByTeamId(UUID teamId);

    long countByTeamId(UUID teamId);

    boolean existsByPlayerProfileId(PlayerProfileId playerProfileId);

    void delete(TeamMember member);

    void deleteAllByTeamId(UUID teamId);
}

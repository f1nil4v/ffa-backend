package com.ffa.tournaments.team.infrastructure.persistence.repository;

import com.ffa.tournaments.team.infrastructure.persistence.entity.TeamMemberId;
import com.ffa.tournaments.team.infrastructure.persistence.entity.TeamMemberJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataTeamMemberRepository
        extends JpaRepository<TeamMemberJpaEntity, TeamMemberId> {

    Optional<TeamMemberJpaEntity> findByTeamIdAndPlayerProfileId(
            UUID teamId,
            UUID playerProfileId
    );

    Optional<TeamMemberJpaEntity> findByPlayerProfileId(UUID playerProfileId);

    List<TeamMemberJpaEntity> findAllByTeamId(UUID teamId);

    long countByTeamId(UUID teamId);

    boolean existsByPlayerProfileId(UUID playerProfileId);

    void deleteAllByTeamId(UUID teamId);
}

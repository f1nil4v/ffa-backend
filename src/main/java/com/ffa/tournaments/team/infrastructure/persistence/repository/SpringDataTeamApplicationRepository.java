package com.ffa.tournaments.team.infrastructure.persistence.repository;

import com.ffa.tournaments.team.infrastructure.persistence.entity.TeamApplicationJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SpringDataTeamApplicationRepository
        extends JpaRepository<TeamApplicationJpaEntity, UUID> {

    List<TeamApplicationJpaEntity> findAllByTeamIdAndStatus(UUID teamId, String status);

    boolean existsByTeamIdAndPlayerProfileIdAndStatus(
            UUID teamId,
            UUID playerProfileId,
            String status
    );
}

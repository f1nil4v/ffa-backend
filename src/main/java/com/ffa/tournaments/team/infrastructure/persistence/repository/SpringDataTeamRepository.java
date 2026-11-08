package com.ffa.tournaments.team.infrastructure.persistence.repository;

import com.ffa.tournaments.team.infrastructure.persistence.entity.TeamJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SpringDataTeamRepository extends JpaRepository<TeamJpaEntity, UUID> {

    boolean existsByNameIgnoreCase(String name);

    boolean existsByTagIgnoreCase(String tag);

    List<TeamJpaEntity> findAllByOrderByCreatedAtDesc();
}

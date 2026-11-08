package com.ffa.tournaments.team.infrastructure.persistence.adapter;

import com.ffa.tournaments.player.domain.valueobject.PlayerProfileId;
import com.ffa.tournaments.team.domain.model.TeamApplication;
import com.ffa.tournaments.team.domain.model.TeamApplicationStatus;
import com.ffa.tournaments.team.domain.repository.TeamApplicationRepository;
import com.ffa.tournaments.team.infrastructure.persistence.mapper.TeamApplicationMapper;
import com.ffa.tournaments.team.infrastructure.persistence.repository.SpringDataTeamApplicationRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TeamApplicationRepositoryAdapter implements TeamApplicationRepository {

    private final SpringDataTeamApplicationRepository jpaRepository;
    private final TeamApplicationMapper mapper;

    public TeamApplicationRepositoryAdapter(
            SpringDataTeamApplicationRepository jpaRepository,
            TeamApplicationMapper mapper
    ) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public TeamApplication save(TeamApplication application) {
        return mapper.toDomain(
                jpaRepository.saveAndFlush(mapper.toEntity(application))
        );
    }

    @Override
    public Optional<TeamApplication> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<TeamApplication> findAllByTeamIdAndStatus(
            UUID teamId,
            TeamApplicationStatus status
    ) {
        return jpaRepository.findAllByTeamIdAndStatus(teamId, status.name())
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsPending(UUID teamId, PlayerProfileId playerProfileId) {
        return jpaRepository.existsByTeamIdAndPlayerProfileIdAndStatus(
                teamId,
                playerProfileId.value(),
                TeamApplicationStatus.PENDING.name()
        );
    }
}

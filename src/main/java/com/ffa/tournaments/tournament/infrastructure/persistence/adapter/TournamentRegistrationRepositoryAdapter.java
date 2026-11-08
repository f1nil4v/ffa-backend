package com.ffa.tournaments.tournament.infrastructure.persistence.adapter;

import com.ffa.tournaments.tournament.domain.model.TournamentRegistration;
import com.ffa.tournaments.tournament.domain.model.TournamentRegistrationStatus;
import com.ffa.tournaments.tournament.domain.repository.TournamentRegistrationRepository;
import com.ffa.tournaments.tournament.infrastructure.persistence.mapper.TournamentRegistrationMapper;
import com.ffa.tournaments.tournament.infrastructure.persistence.repository.SpringDataTournamentRegistrationRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TournamentRegistrationRepositoryAdapter
        implements TournamentRegistrationRepository {

    private static final List<String> ACTIVE_STATUSES = List.of(
            TournamentRegistrationStatus.PENDING.name(),
            TournamentRegistrationStatus.APPROVED.name()
    );

    private final SpringDataTournamentRegistrationRepository jpaRepository;
    private final TournamentRegistrationMapper mapper;

    public TournamentRegistrationRepositoryAdapter(
            SpringDataTournamentRegistrationRepository jpaRepository,
            TournamentRegistrationMapper mapper
    ) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public TournamentRegistration save(TournamentRegistration registration) {
        return mapper.toDomain(
                jpaRepository.saveAndFlush(mapper.toEntity(registration))
        );
    }

    @Override
    public Optional<TournamentRegistration> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<TournamentRegistration> findAllByTournamentId(UUID tournamentId) {
        return jpaRepository.findAllByTournamentIdOrderByCreatedAtAsc(tournamentId)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<TournamentRegistration> findAllByTournamentIdAndStatus(
            UUID tournamentId,
            TournamentRegistrationStatus status
    ) {
        return jpaRepository
                .findAllByTournamentIdAndStatusOrderByCreatedAtAsc(
                        tournamentId,
                        status.name()
                )
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<TournamentRegistration> findAllByTeamId(UUID teamId) {
        return jpaRepository.findAllByTeamIdOrderByCreatedAtDesc(teamId)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<TournamentRegistration> findActiveByTournamentIdAndTeamId(
            UUID tournamentId,
            UUID teamId
    ) {
        return jpaRepository.findActive(tournamentId, teamId).map(mapper::toDomain);
    }

    @Override
    public boolean existsActive(UUID tournamentId, UUID teamId) {
        return jpaRepository.findActive(tournamentId, teamId).isPresent();
    }

    @Override
    public long countActiveByTournamentId(UUID tournamentId) {
        return jpaRepository.countByTournamentIdAndStatusIn(
                tournamentId,
                ACTIVE_STATUSES
        );
    }

    @Override
    public long countByTournamentIdAndStatus(
            UUID tournamentId,
            TournamentRegistrationStatus status
    ) {
        return jpaRepository.countByTournamentIdAndStatus(
                tournamentId,
                status.name()
        );
    }
}

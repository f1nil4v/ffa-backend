package com.ffa.tournaments.tournament.infrastructure.persistence.adapter;

import com.ffa.tournaments.tournament.domain.model.Tournament;
import com.ffa.tournaments.tournament.domain.model.TournamentStatus;
import com.ffa.tournaments.tournament.domain.repository.TournamentRepository;
import com.ffa.tournaments.tournament.infrastructure.persistence.mapper.TournamentMapper;
import com.ffa.tournaments.tournament.infrastructure.persistence.repository.SpringDataTournamentRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TournamentRepositoryAdapter implements TournamentRepository {

    private final SpringDataTournamentRepository jpaRepository;
    private final TournamentMapper mapper;

    public TournamentRepositoryAdapter(
            SpringDataTournamentRepository jpaRepository,
            TournamentMapper mapper
    ) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Tournament save(Tournament tournament) {
        return mapper.toDomain(
                jpaRepository.saveAndFlush(mapper.toEntity(tournament))
        );
    }

    @Override
    public Optional<Tournament> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Tournament> findByIdForUpdate(UUID id) {
        return jpaRepository.findWithLockById(id).map(mapper::toDomain);
    }

    @Override
    public List<Tournament> findAll() {
        return jpaRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Tournament> findAllByStatus(TournamentStatus status) {
        return jpaRepository.findAllByStatusOrderByCreatedAtDesc(status.name())
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}

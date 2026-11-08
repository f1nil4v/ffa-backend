package com.ffa.tournaments.player.infrastructure.persistence.adapter;

import com.ffa.tournaments.player.domain.model.SteamLinkState;
import com.ffa.tournaments.player.domain.repository.SteamLinkStateRepository;
import com.ffa.tournaments.player.infrastructure.persistence.mapper.SteamLinkStateMapper;
import com.ffa.tournaments.player.infrastructure.persistence.repository.SpringDataSteamLinkStateRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class SteamLinkStateRepositoryAdapter implements SteamLinkStateRepository {

    private final SpringDataSteamLinkStateRepository jpaRepository;
    private final SteamLinkStateMapper mapper;

    public SteamLinkStateRepositoryAdapter(
            SpringDataSteamLinkStateRepository jpaRepository,
            SteamLinkStateMapper mapper
    ) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public SteamLinkState save(SteamLinkState state) {
        return mapper.toDomain(
                jpaRepository.save(mapper.toEntity(state))
        );
    }

    @Override
    public Optional<SteamLinkState> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}

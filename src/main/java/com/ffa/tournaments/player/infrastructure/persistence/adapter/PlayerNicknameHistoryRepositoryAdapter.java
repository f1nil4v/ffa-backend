package com.ffa.tournaments.player.infrastructure.persistence.adapter;

import com.ffa.tournaments.player.domain.model.PlayerNicknameHistory;
import com.ffa.tournaments.player.domain.repository.PlayerNicknameHistoryRepository;
import com.ffa.tournaments.player.domain.valueobject.PlayerProfileId;
import com.ffa.tournaments.player.infrastructure.persistence.mapper.PlayerNicknameHistoryMapper;
import com.ffa.tournaments.player.infrastructure.persistence.repository.SpringDataPlayerNicknameHistoryRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PlayerNicknameHistoryRepositoryAdapter implements PlayerNicknameHistoryRepository {

    private final SpringDataPlayerNicknameHistoryRepository jpaRepository;
    private final PlayerNicknameHistoryMapper mapper;

    public PlayerNicknameHistoryRepositoryAdapter(
            SpringDataPlayerNicknameHistoryRepository jpaRepository,
            PlayerNicknameHistoryMapper mapper
    ) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public PlayerNicknameHistory save(PlayerNicknameHistory history) {
        var entity = mapper.toEntity(history);
        var savedEntity = jpaRepository.saveAndFlush(entity);

        return mapper.toDomain(savedEntity);
    }

    @Override
    public List<PlayerNicknameHistory> findAllByPlayerProfileId(PlayerProfileId playerProfileId) {
        return jpaRepository.findAllByPlayerProfileId(playerProfileId.value())
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public java.util.Optional<PlayerNicknameHistory> findCurrentByPlayerProfileId(
            PlayerProfileId playerProfileId
    ) {
        return jpaRepository.findByPlayerProfileIdAndValidToIsNull(playerProfileId.value())
                .map(mapper::toDomain);
    }
}
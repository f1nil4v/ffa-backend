package com.ffa.tournaments.player.infrastructure.persistence.repository;

import com.ffa.tournaments.player.infrastructure.persistence.entity.PlayerNicknameHistoryJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataPlayerNicknameHistoryRepository
        extends JpaRepository<PlayerNicknameHistoryJpaEntity, UUID> {

    List<PlayerNicknameHistoryJpaEntity> findAllByPlayerProfileId(UUID playerProfileId);

    Optional<PlayerNicknameHistoryJpaEntity> findByPlayerProfileIdAndValidToIsNull(UUID playerProfileId);
}
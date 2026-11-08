package com.ffa.tournaments.player.infrastructure.persistence.repository;

import com.ffa.tournaments.player.infrastructure.persistence.entity.PlayerProfileJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataPlayerProfileRepository extends JpaRepository<PlayerProfileJpaEntity, UUID> {

    Optional<PlayerProfileJpaEntity> findByAccountId(UUID accountId);

    boolean existsByNicknameIgnoreCase(String nickname);

    boolean existsBySteamId(String steamId);
}
package com.ffa.tournaments.player.infrastructure.persistence.repository;

import com.ffa.tournaments.player.infrastructure.persistence.entity.SteamLinkStateJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataSteamLinkStateRepository
        extends JpaRepository<SteamLinkStateJpaEntity, UUID> {
}

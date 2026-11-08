package com.ffa.tournaments.player.infrastructure.persistence.repository;

import com.ffa.tournaments.player.infrastructure.persistence.entity.NicknameChangeRequestJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SpringDataNicknameChangeRequestRepository
        extends JpaRepository<NicknameChangeRequestJpaEntity, UUID> {

    List<NicknameChangeRequestJpaEntity> findAllByStatus(String status);

    List<NicknameChangeRequestJpaEntity> findAllByPlayerProfileIdOrderByCreatedAtDesc(
            UUID playerProfileId
    );

    boolean existsByPlayerProfileIdAndStatus(
            UUID playerProfileId,
            String status
    );
}
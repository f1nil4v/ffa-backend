package com.ffa.tournaments.support.infrastructure.persistence.repository;

import com.ffa.tournaments.support.infrastructure.persistence.entity.SupportAccountJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataSupportAccountRepository
        extends JpaRepository<SupportAccountJpaEntity, UUID> {

    Optional<SupportAccountJpaEntity> findByEmail(String email);

    boolean existsByEmail(String email);
}

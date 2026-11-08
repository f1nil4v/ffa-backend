package com.ffa.tournaments.identity.infrastructure.persistence.repository;

import com.ffa.tournaments.identity.infrastructure.persistence.entity.PasswordResetTokenJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataPasswordResetTokenRepository extends JpaRepository<PasswordResetTokenJpaEntity, UUID> {

    Optional<PasswordResetTokenJpaEntity> findByTokenHashAndUsedAtIsNull(String tokenHash);

    List<PasswordResetTokenJpaEntity> findAllByAccountIdAndUsedAtIsNull(UUID accountId);
}
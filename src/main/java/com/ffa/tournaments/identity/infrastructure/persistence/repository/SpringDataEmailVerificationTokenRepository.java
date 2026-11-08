package com.ffa.tournaments.identity.infrastructure.persistence.repository;

import com.ffa.tournaments.identity.infrastructure.persistence.entity.EmailVerificationTokenJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataEmailVerificationTokenRepository extends JpaRepository<EmailVerificationTokenJpaEntity, UUID> {

    Optional<EmailVerificationTokenJpaEntity> findByTokenHashAndUsedAtIsNull(String tokenHash);

    List<EmailVerificationTokenJpaEntity> findAllByAccountIdAndUsedAtIsNull(UUID accountId);
}
package com.ffa.tournaments.identity.infrastructure.persistence.repository;

import com.ffa.tournaments.identity.infrastructure.persistence.entity.EmailChangeRequestJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataEmailChangeRequestRepository
        extends JpaRepository<EmailChangeRequestJpaEntity, UUID> {

    @Query("""
            SELECT request
            FROM EmailChangeRequestJpaEntity request
            WHERE request.oldTokenHash = :tokenHash
               OR request.newTokenHash = :tokenHash
            """)
    Optional<EmailChangeRequestJpaEntity> findByTokenHash(@Param("tokenHash") String tokenHash);

    Optional<EmailChangeRequestJpaEntity> findByAccountIdAndStatus(
            UUID accountId,
            String status
    );
}

package com.ffa.tournaments.tournament.infrastructure.persistence.repository;

import com.ffa.tournaments.tournament.infrastructure.persistence.entity.TournamentRegistrationJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataTournamentRegistrationRepository
        extends JpaRepository<TournamentRegistrationJpaEntity, UUID> {

    List<TournamentRegistrationJpaEntity> findAllByTournamentIdOrderByCreatedAtAsc(
            UUID tournamentId
    );

    List<TournamentRegistrationJpaEntity> findAllByTournamentIdAndStatusOrderByCreatedAtAsc(
            UUID tournamentId,
            String status
    );

    List<TournamentRegistrationJpaEntity> findAllByTeamIdOrderByCreatedAtDesc(UUID teamId);

    @Query("""
            SELECT registration
            FROM TournamentRegistrationJpaEntity registration
            WHERE registration.tournamentId = :tournamentId
              AND registration.teamId = :teamId
              AND registration.status IN ('PENDING', 'APPROVED')
            """)
    Optional<TournamentRegistrationJpaEntity> findActive(
            @Param("tournamentId") UUID tournamentId,
            @Param("teamId") UUID teamId
    );

    @Query("""
            SELECT COUNT(registration)
            FROM TournamentRegistrationJpaEntity registration
            WHERE registration.tournamentId = :tournamentId
              AND registration.status IN :statuses
            """)
    long countByTournamentIdAndStatusIn(
            @Param("tournamentId") UUID tournamentId,
            @Param("statuses") Collection<String> statuses
    );

    long countByTournamentIdAndStatus(UUID tournamentId, String status);
}

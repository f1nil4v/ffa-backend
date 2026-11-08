package com.ffa.tournaments.tournament.infrastructure.persistence.repository;

import com.ffa.tournaments.tournament.infrastructure.persistence.entity.TournamentJpaEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataTournamentRepository extends JpaRepository<TournamentJpaEntity, UUID> {

    List<TournamentJpaEntity> findAllByOrderByCreatedAtDesc();

    List<TournamentJpaEntity> findAllByStatusOrderByCreatedAtDesc(String status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT tournament FROM TournamentJpaEntity tournament WHERE tournament.id = :id")
    Optional<TournamentJpaEntity> findWithLockById(@Param("id") UUID id);
}

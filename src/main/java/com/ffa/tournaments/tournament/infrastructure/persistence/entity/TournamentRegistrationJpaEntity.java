package com.ffa.tournaments.tournament.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.ColumnTransformer;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tournament_registrations")
public class TournamentRegistrationJpaEntity {

    @Id
    private UUID id;

    @Column(name = "tournament_id", nullable = false)
    private UUID tournamentId;

    @Column(name = "team_id", nullable = false)
    private UUID teamId;

    @Column(name = "registered_by", nullable = false)
    private UUID registeredBy;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(name = "roster_snapshot", nullable = false, columnDefinition = "jsonb")
    @ColumnTransformer(write = "?::jsonb")
    private String rosterSnapshot;

    @Column
    private Integer seed;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "reviewed_by")
    private UUID reviewedBy;

    @Column(name = "reviewed_at")
    private Instant reviewedAt;

    protected TournamentRegistrationJpaEntity() {
    }

    public TournamentRegistrationJpaEntity(
            UUID id,
            UUID tournamentId,
            UUID teamId,
            UUID registeredBy,
            String status,
            String rosterSnapshot,
            Integer seed,
            Instant createdAt,
            UUID reviewedBy,
            Instant reviewedAt
    ) {
        this.id = id;
        this.tournamentId = tournamentId;
        this.teamId = teamId;
        this.registeredBy = registeredBy;
        this.status = status;
        this.rosterSnapshot = rosterSnapshot;
        this.seed = seed;
        this.createdAt = createdAt;
        this.reviewedBy = reviewedBy;
        this.reviewedAt = reviewedAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getTournamentId() {
        return tournamentId;
    }

    public UUID getTeamId() {
        return teamId;
    }

    public UUID getRegisteredBy() {
        return registeredBy;
    }

    public String getStatus() {
        return status;
    }

    public String getRosterSnapshot() {
        return rosterSnapshot;
    }

    public Integer getSeed() {
        return seed;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public UUID getReviewedBy() {
        return reviewedBy;
    }

    public Instant getReviewedAt() {
        return reviewedAt;
    }
}

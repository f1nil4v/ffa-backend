package com.ffa.tournaments.team.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "team_applications")
public class TeamApplicationJpaEntity {

    @Id
    private UUID id;

    @Column(name = "team_id", nullable = false)
    private UUID teamId;

    @Column(name = "player_profile_id", nullable = false)
    private UUID playerProfileId;

    @Column
    private String message;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(name = "reviewed_by")
    private UUID reviewedBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "reviewed_at")
    private Instant reviewedAt;

    protected TeamApplicationJpaEntity() {
    }

    public TeamApplicationJpaEntity(
            UUID id,
            UUID teamId,
            UUID playerProfileId,
            String message,
            String status,
            UUID reviewedBy,
            Instant createdAt,
            Instant reviewedAt
    ) {
        this.id = id;
        this.teamId = teamId;
        this.playerProfileId = playerProfileId;
        this.message = message;
        this.status = status;
        this.reviewedBy = reviewedBy;
        this.createdAt = createdAt;
        this.reviewedAt = reviewedAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getTeamId() {
        return teamId;
    }

    public UUID getPlayerProfileId() {
        return playerProfileId;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public UUID getReviewedBy() {
        return reviewedBy;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getReviewedAt() {
        return reviewedAt;
    }
}

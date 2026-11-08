package com.ffa.tournaments.team.domain.model;

import com.ffa.tournaments.player.domain.valueobject.PlayerProfileId;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class TeamApplication {

    private final UUID id;
    private final UUID teamId;
    private final PlayerProfileId playerProfileId;
    private final String message;
    private TeamApplicationStatus status;
    private PlayerProfileId reviewedBy;
    private Instant reviewedAt;
    private final Instant createdAt;

    private TeamApplication(
            UUID id,
            UUID teamId,
            PlayerProfileId playerProfileId,
            String message,
            TeamApplicationStatus status,
            PlayerProfileId reviewedBy,
            Instant reviewedAt,
            Instant createdAt
    ) {
        this.id = Objects.requireNonNull(id);
        this.teamId = Objects.requireNonNull(teamId);
        this.playerProfileId = Objects.requireNonNull(playerProfileId);
        this.message = message;
        this.status = Objects.requireNonNull(status);
        this.reviewedBy = reviewedBy;
        this.reviewedAt = reviewedAt;
        this.createdAt = Objects.requireNonNull(createdAt);
    }

    public static TeamApplication create(
            UUID teamId,
            PlayerProfileId playerProfileId,
            String message
    ) {
        return new TeamApplication(
                UUID.randomUUID(),
                teamId,
                playerProfileId,
                validateMessage(message),
                TeamApplicationStatus.PENDING,
                null,
                null,
                Instant.now()
        );
    }

    public static TeamApplication restore(
            UUID id,
            UUID teamId,
            PlayerProfileId playerProfileId,
            String message,
            TeamApplicationStatus status,
            PlayerProfileId reviewedBy,
            Instant reviewedAt,
            Instant createdAt
    ) {
        return new TeamApplication(
                id,
                teamId,
                playerProfileId,
                message,
                status,
                reviewedBy,
                reviewedAt,
                createdAt
        );
    }

    public void approve(PlayerProfileId reviewerId, Instant now) {
        ensurePending();

        this.status = TeamApplicationStatus.APPROVED;
        this.reviewedBy = reviewerId;
        this.reviewedAt = now;
    }

    public void reject(PlayerProfileId reviewerId, Instant now) {
        ensurePending();

        this.status = TeamApplicationStatus.REJECTED;
        this.reviewedBy = reviewerId;
        this.reviewedAt = now;
    }

    public void withdraw() {
        ensurePending();

        this.status = TeamApplicationStatus.WITHDRAWN;
    }

    private void ensurePending() {
        if (status != TeamApplicationStatus.PENDING) {
            throw new IllegalStateException("Team application is not pending");
        }
    }

    private static String validateMessage(String message) {
        if (message == null) {
            return null;
        }

        String normalized = message.trim();

        if (normalized.length() > 500) {
            throw new IllegalArgumentException("Application message cannot exceed 500 characters");
        }

        return normalized.isBlank() ? null : normalized;
    }

    public UUID getId() {
        return id;
    }

    public UUID getTeamId() {
        return teamId;
    }

    public PlayerProfileId getPlayerProfileId() {
        return playerProfileId;
    }

    public String getMessage() {
        return message;
    }

    public TeamApplicationStatus getStatus() {
        return status;
    }

    public PlayerProfileId getReviewedBy() {
        return reviewedBy;
    }

    public Instant getReviewedAt() {
        return reviewedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}

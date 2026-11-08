package com.ffa.tournaments.player.infrastructure.persistence.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "nickname_change_requests")
public class NicknameChangeRequestJpaEntity {

    @Id
    private UUID id;

    @Column(name = "player_profile_id", nullable = false)
    private UUID playerProfileId;

    @Column(name = "old_nickname", nullable = false, length = 32)
    private String oldNickname;

    @Column(name = "new_nickname", nullable = false, length = 32)
    private String newNickname;

    @Column(nullable = false)
    private String reason;

    @Column(nullable = false)
    private String status;

    @Column(name = "reviewed_by")
    private UUID reviewedBy;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "reviewed_at")
    private Instant reviewedAt;

    protected NicknameChangeRequestJpaEntity() {
    }

    public NicknameChangeRequestJpaEntity(
            UUID id,
            UUID playerProfileId,
            String oldNickname,
            String newNickname,
            String reason,
            String status,
            UUID reviewedBy,
            Instant createdAt,
            Instant reviewedAt
    ) {
        this.id = id;
        this.playerProfileId = playerProfileId;
        this.oldNickname = oldNickname;
        this.newNickname = newNickname;
        this.reason = reason;
        this.status = status;
        this.reviewedBy = reviewedBy;
        this.createdAt = createdAt;
        this.reviewedAt = reviewedAt;
    }

    public UUID getId() { return id; }
    public UUID getPlayerProfileId() { return playerProfileId; }
    public String getOldNickname() { return oldNickname; }
    public String getNewNickname() { return newNickname; }
    public String getReason() { return reason; }
    public String getStatus() { return status; }
    public UUID getReviewedBy() { return reviewedBy; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getReviewedAt() { return reviewedAt; }
}
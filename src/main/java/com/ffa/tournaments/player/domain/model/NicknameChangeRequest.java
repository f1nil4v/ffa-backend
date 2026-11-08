package com.ffa.tournaments.player.domain.model;

import com.ffa.tournaments.player.domain.valueobject.PlayerProfileId;
import com.ffa.tournaments.support.domain.valueobject.SupportAccountId;

import java.time.Instant;
import java.util.UUID;

public class NicknameChangeRequest {

    private final UUID id;
    private final PlayerProfileId playerProfileId;

    private final String oldNickname;
    private final String newNickname;
    private final String reason;

    private NicknameChangeRequestStatus status;

    private final Instant createdAt;

    private SupportAccountId reviewedBy;
    private Instant reviewedAt;

    private NicknameChangeRequest(
            UUID id,
            PlayerProfileId playerProfileId,
            String oldNickname,
            String newNickname,
            String reason,
            NicknameChangeRequestStatus status,
            Instant createdAt,
            SupportAccountId reviewedBy,
            Instant reviewedAt
    ) {
        this.id = id;
        this.playerProfileId = playerProfileId;
        this.oldNickname = oldNickname;
        this.newNickname = newNickname;
        this.reason = reason;
        this.status = status;
        this.createdAt = createdAt;
        this.reviewedBy = reviewedBy;
        this.reviewedAt = reviewedAt;
    }

    public static NicknameChangeRequest create(
            PlayerProfileId playerProfileId,
            String oldNickname,
            String newNickname,
            String reason
    ) {
        return new NicknameChangeRequest(
                UUID.randomUUID(),
                playerProfileId,
                oldNickname,
                newNickname,
                reason,
                NicknameChangeRequestStatus.PENDING,
                Instant.now(),
                null,
                null
        );
    }

    public static NicknameChangeRequest restore(
            UUID id,
            PlayerProfileId playerProfileId,
            String oldNickname,
            String newNickname,
            String reason,
            NicknameChangeRequestStatus status,
            Instant createdAt,
            SupportAccountId reviewedBy,
            Instant reviewedAt
    ) {
        return new NicknameChangeRequest(
                id,
                playerProfileId,
                oldNickname,
                newNickname,
                reason,
                status,
                createdAt,
                reviewedBy,
                reviewedAt
        );
    }

    public void approve(SupportAccountId reviewerId, Instant now) {
        ensurePending();

        this.status = NicknameChangeRequestStatus.APPROVED;
        this.reviewedBy = reviewerId;
        this.reviewedAt = now;
    }

    public void reject(SupportAccountId reviewerId, Instant now) {
        ensurePending();

        this.status = NicknameChangeRequestStatus.REJECTED;
        this.reviewedBy = reviewerId;
        this.reviewedAt = now;
    }

    public void cancel() {
        ensurePending();

        this.status = NicknameChangeRequestStatus.CANCELLED;
    }

    private void ensurePending() {
        if (status != NicknameChangeRequestStatus.PENDING) {
            throw new IllegalStateException(
                    "Nickname change request is not pending"
            );
        }
    }

    public UUID getId() {
        return id;
    }

    public PlayerProfileId getPlayerProfileId() {
        return playerProfileId;
    }

    public String getOldNickname() {
        return oldNickname;
    }

    public String getNewNickname() {
        return newNickname;
    }

    public String getReason() {
        return reason;
    }

    public NicknameChangeRequestStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public SupportAccountId getReviewedBy() {
        return reviewedBy;
    }

    public Instant getReviewedAt() {
        return reviewedAt;
    }
}
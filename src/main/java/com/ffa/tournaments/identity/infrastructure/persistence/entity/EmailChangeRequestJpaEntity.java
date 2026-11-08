package com.ffa.tournaments.identity.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "email_change_requests")
public class EmailChangeRequestJpaEntity {

    @Id
    private UUID id;

    @Column(name = "account_id", nullable = false)
    private UUID accountId;

    @Column(name = "current_email", nullable = false, length = 254)
    private String currentEmail;

    @Column(name = "new_email", nullable = false, length = 254)
    private String newEmail;

    @Column(name = "old_token_hash", nullable = false, length = 64)
    private String oldTokenHash;

    @Column(name = "new_token_hash", nullable = false, length = 64)
    private String newTokenHash;

    @Column(name = "old_confirmed_at")
    private Instant oldConfirmedAt;

    @Column(name = "new_confirmed_at")
    private Instant newConfirmedAt;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    protected EmailChangeRequestJpaEntity() {
    }

    public EmailChangeRequestJpaEntity(
            UUID id,
            UUID accountId,
            String currentEmail,
            String newEmail,
            String oldTokenHash,
            String newTokenHash,
            Instant oldConfirmedAt,
            Instant newConfirmedAt,
            String status,
            Instant createdAt,
            Instant expiresAt
    ) {
        this.id = id;
        this.accountId = accountId;
        this.currentEmail = currentEmail;
        this.newEmail = newEmail;
        this.oldTokenHash = oldTokenHash;
        this.newTokenHash = newTokenHash;
        this.oldConfirmedAt = oldConfirmedAt;
        this.newConfirmedAt = newConfirmedAt;
        this.status = status;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public String getCurrentEmail() {
        return currentEmail;
    }

    public String getNewEmail() {
        return newEmail;
    }

    public String getOldTokenHash() {
        return oldTokenHash;
    }

    public String getNewTokenHash() {
        return newTokenHash;
    }

    public Instant getOldConfirmedAt() {
        return oldConfirmedAt;
    }

    public Instant getNewConfirmedAt() {
        return newConfirmedAt;
    }

    public String getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }
}

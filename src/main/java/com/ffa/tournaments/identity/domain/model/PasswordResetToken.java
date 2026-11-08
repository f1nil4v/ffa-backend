package com.ffa.tournaments.identity.domain.model;

import com.ffa.tournaments.identity.domain.valueobject.AccountId;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class PasswordResetToken {

    private final UUID id;
    private final AccountId accountId;
    private final String tokenHash;
    private final Instant expiresAt;
    private Instant usedAt;
    private final Instant createdAt;

    private PasswordResetToken(UUID id, AccountId accountId, String tokenHash, Instant expiresAt, Instant usedAt, Instant createdAt) {
        this.id = Objects.requireNonNull(id);
        this.accountId = Objects.requireNonNull(accountId);
        this.tokenHash = Objects.requireNonNull(tokenHash);
        this.expiresAt = Objects.requireNonNull(expiresAt);
        this.usedAt = usedAt;
        this.createdAt = Objects.requireNonNull(createdAt);
    }

    public static PasswordResetToken create(AccountId accountId, String tokenHash, Instant expiresAt) {
        return new PasswordResetToken(UUID.randomUUID(), accountId, tokenHash, expiresAt, null, Instant.now());
    }

    public static PasswordResetToken restore(UUID id, AccountId accountId, String tokenHash, Instant expiresAt, Instant usedAt, Instant createdAt) {
        return new PasswordResetToken(id, accountId, tokenHash, expiresAt, usedAt, createdAt);
    }

    public boolean isExpired() {
        return !Instant.now().isBefore(expiresAt);
    }

    public boolean isUsed() {
        return usedAt != null;
    }

    public boolean isValid() {
        return !isUsed() && !isExpired();
    }

    public void markAsUsed() {
        if (!isValid()) {
            throw new IllegalStateException("Token is expired or already used");
        }

        this.usedAt = Instant.now();
    }

    public UUID getId() { return id; }
    public AccountId getAccountId() { return accountId; }
    public String getTokenHash() { return tokenHash; }
    public Instant getExpiresAt() { return expiresAt; }
    public Instant getUsedAt() { return usedAt; }
    public Instant getCreatedAt() { return createdAt; }
}
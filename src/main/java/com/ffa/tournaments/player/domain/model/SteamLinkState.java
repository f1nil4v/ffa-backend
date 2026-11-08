package com.ffa.tournaments.player.domain.model;

import com.ffa.tournaments.identity.domain.valueobject.AccountId;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class SteamLinkState {

    private final UUID id;
    private final AccountId accountId;
    private final Instant createdAt;
    private final Instant expiresAt;

    private SteamLinkState(
            UUID id,
            AccountId accountId,
            Instant createdAt,
            Instant expiresAt
    ) {
        this.id = Objects.requireNonNull(id);
        this.accountId = Objects.requireNonNull(accountId);
        this.createdAt = Objects.requireNonNull(createdAt);
        this.expiresAt = Objects.requireNonNull(expiresAt);
    }

    public static SteamLinkState create(AccountId accountId, Instant expiresAt) {
        return new SteamLinkState(
                UUID.randomUUID(),
                accountId,
                Instant.now(),
                expiresAt
        );
    }

    public static SteamLinkState restore(
            UUID id,
            AccountId accountId,
            Instant createdAt,
            Instant expiresAt
    ) {
        return new SteamLinkState(id, accountId, createdAt, expiresAt);
    }

    public boolean isExpired(Instant now) {
        return !now.isBefore(expiresAt);
    }

    public UUID getId() {
        return id;
    }

    public AccountId getAccountId() {
        return accountId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }
}

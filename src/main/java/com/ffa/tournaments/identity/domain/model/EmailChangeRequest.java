package com.ffa.tournaments.identity.domain.model;

import com.ffa.tournaments.identity.domain.valueobject.AccountId;
import com.ffa.tournaments.identity.domain.valueobject.Email;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class EmailChangeRequest {

    private final UUID id;
    private final AccountId accountId;
    private final Email currentEmail;
    private final Email newEmail;
    private final String oldTokenHash;
    private final String newTokenHash;
    private final Instant createdAt;

    private Instant oldConfirmedAt;
    private Instant newConfirmedAt;
    private EmailChangeRequestStatus status;
    private final Instant expiresAt;

    private EmailChangeRequest(
            UUID id,
            AccountId accountId,
            Email currentEmail,
            Email newEmail,
            String oldTokenHash,
            String newTokenHash,
            Instant oldConfirmedAt,
            Instant newConfirmedAt,
            EmailChangeRequestStatus status,
            Instant createdAt,
            Instant expiresAt
    ) {
        this.id = Objects.requireNonNull(id);
        this.accountId = Objects.requireNonNull(accountId);
        this.currentEmail = Objects.requireNonNull(currentEmail);
        this.newEmail = Objects.requireNonNull(newEmail);
        this.oldTokenHash = Objects.requireNonNull(oldTokenHash);
        this.newTokenHash = Objects.requireNonNull(newTokenHash);
        this.oldConfirmedAt = oldConfirmedAt;
        this.newConfirmedAt = newConfirmedAt;
        this.status = Objects.requireNonNull(status);
        this.createdAt = Objects.requireNonNull(createdAt);
        this.expiresAt = Objects.requireNonNull(expiresAt);
    }

    public static EmailChangeRequest create(
            AccountId accountId,
            Email currentEmail,
            Email newEmail,
            String oldTokenHash,
            String newTokenHash,
            Instant expiresAt
    ) {
        return new EmailChangeRequest(
                UUID.randomUUID(),
                accountId,
                currentEmail,
                newEmail,
                oldTokenHash,
                newTokenHash,
                null,
                null,
                EmailChangeRequestStatus.PENDING,
                Instant.now(),
                expiresAt
        );
    }

    public static EmailChangeRequest restore(
            UUID id,
            AccountId accountId,
            Email currentEmail,
            Email newEmail,
            String oldTokenHash,
            String newTokenHash,
            Instant oldConfirmedAt,
            Instant newConfirmedAt,
            EmailChangeRequestStatus status,
            Instant createdAt,
            Instant expiresAt
    ) {
        return new EmailChangeRequest(
                id,
                accountId,
                currentEmail,
                newEmail,
                oldTokenHash,
                newTokenHash,
                oldConfirmedAt,
                newConfirmedAt,
                status,
                createdAt,
                expiresAt
        );
    }

    public void confirmOld(String tokenHash, Instant now) {
        ensurePending();

        if (!oldTokenHash.equals(tokenHash)) {
            throw new IllegalArgumentException("Invalid email change token");
        }

        if (oldConfirmedAt == null) {
            oldConfirmedAt = now;
        }
    }

    public void confirmNew(String tokenHash, Instant now) {
        ensurePending();

        if (!newTokenHash.equals(tokenHash)) {
            throw new IllegalArgumentException("Invalid email change token");
        }

        if (newConfirmedAt == null) {
            newConfirmedAt = now;
        }
    }

    public boolean hasBothConfirmations() {
        return oldConfirmedAt != null && newConfirmedAt != null;
    }

    public void complete() {
        ensurePending();

        if (!hasBothConfirmations()) {
            throw new IllegalStateException("Both email addresses must be confirmed");
        }

        this.status = EmailChangeRequestStatus.COMPLETED;
    }

    public void cancel() {
        ensurePending();

        this.status = EmailChangeRequestStatus.CANCELLED;
    }

    public void expire() {
        ensurePending();

        this.status = EmailChangeRequestStatus.EXPIRED;
    }

    public boolean isPending() {
        return status == EmailChangeRequestStatus.PENDING;
    }

    public boolean isExpired(Instant now) {
        return !now.isBefore(expiresAt);
    }

    private void ensurePending() {
        if (status != EmailChangeRequestStatus.PENDING) {
            throw new IllegalStateException("Email change request is not pending");
        }
    }

    public UUID getId() {
        return id;
    }

    public AccountId getAccountId() {
        return accountId;
    }

    public Email getCurrentEmail() {
        return currentEmail;
    }

    public Email getNewEmail() {
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

    public EmailChangeRequestStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }
}

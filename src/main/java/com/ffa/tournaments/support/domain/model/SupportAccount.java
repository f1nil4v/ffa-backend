package com.ffa.tournaments.support.domain.model;

import com.ffa.tournaments.identity.domain.valueobject.Email;
import com.ffa.tournaments.support.domain.valueobject.SupportAccountId;

import java.time.Instant;
import java.util.Objects;

public class SupportAccount {

    private final SupportAccountId id;
    private final Email email;
    private final String passwordHash;
    private final String displayName;
    private final boolean active;
    private final Instant createdAt;

    private SupportAccount(
            SupportAccountId id,
            Email email,
            String passwordHash,
            String displayName,
            boolean active,
            Instant createdAt
    ) {
        this.id = Objects.requireNonNull(id);
        this.email = Objects.requireNonNull(email);
        this.passwordHash = Objects.requireNonNull(passwordHash);
        this.displayName = validateDisplayName(displayName);
        this.active = active;
        this.createdAt = Objects.requireNonNull(createdAt);
    }

    public static SupportAccount create(Email email, String passwordHash, String displayName) {
        return new SupportAccount(
                SupportAccountId.generate(),
                email,
                passwordHash,
                displayName,
                true,
                Instant.now()
        );
    }

    public static SupportAccount restore(
            SupportAccountId id,
            Email email,
            String passwordHash,
            String displayName,
            boolean active,
            Instant createdAt
    ) {
        return new SupportAccount(id, email, passwordHash, displayName, active, createdAt);
    }

    private static String validateDisplayName(String displayName) {
        Objects.requireNonNull(displayName, "Display name cannot be null");

        String normalized = displayName.trim();

        if (normalized.isBlank()) {
            throw new IllegalArgumentException("Display name cannot be blank");
        }

        if (normalized.length() > 100) {
            throw new IllegalArgumentException("Display name cannot exceed 100 characters");
        }

        return normalized;
    }

    public SupportAccountId getId() {
        return id;
    }

    public Email getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}

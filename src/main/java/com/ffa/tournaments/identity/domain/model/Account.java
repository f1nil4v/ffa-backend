package com.ffa.tournaments.identity.domain.model;

import com.ffa.tournaments.identity.domain.valueobject.AccountId;
import com.ffa.tournaments.identity.domain.valueobject.Email;

import java.time.Instant;
import java.util.Objects;

public class Account {

    private final AccountId id;
    private Email email;
    private String passwordHash;
    private boolean emailVerified;
    private final Instant createdAt;

    private Account(AccountId id, Email email, String passwordHash, boolean emailVerified, Instant createdAt) {
        this.id = Objects.requireNonNull(id);
        this.email = Objects.requireNonNull(email);
        this.passwordHash = Objects.requireNonNull(passwordHash);
        this.emailVerified = emailVerified;
        this.createdAt = Objects.requireNonNull(createdAt);
    }

    public static Account register(AccountId id, Email email, String passwordHash) {
        return new Account(id, email, passwordHash, false, Instant.now());
    }

    public static Account restore(AccountId id, Email email, String passwordHash, boolean emailVerified, Instant createdAt) {
        return new Account(id, email, passwordHash, emailVerified, createdAt);
    }

    public void verifyEmail() {
        this.emailVerified = true;
    }

    public void changePassword(String newPasswordHash) {
        this.passwordHash = Objects.requireNonNull(newPasswordHash);
    }

    public void changeEmail(Email newEmail) {
        this.email = Objects.requireNonNull(newEmail);
    }

    public AccountId getId() {
        return id;
    }

    public Email getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
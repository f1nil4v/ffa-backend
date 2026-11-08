package com.ffa.tournaments.team.domain.model;

import com.ffa.tournaments.player.domain.valueobject.PlayerProfileId;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Team {

    public static final int MIN_ROSTER_SIZE = 5;
    public static final int MAX_ROSTER_SIZE = 7;

    private final UUID id;
    private String name;
    private String tag;
    private PlayerProfileId captainProfileId;
    private String description;
    private final Instant createdAt;

    private Team(
            UUID id,
            String name,
            String tag,
            PlayerProfileId captainProfileId,
            String description,
            Instant createdAt
    ) {
        this.id = Objects.requireNonNull(id);
        this.name = validateName(name);
        this.tag = validateTag(tag);
        this.captainProfileId = Objects.requireNonNull(captainProfileId);
        this.description = validateDescription(description);
        this.createdAt = Objects.requireNonNull(createdAt);
    }

    public static Team create(
            String name,
            String tag,
            PlayerProfileId captainProfileId,
            String description
    ) {
        return new Team(
                UUID.randomUUID(),
                name,
                tag,
                captainProfileId,
                description,
                Instant.now()
        );
    }

    public static Team restore(
            UUID id,
            String name,
            String tag,
            PlayerProfileId captainProfileId,
            String description,
            Instant createdAt
    ) {
        return new Team(id, name, tag, captainProfileId, description, createdAt);
    }

    public void update(String name, String tag, String description) {
        this.name = validateName(name);
        this.tag = validateTag(tag);
        this.description = validateDescription(description);
    }

    public void transferCaptain(PlayerProfileId newCaptainProfileId) {
        this.captainProfileId = Objects.requireNonNull(newCaptainProfileId);
    }

    public boolean isCaptain(PlayerProfileId playerProfileId) {
        return captainProfileId.equals(playerProfileId);
    }

    private static String validateName(String name) {
        Objects.requireNonNull(name, "Team name cannot be null");

        String normalized = name.trim();

        if (normalized.length() < 2 || normalized.length() > 64) {
            throw new IllegalArgumentException("Team name must contain 2-64 characters");
        }

        return normalized;
    }

    private static String validateTag(String tag) {
        Objects.requireNonNull(tag, "Team tag cannot be null");

        String normalized = tag.trim();

        if (!normalized.matches("^[A-Za-z0-9_-]{2,16}$")) {
            throw new IllegalArgumentException(
                    "Team tag must contain 2-16 characters: Latin letters, numbers, _ or -"
            );
        }

        return normalized;
    }

    private static String validateDescription(String description) {
        if (description == null) {
            return null;
        }

        String normalized = description.trim();

        if (normalized.length() > 1000) {
            throw new IllegalArgumentException("Team description cannot exceed 1000 characters");
        }

        return normalized.isBlank() ? null : normalized;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTag() {
        return tag;
    }

    public PlayerProfileId getCaptainProfileId() {
        return captainProfileId;
    }

    public String getDescription() {
        return description;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}

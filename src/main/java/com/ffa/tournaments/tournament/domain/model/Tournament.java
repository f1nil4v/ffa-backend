package com.ffa.tournaments.tournament.domain.model;

import com.ffa.tournaments.support.domain.valueobject.SupportAccountId;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Tournament {

    public static final int MIN_TEAMS = 2;
    public static final int MAX_TEAMS = 128;

    private final UUID id;
    private String name;
    private String description;
    private TournamentFormat format;
    private TournamentStatus status;
    private int maxTeams;
    private Instant registrationOpensAt;
    private Instant registrationClosesAt;
    private Instant startsAt;
    private final SupportAccountId createdBy;
    private final Instant createdAt;
    private Instant updatedAt;

    private Tournament(
            UUID id,
            String name,
            String description,
            TournamentFormat format,
            TournamentStatus status,
            int maxTeams,
            Instant registrationOpensAt,
            Instant registrationClosesAt,
            Instant startsAt,
            SupportAccountId createdBy,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = Objects.requireNonNull(id);
        this.name = validateName(name);
        this.description = validateDescription(description);
        this.format = Objects.requireNonNull(format);
        this.status = Objects.requireNonNull(status);
        this.maxTeams = validateMaxTeams(maxTeams);
        this.registrationOpensAt = Objects.requireNonNull(registrationOpensAt);
        this.registrationClosesAt = Objects.requireNonNull(registrationClosesAt);
        validateWindow(registrationOpensAt, registrationClosesAt, startsAt);
        this.startsAt = startsAt;
        this.createdBy = Objects.requireNonNull(createdBy);
        this.createdAt = Objects.requireNonNull(createdAt);
        this.updatedAt = Objects.requireNonNull(updatedAt);
    }

    public static Tournament create(
            String name,
            String description,
            TournamentFormat format,
            int maxTeams,
            Instant registrationOpensAt,
            Instant registrationClosesAt,
            Instant startsAt,
            SupportAccountId createdBy
    ) {
        Instant now = Instant.now();

        return new Tournament(
                UUID.randomUUID(),
                name,
                description,
                format,
                TournamentStatus.DRAFT,
                maxTeams,
                registrationOpensAt,
                registrationClosesAt,
                startsAt,
                createdBy,
                now,
                now
        );
    }

    public static Tournament restore(
            UUID id,
            String name,
            String description,
            TournamentFormat format,
            TournamentStatus status,
            int maxTeams,
            Instant registrationOpensAt,
            Instant registrationClosesAt,
            Instant startsAt,
            SupportAccountId createdBy,
            Instant createdAt,
            Instant updatedAt
    ) {
        return new Tournament(
                id,
                name,
                description,
                format,
                status,
                maxTeams,
                registrationOpensAt,
                registrationClosesAt,
                startsAt,
                createdBy,
                createdAt,
                updatedAt
        );
    }

    public void update(
            String name,
            String description,
            TournamentFormat format,
            int maxTeams,
            Instant registrationOpensAt,
            Instant registrationClosesAt,
            Instant startsAt
    ) {
        ensureDraft();

        this.name = validateName(name);
        this.description = validateDescription(description);
        this.format = Objects.requireNonNull(format);
        this.maxTeams = validateMaxTeams(maxTeams);
        validateWindow(registrationOpensAt, registrationClosesAt, startsAt);
        this.registrationOpensAt = Objects.requireNonNull(registrationOpensAt);
        this.registrationClosesAt = Objects.requireNonNull(registrationClosesAt);
        this.startsAt = startsAt;
        touch();
    }

    public void openRegistration() {
        ensureDraft();

        this.status = TournamentStatus.REGISTRATION;
        touch();
    }

    public void closeRegistration() {
        if (status != TournamentStatus.REGISTRATION) {
            throw new IllegalStateException("Tournament registration is not open");
        }

        this.status = TournamentStatus.ONGOING;
        touch();
    }

    public void finish() {
        if (status != TournamentStatus.ONGOING) {
            throw new IllegalStateException("Tournament is not ongoing");
        }

        this.status = TournamentStatus.FINISHED;
        touch();
    }

    public void cancel() {
        if (status != TournamentStatus.DRAFT && status != TournamentStatus.REGISTRATION) {
            throw new IllegalStateException("Tournament cannot be cancelled");
        }

        this.status = TournamentStatus.CANCELLED;
        touch();
    }

    public boolean isRegistrationOpen(Instant now) {
        return status == TournamentStatus.REGISTRATION
                && !now.isBefore(registrationOpensAt)
                && now.isBefore(registrationClosesAt);
    }

    private void ensureDraft() {
        if (status != TournamentStatus.DRAFT) {
            throw new IllegalStateException("Tournament can only be modified in DRAFT status");
        }
    }

    private void touch() {
        this.updatedAt = Instant.now();
    }

    private static String validateName(String name) {
        Objects.requireNonNull(name, "Tournament name cannot be null");

        String normalized = name.trim();

        if (normalized.length() < 2 || normalized.length() > 128) {
            throw new IllegalArgumentException(
                    "Tournament name must contain 2-128 characters"
            );
        }

        return normalized;
    }

    private static String validateDescription(String description) {
        if (description == null) {
            return null;
        }

        String normalized = description.trim();

        if (normalized.length() > 2000) {
            throw new IllegalArgumentException(
                    "Tournament description cannot exceed 2000 characters"
            );
        }

        return normalized.isBlank() ? null : normalized;
    }

    private static int validateMaxTeams(int maxTeams) {
        if (maxTeams < MIN_TEAMS || maxTeams > MAX_TEAMS) {
            throw new IllegalArgumentException(
                    "Max teams must be between " + MIN_TEAMS + " and " + MAX_TEAMS
            );
        }

        return maxTeams;
    }

    private static void validateWindow(
            Instant registrationOpensAt,
            Instant registrationClosesAt,
            Instant startsAt
    ) {
        Objects.requireNonNull(registrationOpensAt, "Registration open time is required");
        Objects.requireNonNull(registrationClosesAt, "Registration close time is required");

        if (!registrationClosesAt.isAfter(registrationOpensAt)) {
            throw new IllegalArgumentException(
                    "Registration close time must be after open time"
            );
        }

        if (startsAt != null && startsAt.isBefore(registrationClosesAt)) {
            throw new IllegalArgumentException(
                    "Tournament start time cannot be before registration closes"
            );
        }
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public TournamentFormat getFormat() {
        return format;
    }

    public TournamentStatus getStatus() {
        return status;
    }

    public int getMaxTeams() {
        return maxTeams;
    }

    public Instant getRegistrationOpensAt() {
        return registrationOpensAt;
    }

    public Instant getRegistrationClosesAt() {
        return registrationClosesAt;
    }

    public Instant getStartsAt() {
        return startsAt;
    }

    public SupportAccountId getCreatedBy() {
        return createdBy;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}

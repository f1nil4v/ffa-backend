package com.ffa.tournaments.tournament.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tournaments")
public class TournamentJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false, length = 128)
    private String name;

    @Column
    private String description;

    @Column(nullable = false, length = 32)
    private String format;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(name = "max_teams", nullable = false)
    private int maxTeams;

    @Column(name = "registration_opens_at", nullable = false)
    private Instant registrationOpensAt;

    @Column(name = "registration_closes_at", nullable = false)
    private Instant registrationClosesAt;

    @Column(name = "starts_at")
    private Instant startsAt;

    @Column(name = "created_by", nullable = false, updatable = false)
    private UUID createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected TournamentJpaEntity() {
    }

    public TournamentJpaEntity(
            UUID id,
            String name,
            String description,
            String format,
            String status,
            int maxTeams,
            Instant registrationOpensAt,
            Instant registrationClosesAt,
            Instant startsAt,
            UUID createdBy,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.format = format;
        this.status = status;
        this.maxTeams = maxTeams;
        this.registrationOpensAt = registrationOpensAt;
        this.registrationClosesAt = registrationClosesAt;
        this.startsAt = startsAt;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public String getFormat() {
        return format;
    }

    public String getStatus() {
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

    public UUID getCreatedBy() {
        return createdBy;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}

package com.ffa.tournaments.team.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "teams")
public class TeamJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false, length = 64)
    private String name;

    @Column(nullable = false, length = 16)
    private String tag;

    @Column(name = "captain_profile_id", nullable = false)
    private UUID captainProfileId;

    @Column
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected TeamJpaEntity() {
    }

    public TeamJpaEntity(
            UUID id,
            String name,
            String tag,
            UUID captainProfileId,
            String description,
            Instant createdAt
    ) {
        this.id = id;
        this.name = name;
        this.tag = tag;
        this.captainProfileId = captainProfileId;
        this.description = description;
        this.createdAt = createdAt;
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

    public UUID getCaptainProfileId() {
        return captainProfileId;
    }

    public String getDescription() {
        return description;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}

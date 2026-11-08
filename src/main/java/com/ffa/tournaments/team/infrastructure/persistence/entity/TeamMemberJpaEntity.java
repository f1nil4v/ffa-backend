package com.ffa.tournaments.team.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "team_members")
@IdClass(TeamMemberId.class)
public class TeamMemberJpaEntity {

    @Id
    @Column(name = "team_id", nullable = false)
    private UUID teamId;

    @Id
    @Column(name = "player_profile_id", nullable = false)
    private UUID playerProfileId;

    @Column(nullable = false, length = 20)
    private String role;

    @Column(name = "joined_at", nullable = false)
    private Instant joinedAt;

    protected TeamMemberJpaEntity() {
    }

    public TeamMemberJpaEntity(
            UUID teamId,
            UUID playerProfileId,
            String role,
            Instant joinedAt
    ) {
        this.teamId = teamId;
        this.playerProfileId = playerProfileId;
        this.role = role;
        this.joinedAt = joinedAt;
    }

    public UUID getTeamId() {
        return teamId;
    }

    public UUID getPlayerProfileId() {
        return playerProfileId;
    }

    public String getRole() {
        return role;
    }

    public Instant getJoinedAt() {
        return joinedAt;
    }
}

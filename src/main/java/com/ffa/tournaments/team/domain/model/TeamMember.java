package com.ffa.tournaments.team.domain.model;

import com.ffa.tournaments.player.domain.valueobject.PlayerProfileId;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class TeamMember {

    private final UUID teamId;
    private final PlayerProfileId playerProfileId;
    private TeamMemberRole role;
    private final Instant joinedAt;

    private TeamMember(
            UUID teamId,
            PlayerProfileId playerProfileId,
            TeamMemberRole role,
            Instant joinedAt
    ) {
        this.teamId = Objects.requireNonNull(teamId);
        this.playerProfileId = Objects.requireNonNull(playerProfileId);
        this.role = Objects.requireNonNull(role);
        this.joinedAt = Objects.requireNonNull(joinedAt);
    }

    public static TeamMember createCaptain(UUID teamId, PlayerProfileId playerProfileId) {
        return new TeamMember(
                teamId,
                playerProfileId,
                TeamMemberRole.CAPTAIN,
                Instant.now()
        );
    }

    public static TeamMember createMember(UUID teamId, PlayerProfileId playerProfileId) {
        return new TeamMember(
                teamId,
                playerProfileId,
                TeamMemberRole.MEMBER,
                Instant.now()
        );
    }

    public static TeamMember restore(
            UUID teamId,
            PlayerProfileId playerProfileId,
            TeamMemberRole role,
            Instant joinedAt
    ) {
        return new TeamMember(teamId, playerProfileId, role, joinedAt);
    }

    public void promoteToCaptain() {
        this.role = TeamMemberRole.CAPTAIN;
    }

    public void demoteToMember() {
        this.role = TeamMemberRole.MEMBER;
    }

    public boolean isCaptain() {
        return role == TeamMemberRole.CAPTAIN;
    }

    public UUID getTeamId() {
        return teamId;
    }

    public PlayerProfileId getPlayerProfileId() {
        return playerProfileId;
    }

    public TeamMemberRole getRole() {
        return role;
    }

    public Instant getJoinedAt() {
        return joinedAt;
    }
}

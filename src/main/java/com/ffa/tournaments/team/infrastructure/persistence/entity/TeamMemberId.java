package com.ffa.tournaments.team.infrastructure.persistence.entity;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class TeamMemberId implements Serializable {

    private UUID teamId;
    private UUID playerProfileId;

    public TeamMemberId() {
    }

    public TeamMemberId(UUID teamId, UUID playerProfileId) {
        this.teamId = teamId;
        this.playerProfileId = playerProfileId;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof TeamMemberId that)) {
            return false;
        }

        return Objects.equals(teamId, that.teamId)
                && Objects.equals(playerProfileId, that.playerProfileId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamId, playerProfileId);
    }
}

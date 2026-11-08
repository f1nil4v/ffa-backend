package com.ffa.tournaments.team.presentation.dto.response;

import com.ffa.tournaments.team.application.result.TeamResult;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record TeamResponse(
        UUID id,
        String name,
        String tag,
        UUID captainProfileId,
        String description,
        Instant createdAt,
        List<TeamMemberResponse> members
) {

    public static TeamResponse from(TeamResult result) {
        return new TeamResponse(
                result.id(),
                result.name(),
                result.tag(),
                result.captainProfileId(),
                result.description(),
                result.createdAt(),
                result.members()
                        .stream()
                        .map(TeamMemberResponse::from)
                        .toList()
        );
    }
}

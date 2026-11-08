package com.ffa.tournaments.team.presentation.dto.request;

import jakarta.validation.constraints.Size;

public record ApplyToTeamRequest(
        @Size(max = 500) String message
) {
}

package com.ffa.tournaments.team.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateTeamRequest(

        @NotBlank
        @Size(min = 2, max = 64)
        String name,

        @NotBlank
        @Size(min = 2, max = 16)
        @Pattern(regexp = "^[A-Za-z0-9_-]+$")
        String tag,

        @Size(max = 1000)
        String description
) {
}

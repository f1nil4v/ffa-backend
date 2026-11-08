package com.ffa.tournaments.tournament.presentation.dto.request;

import com.ffa.tournaments.tournament.domain.model.TournamentFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public record UpdateTournamentRequest(

        @NotBlank
        @Size(min = 2, max = 128)
        String name,

        @Size(max = 2000)
        String description,

        @NotNull
        TournamentFormat format,

        @Min(2)
        @Max(128)
        int maxTeams,

        @NotNull
        Instant registrationOpensAt,

        @NotNull
        Instant registrationClosesAt,

        Instant startsAt
) {
}

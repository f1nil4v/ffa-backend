package com.ffa.tournaments.team.presentation.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record TransferCaptainRequest(
        @NotNull UUID playerProfileId
) {
}

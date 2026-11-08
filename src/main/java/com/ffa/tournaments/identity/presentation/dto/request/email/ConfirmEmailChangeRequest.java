package com.ffa.tournaments.identity.presentation.dto.request.email;

import jakarta.validation.constraints.NotBlank;

public record ConfirmEmailChangeRequest(
        @NotBlank String token
) {
}

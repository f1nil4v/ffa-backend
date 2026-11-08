package com.ffa.tournaments.identity.presentation.dto.request.email;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record VerifyEmailRequest(
        @NotBlank @Size(max = 128) String token
) {}
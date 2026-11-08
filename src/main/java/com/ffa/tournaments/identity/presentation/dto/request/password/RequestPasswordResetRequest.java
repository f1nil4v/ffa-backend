package com.ffa.tournaments.identity.presentation.dto.request.password;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RequestPasswordResetRequest(
        @NotBlank @Email @Size(max = 254) String email
) {}
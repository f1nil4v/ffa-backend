package com.ffa.tournaments.identity.presentation.dto.response.auth;

import java.util.UUID;

public record RegisterResponse(UUID id, String email, boolean emailVerified) {
}
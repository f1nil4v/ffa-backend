package com.ffa.tournaments.identity.presentation.dto.response.auth;

import java.util.UUID;

public record LoginResponse(UUID id, String email, String accessToken, String tokenType) {
}
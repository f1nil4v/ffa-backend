package com.ffa.tournaments.identity.application.result;

import java.util.UUID;

public record LoginResult(UUID id, String email, String accessToken, String tokenType) {
}
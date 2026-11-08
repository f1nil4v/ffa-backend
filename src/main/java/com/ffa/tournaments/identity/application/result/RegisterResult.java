package com.ffa.tournaments.identity.application.result;

import java.util.UUID;

public record RegisterResult(UUID id, String email, boolean emailVerified) {
}
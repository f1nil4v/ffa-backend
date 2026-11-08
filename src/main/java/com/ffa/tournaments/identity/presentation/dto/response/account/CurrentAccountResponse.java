package com.ffa.tournaments.identity.presentation.dto.response.account;

import java.util.UUID;

public record CurrentAccountResponse(UUID id, String email) {}
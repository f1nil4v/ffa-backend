package com.ffa.tournaments.player.presentation.dto.response;

import java.util.UUID;

public record PlayerProfileResponse(
        boolean profileExists,
        UUID id,
        String nickname,
        String avatarUrl,
        String steamId
) {}
package com.ffa.tournaments.player.presentation.dto.response;

import com.ffa.tournaments.player.application.result.NicknameChangeRequestResult;

import java.time.Instant;
import java.util.UUID;

public record NicknameChangeRequestResponse(
        UUID id,
        String oldNickname,
        String newNickname,
        String reason,
        String status,
        Instant createdAt,
        Instant reviewedAt
) {

    public static NicknameChangeRequestResponse from(NicknameChangeRequestResult result) {
        return new NicknameChangeRequestResponse(
                result.id(),
                result.oldNickname(),
                result.newNickname(),
                result.reason(),
                result.status(),
                result.createdAt(),
                result.reviewedAt()
        );
    }
}

package com.ffa.tournaments.player.application.result;

import com.ffa.tournaments.player.domain.model.NicknameChangeRequest;

import java.time.Instant;
import java.util.UUID;

public record NicknameChangeRequestResult(
        UUID id,
        String oldNickname,
        String newNickname,
        String reason,
        String status,
        Instant createdAt,
        Instant reviewedAt
) {

    public static NicknameChangeRequestResult from(NicknameChangeRequest request) {
        return new NicknameChangeRequestResult(
                request.getId(),
                request.getOldNickname(),
                request.getNewNickname(),
                request.getReason(),
                request.getStatus().name(),
                request.getCreatedAt(),
                request.getReviewedAt()
        );
    }
}

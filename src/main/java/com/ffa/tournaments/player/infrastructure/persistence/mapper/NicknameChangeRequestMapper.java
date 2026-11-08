package com.ffa.tournaments.player.infrastructure.persistence.mapper;

import com.ffa.tournaments.support.domain.valueobject.SupportAccountId;
import com.ffa.tournaments.player.domain.model.NicknameChangeRequest;
import com.ffa.tournaments.player.domain.model.NicknameChangeRequestStatus;
import com.ffa.tournaments.player.domain.valueobject.PlayerProfileId;
import com.ffa.tournaments.player.infrastructure.persistence.entity.NicknameChangeRequestJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class NicknameChangeRequestMapper {

    public NicknameChangeRequestJpaEntity toEntity(
            NicknameChangeRequest request
    ) {
        return new NicknameChangeRequestJpaEntity(
                request.getId(),
                request.getPlayerProfileId().value(),
                request.getOldNickname(),
                request.getNewNickname(),
                request.getReason(),
                request.getStatus().name(),
                request.getReviewedBy() == null
                        ? null
                        : request.getReviewedBy().value(),
                request.getCreatedAt(),
                request.getReviewedAt()
        );
    }

    public NicknameChangeRequest toDomain(
            NicknameChangeRequestJpaEntity entity
    ) {
        return NicknameChangeRequest.restore(
                entity.getId(),
                PlayerProfileId.from(entity.getPlayerProfileId()),
                entity.getOldNickname(),
                entity.getNewNickname(),
                entity.getReason(),
                NicknameChangeRequestStatus.valueOf(entity.getStatus()),
                entity.getCreatedAt(),
                entity.getReviewedBy() == null
                        ? null
                        : SupportAccountId.from(entity.getReviewedBy()),
                entity.getReviewedAt()
        );
    }
}
package com.ffa.tournaments.player.infrastructure.persistence.mapper;

import com.ffa.tournaments.player.domain.model.PlayerNicknameHistory;
import com.ffa.tournaments.player.domain.valueobject.PlayerProfileId;
import com.ffa.tournaments.player.infrastructure.persistence.entity.PlayerNicknameHistoryJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class PlayerNicknameHistoryMapper {

    public PlayerNicknameHistoryJpaEntity toEntity(PlayerNicknameHistory history) {
        return new PlayerNicknameHistoryJpaEntity(
                history.getId(),
                history.getPlayerProfileId().value(),
                history.getNickname(),
                history.getValidFrom(),
                history.getValidTo()
        );
    }

    public PlayerNicknameHistory toDomain(PlayerNicknameHistoryJpaEntity entity) {
        return PlayerNicknameHistory.restore(
                entity.getId(),
                PlayerProfileId.from(entity.getPlayerProfileId()),
                entity.getNickname(),
                entity.getValidFrom(),
                entity.getValidTo()
        );
    }
}
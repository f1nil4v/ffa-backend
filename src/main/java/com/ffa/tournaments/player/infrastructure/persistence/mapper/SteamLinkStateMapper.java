package com.ffa.tournaments.player.infrastructure.persistence.mapper;

import com.ffa.tournaments.identity.domain.valueobject.AccountId;
import com.ffa.tournaments.player.domain.model.SteamLinkState;
import com.ffa.tournaments.player.infrastructure.persistence.entity.SteamLinkStateJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class SteamLinkStateMapper {

    public SteamLinkStateJpaEntity toEntity(SteamLinkState state) {
        return new SteamLinkStateJpaEntity(
                state.getId(),
                state.getAccountId().value(),
                state.getCreatedAt(),
                state.getExpiresAt()
        );
    }

    public SteamLinkState toDomain(SteamLinkStateJpaEntity entity) {
        return SteamLinkState.restore(
                entity.getId(),
                AccountId.from(entity.getAccountId()),
                entity.getCreatedAt(),
                entity.getExpiresAt()
        );
    }
}

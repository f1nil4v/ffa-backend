package com.ffa.tournaments.player.infrastructure.persistence.mapper;

import com.ffa.tournaments.identity.domain.valueobject.AccountId;
import com.ffa.tournaments.player.domain.model.PlayerProfile;
import com.ffa.tournaments.player.domain.valueobject.PlayerProfileId;
import com.ffa.tournaments.player.infrastructure.persistence.entity.PlayerProfileJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class PlayerProfileMapper {

    public PlayerProfileJpaEntity toEntity(PlayerProfile profile) {
        return new PlayerProfileJpaEntity(
                profile.getId().value(),
                profile.getAccountId().value(),
                profile.getNickname(),
                profile.getAvatarUrl(),
                profile.getSteamId(),
                profile.getSteamLinkedAt(),
                profile.getCreatedAt()
        );
    }

    public PlayerProfile toDomain(PlayerProfileJpaEntity entity) {
        return PlayerProfile.restore(
                PlayerProfileId.from(entity.getId()),
                AccountId.from(entity.getAccountId()),
                entity.getNickname(),
                entity.getAvatarUrl(),
                entity.getSteamId(),
                entity.getSteamLinkedAt(),
                entity.getCreatedAt()
        );
    }
}